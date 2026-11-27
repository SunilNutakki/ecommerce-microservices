package com.ecommerce.cart.iservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.cartservice.client.ProductServiceClient;
import com.ecommerce.cartservice.dto.AddCartItemRequest;
import com.ecommerce.cartservice.dto.CartDetailsResponse;
import com.ecommerce.cartservice.dto.CartItemResponse;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.CreateCartRequest;
import com.ecommerce.cartservice.dto.ProductItemResponse;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.entity.CartStatus;
import com.ecommerce.cartservice.repository.CartItemRepository;
import com.ecommerce.cartservice.repository.CartRepository;
import com.ecommerce.cartservice.repository.CartStatusRepository;
import com.ecommerce.mapper.CartMapper;

@Service
@Transactional
public class CartServiceImpl implements ICartService {

    private static final String ACTIVE_STATUS = "ACTIVE";

    private final CartRepository cartRepository;
    private final CartStatusRepository cartStatusRepository;
    private final CartMapper cartMapper;
    private final ProductServiceClient productServiceClient;
    private final CartItemRepository cartItemRepository;
    // Constructor Injection
    public CartServiceImpl(
            CartRepository cartRepository,
            CartStatusRepository cartStatusRepository,
            CartMapper cartMapper,CartItemRepository cartItemRepository,ProductServiceClient productServiceClient) {

        this.cartRepository = cartRepository;
        this.cartStatusRepository = cartStatusRepository;
        this.cartMapper = cartMapper;
        this.cartItemRepository = cartItemRepository;
        this.productServiceClient = productServiceClient;
    }

    @Override
    public CartResponse createOrGetCart(CreateCartRequest request) {

        String userId = request.getUserId().trim();

        // 1. Check existing active cart
        var existingCart =
                cartRepository
                    .findFirstByUserIdAndStatus_StatusNameOrderByCreatedAtDesc(
                            userId, ACTIVE_STATUS);

        if (existingCart.isPresent()) {
            return cartMapper.toResponse(
                    existingCart.get(),
                    "Existing active cart retrieved");
        }

        // 2. Get ACTIVE status
        CartStatus activeStatus =
                cartStatusRepository.findByStatusName(ACTIVE_STATUS)
                    .orElseThrow(() ->
                        new IllegalStateException(
                            "ACTIVE cart status is not configured"));

        // 3. Generate Cart ID
        String cartId = "CART" +
                UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 16)
                    .toUpperCase();

        // 4. Create Cart
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setUserId(userId);
        cart.setStatus(activeStatus);

        // 5. Save Cart
        Cart savedCart = cartRepository.save(cart);

        // 6. Return Response
        return cartMapper.toResponse(
                savedCart,
                "Cart created successfully");
    }

    // API 2: Add Product to Cart
    @Override
    public CartItemResponse addCartItem(
            String cartId,
            AddCartItemRequest request) {

        // 1. Validate Cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Cart not found: " + cartId));

        // 2. Check whether cart is ACTIVE
        if (!ACTIVE_STATUS.equalsIgnoreCase(
                cart.getStatus().getStatusName())) {

            throw new IllegalStateException(
                    "Products cannot be added to an inactive cart");
        }

        // 3. Validate product item through Product Service
        ProductItemResponse product =
                productServiceClient.getProductItem(
                        request.getProductItemId().trim());

        if (product == null || product.getSalePrice() == null) {
            throw new IllegalArgumentException(
                    "Product item or sale price not available");
        }

        BigDecimal unitPrice = product.getSalePrice();

        // 4. Check whether product already exists in cart
        var existingItem =
                cartItemRepository.findByCart_CartIdAndProductItemId(
                        cartId,
                        request.getProductItemId().trim());

        CartItem cartItem;

        if (existingItem.isPresent()) {

            // Update existing quantity
            cartItem = existingItem.get();

            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity());

            // Refresh price snapshot
            cartItem.setUnitPrice(unitPrice);

        } else {

            // 5. Create new Cart Item
            cartItem = new CartItem();

            String cartItemId = "CI" +
                    UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 18)
                        .toUpperCase();

            cartItem.setCartItemId(cartItemId);
            cartItem.setCart(cart);
            cartItem.setProductItemId(
                    request.getProductItemId().trim());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setUnitPrice(unitPrice);
        }

        // 6. Save Cart Item
        CartItem savedItem = cartItemRepository.save(cartItem);

        // 7. Calculate total price
        BigDecimal totalPrice = savedItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(savedItem.getQuantity()));

        // 8. Prepare response
        return new CartItemResponse(
                savedItem.getCartItemId(),
                cart.getCartId(),
                product.getProductItemId(),
                product.getProductId(),
                product.getProductName(),
                savedItem.getQuantity(),
                savedItem.getUnitPrice(),
                totalPrice,
                existingItem.isPresent()
                        ? "Cart item quantity updated successfully"
                        : "Product added to cart successfully");
    }
    

@Override
@Transactional(readOnly = true)
public CartDetailsResponse getCartDetails(String cartId) {

    // 1. Find Cart
    Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Cart not found: " + cartId));

    // 2. Find all Cart Items
    var cartItems =
            cartItemRepository.findByCart_CartId(cartId);

    // 3. Prepare response items
    List<CartItemResponse> itemResponses = new ArrayList<>();

    BigDecimal grandTotal = BigDecimal.ZERO;
    int totalItems = 0;

    // 4. Process each Cart Item
    for (CartItem cartItem : cartItems) {

        // Get product details from Product Service
        ProductItemResponse product =
                productServiceClient.getProductItem(
                        cartItem.getProductItemId());

        // Calculate item total
        BigDecimal itemTotal =
                cartItem.getUnitPrice().multiply(
                        BigDecimal.valueOf(cartItem.getQuantity()));

        // Map CartItem Entity to CartItemResponse
        CartItemResponse itemResponse =
                cartMapper.toCartItemResponse(
                        cartItem,
                        product,
                        itemTotal,
                        "Cart item retrieved successfully");

        itemResponses.add(itemResponse);

        // Calculate grand total
        grandTotal = grandTotal.add(itemTotal);

        totalItems += cartItem.getQuantity();
    }

    // 5. Map Cart Entity to CartDetailsResponse
    return cartMapper.toCartDetailsResponse(
            cart,
            itemResponses,
            totalItems,
            grandTotal);
}


@Override
public CartItemResponse updateCartItem(
        String cartId,
        String itemId,
        UpdateCartItemRequest request) {

    // 1. Find Cart
    Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Cart not found: " + cartId));

    // 2. Check Cart status
    if (!ACTIVE_STATUS.equalsIgnoreCase(
            cart.getStatus().getStatusName())) {

        throw new IllegalStateException(
                "Cannot update items in an inactive cart");
    }

    // 3. Find Cart Item belonging to this Cart
    CartItem cartItem =
            cartItemRepository
                    .findByCart_CartIdAndCartItemId(cartId, itemId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Cart item not found: " + itemId));

    // 4. Update quantity
    cartItem.setQuantity(request.getQuantity());

    // 5. Save updated Cart Item
    CartItem savedItem = cartItemRepository.save(cartItem);

    // 6. Get product details
    ProductItemResponse product =
            productServiceClient.getProductItem(
                    savedItem.getProductItemId());

    // 7. Calculate total price
    BigDecimal totalPrice =
            savedItem.getUnitPrice().multiply(
                    BigDecimal.valueOf(savedItem.getQuantity()));

    // 8. Map response using existing MapStruct mapper
    return cartMapper.toCartItemResponse(
            savedItem,
            product,
            totalPrice,
            "Cart item quantity updated successfully");
}

@Override
public String removeCartItem(String cartId, String itemId) {

    // Step 1: Find cart
    Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Cart not found: " + cartId));

    // Step 2: Validate cart status
    if (!ACTIVE_STATUS.equals(cart.getStatus().getStatusName())) {
        throw new IllegalArgumentException(
                "Cannot remove items from a non-active cart");
    }

    // Step 3: Find item belonging to this cart
    CartItem cartItem = cartItemRepository
            .findByCart_CartIdAndCartItemId(cartId, itemId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Cart item not found: " + itemId));

    // Step 4: Delete item
    cartItemRepository.delete(cartItem);

    return "Cart item removed successfully";
}

@Override
public String clearCart(String cartId) {

    // Step 1: Find cart
    Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Cart not found: " + cartId));

    // Step 2: Validate cart status
    if (!ACTIVE_STATUS.equals(cart.getStatus().getStatusName())) {
        throw new IllegalArgumentException(
                "Cannot clear a non-active cart");
    }

    // Step 3: Delete all items from cart
    cartItemRepository.deleteByCart_CartId(cartId);

    return "Cart cleared successfully";
}

@Override
public CartDetailsResponse getActiveCartByUserId(String userId) {

    Cart cart = cartRepository
            .findFirstByUserIdAndStatus_StatusNameOrderByCreatedAtDesc(
                    userId, ACTIVE_STATUS)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Active cart not found for user: " + userId));

    return getCartDetails(cart.getCartId());
}

@Override
public CartDetailsResponse checkoutCart(String cartId) {

    // 1. Find cart
    Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Cart not found: " + cartId));

    // 2. Validate cart status
    if (!ACTIVE_STATUS.equals(cart.getStatus().getStatusName())) {
        throw new IllegalArgumentException(
                "Only active carts can be checked out");
    }

    // 3. Validate cart items
    List<CartItem> cartItems =
            cartItemRepository.findByCart_CartId(cartId);

    if (cartItems.isEmpty()) {
        throw new IllegalArgumentException(
                "Cannot checkout an empty cart");
    }

    // 4. Update cart status
    CartStatus checkedOutStatus = cartStatusRepository
            .findByStatusName("CHECKED_OUT")
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "CHECKED_OUT status not found"));

    cart.setStatus(checkedOutStatus);

    cartRepository.save(cart);

    // 5. Return cart details
    return getCartDetails(cartId);
}


}