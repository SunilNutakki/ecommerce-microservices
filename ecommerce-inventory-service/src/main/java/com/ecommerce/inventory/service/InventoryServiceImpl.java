
package com.ecommerce.inventory.service;
import java.util.Objects;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.inventory.client.ProductServiceClient;
import com.ecommerce.inventory.dto.AddStockRequest;
import com.ecommerce.inventory.dto.InventoryReservationResponse;
import com.ecommerce.inventory.dto.InventoryStockResponse;
import com.ecommerce.inventory.dto.InventoryTransactionResponse;
import com.ecommerce.inventory.dto.ProductItemResponse;
import com.ecommerce.inventory.dto.ReserveStockRequest;
import com.ecommerce.inventory.entity.InventoryReservation;
import com.ecommerce.inventory.entity.InventoryStock;
import com.ecommerce.inventory.entity.InventoryTransaction;
import com.ecommerce.inventory.repository.InventoryReservationRepository;
import com.ecommerce.inventory.repository.InventoryStockRepository;
import com.ecommerce.inventory.repository.InventoryTransactionRepository;
import com.ecommerce.mapper.InventoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryStockRepository stockRepository;
    private final InventoryReservationRepository reservationRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductServiceClient productServiceClient;
    private final InventoryStockRepository inventoryStockRepository;
    private final ReservationIdempotencyService idempotencyService;
    private final InventoryReservationTransactionService
    reservationTransactionService;	
//    public InventoryServiceImpl(
//            InventoryStockRepository stockRepository,
//            InventoryReservationRepository reservationRepository,
//            InventoryTransactionRepository transactionRepository,
//            InventoryMapper inventoryMapper,ProductServiceClient productServiceClient,InventoryStockRepository inventoryStockRepository) {
//
//        this.stockRepository = stockRepository;
//        this.reservationRepository = reservationRepository;
//        this.transactionRepository = transactionRepository;
//        this.inventoryMapper = inventoryMapper;   
//        this.productServiceClient = productServiceClient;
//        this.inventoryStockRepository = inventoryStockRepository;
//    }

    // 1. Get all inventory stock
    @Override
    @Transactional(readOnly = true)
    public List<InventoryStockResponse> getAllStock() {

        return stockRepository.findAll()
                .stream()
                .map(inventoryMapper::toStockResponse)
                .toList();
    }

    // 2. Get stock by product item ID
    @Override
    @Transactional(readOnly = true)
    public InventoryStockResponse getStockByProductItemId(
            String productItemId) {

        InventoryStock stock = stockRepository
                .findByProductItemId(productItemId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Inventory stock not found for product item: "
                                        + productItemId));

        return inventoryMapper.toStockResponse(stock);
    }

    // 3. Add incoming stock
    
    @Override
    public InventoryReservationResponse reserveStock(
            ReserveStockRequest request) {

        String idempotencyKey = request.getIdempotencyKey();

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException(
                    "Idempotency key is required");
        }

        validatePositiveQuantity(request.getQuantity());

        // Check for an existing reservation
        Optional<InventoryReservation> existing =
                idempotencyService.findExistingReservation(idempotencyKey);

        if (existing.isPresent()) {
            return validateAndReturnExisting(existing.get(), request);
        }

        try {
            // Create reservation in an independent transaction
            InventoryReservation reservation =
                    reservationTransactionService.createReservation(
                            request, idempotencyKey);

            return inventoryMapper.toReservationResponse(reservation);

        } catch (org.springframework.dao.DataIntegrityViolationException ex) {

            // Unique-key conflict: retrieve the original reservation
            Optional<InventoryReservation> duplicate =
                    idempotencyService.findExistingReservation(idempotencyKey);

            if (duplicate.isPresent()) {
                return validateAndReturnExisting(duplicate.get(), request);
            }

            throw ex;
        }
    }
    private InventoryReservationResponse validateAndReturnExisting(
            InventoryReservation existing,
            ReserveStockRequest request) {

        boolean sameRequest =
                Objects.equals(existing.getProductItemId(), request.getProductItemId())
                && Objects.equals(existing.getCartId(), request.getCartId())
                && Objects.equals(existing.getCartItemId(), request.getCartItemId())
                && Objects.equals(existing.getUserId(), request.getUserId())
                && Objects.equals(existing.getQuantity(), request.getQuantity());

        if (!sameRequest) {
            throw new IllegalArgumentException(
                    "Idempotency key already used with different request details");
        }

        return inventoryMapper.toReservationResponse(existing);
    }
    // 5. Release reserved stock
    @Override
    public InventoryReservationResponse releaseStock(String reservationId) {

        InventoryReservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Reservation not found: " + reservationId));

        if (!"RESERVED".equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException(
                    "Only RESERVED stock can be released. Current status: "
                            + reservation.getReservationStatus());
        }

        // Lock stock before changing its quantity
        InventoryStock stock = stockRepository
                .findByProductItemIdForUpdate(
                        reservation.getProductItemId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Inventory stock not found"));

        int quantity = reservation.getQuantity();

        if (stock.getReservedQuantity() < quantity) {
            throw new IllegalStateException(
                    "Reserved stock balance is inconsistent");
        }

        stock.setReservedQuantity(
                stock.getReservedQuantity() - quantity);

        stock.setAvailableQuantity(
                stock.getAvailableQuantity() + quantity);

        stockRepository.save(stock);

        reservation.setReservationStatus("RELEASED");
        reservationRepository.save(reservation);

        // Record stock movement
        InventoryTransaction transaction = new InventoryTransaction();

        transaction.setTransactionId(generateId("ITX"));
        transaction.setInventoryStock(stock);
        transaction.setTransactionType("RELEASED");
        transaction.setQuantity(quantity);
        transaction.setReferenceId(reservationId);
        transaction.setReferenceType("RESERVATION");
        transaction.setRemarks("Reserved stock released");
        transaction.setCreatedBy(reservation.getUserId());

        transactionRepository.save(transaction);

        return inventoryMapper.toReservationResponse(reservation);
    }

    private void validatePositiveQuantity(Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }
    }

    private String generateId(String prefix) {

        return prefix + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 16)
                .toUpperCase();
    }
    
    @Override
    public InventoryReservationResponse confirmReservation(String reservationId) {

        InventoryReservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation not found: " + reservationId));

        if (!"RESERVED".equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException(
                    "Only RESERVED stock can be confirmed. Current status: "
                            + reservation.getReservationStatus());
        }

        reservation.setReservationStatus("CONFIRMED");

        InventoryReservation savedReservation =
                reservationRepository.saveAndFlush(reservation);

        InventoryTransaction transaction = new InventoryTransaction();

        transaction.setTransactionId(generateId("ITX"));
        transaction.setInventoryStock(reservation.getInventoryStock());
        transaction.setTransactionType("CONFIRMED");
        transaction.setQuantity(reservation.getQuantity());
        transaction.setReferenceId(reservation.getReservationId());
        transaction.setReferenceType("RESERVATION");
        transaction.setRemarks("Stock reservation confirmed");
        transaction.setCreatedBy(reservation.getUserId());

        transactionRepository.save(transaction);

        return inventoryMapper.toReservationResponse(savedReservation);
    }
    
    
    @Override
    public InventoryReservationResponse cancelReservation(String reservationId) {

        InventoryReservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation not found: " + reservationId));

        if (!"RESERVED".equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException(
                    "Only RESERVED stock can be cancelled. Current status: "
                            + reservation.getReservationStatus());
        }

        InventoryStock stock = inventoryStockRepository
                .findByProductItemIdForUpdate(reservation.getProductItemId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Stock not found for product item: "
                                + reservation.getProductItemId()));

        if (stock.getReservedQuantity() < reservation.getQuantity()) {
            throw new IllegalStateException(
                    "Insufficient reserved quantity to cancel reservation");
        }

        stock.setReservedQuantity(
                stock.getReservedQuantity() - reservation.getQuantity());

        stock.setAvailableQuantity(
                stock.getAvailableQuantity() + reservation.getQuantity());

        inventoryStockRepository.save(stock);

        reservation.setReservationStatus("CANCELLED");

        InventoryReservation savedReservation =
                reservationRepository.saveAndFlush(reservation);

        InventoryTransaction transaction = new InventoryTransaction();

        transaction.setTransactionId(generateId("ITX"));
        transaction.setInventoryStock(stock);
        transaction.setTransactionType("CANCELLED");
        transaction.setQuantity(reservation.getQuantity());
        transaction.setReferenceId(reservation.getReservationId());
        transaction.setReferenceType("RESERVATION");
        transaction.setRemarks("Stock reservation cancelled");
        transaction.setCreatedBy(reservation.getUserId());

        transactionRepository.save(transaction);

        return inventoryMapper.toReservationResponse(savedReservation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public InventoryReservationResponse getReservationById(String reservationId) {

        InventoryReservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation not found: " + reservationId));

        return inventoryMapper.toReservationResponse(reservation);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransactionResponse> getAllTransactions() {

        return transactionRepository.findAllWithStock()
                .stream()
                .map(inventoryMapper::toTransactionResponse)
                .toList();
    }
    

    
    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransactionResponse> getTransactionsByStockId(
            String stockId) {

        if (!inventoryStockRepository.existsById(stockId)) {
            throw new IllegalArgumentException(
                    "Stock not found: " + stockId);
        }

        return transactionRepository.findTransactionsWithStock(stockId)
                .stream()
                .map(inventoryMapper::toTransactionResponse)
                .toList();
    }
    
    

    @Override
    public InventoryStockResponse addStock(AddStockRequest request) {

        validatePositiveQuantity(request.getQuantity());

        // 1. Verify product exists
        ProductItemResponse product = productServiceClient
                .getProductItemById(request.getProductItemId());

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product item not found: " + request.getProductItemId());
        }

        // 2. Lock inventory stock
        InventoryStock stock = stockRepository
                .findByProductItemIdForUpdate(request.getProductItemId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Inventory stock not found for product item: "
                                + request.getProductItemId()));

        // 3. Update stock balance
        stock.setTotalQuantity(
                stock.getTotalQuantity() + request.getQuantity());

        stock.setAvailableQuantity(
                stock.getAvailableQuantity() + request.getQuantity());

        stockRepository.save(stock);

        // 4. Record stock movement
        InventoryTransaction transaction = new InventoryTransaction();

        transaction.setTransactionId(generateId("ITX"));
        transaction.setInventoryStock(stock);
        transaction.setTransactionType("IN");
        transaction.setQuantity(request.getQuantity());
        transaction.setReferenceType("STOCK_RECEIPT");
        transaction.setRemarks(request.getRemarks());
        transaction.setCreatedBy(request.getCreatedBy());

        transactionRepository.save(transaction);

        return inventoryMapper.toStockResponse(stock);
    }
}
