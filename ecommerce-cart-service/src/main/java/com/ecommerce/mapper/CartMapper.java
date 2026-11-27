
package com.ecommerce.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecommerce.cartservice.dto.CartDetailsResponse;
import com.ecommerce.cartservice.dto.CartItemResponse;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.ProductItemResponse;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

    // API 1: Cart Entity -> CartResponse
    @Mapping(source = "cart.status.statusName", target = "status")
    @Mapping(source = "message", target = "message")
    CartResponse toResponse(Cart cart, String message);


    // API 3: CartItem Entity -> CartItemResponse
    @Mapping(source = "cartItem.cart.cartId", target = "cartId")
    @Mapping(source = "cartItem.productItemId", target = "productItemId")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "cartItem.quantity", target = "quantity")
    @Mapping(source = "cartItem.unitPrice", target = "unitPrice")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "message", target = "message")
    CartItemResponse toCartItemResponse(
            CartItem cartItem,
            ProductItemResponse product,
            BigDecimal totalPrice,
            String message);


    // API 3: Cart Entity -> CartDetailsResponse
    @Mapping(source = "cart.cartId", target = "cartId")
    @Mapping(source = "cart.userId", target = "userId")
    @Mapping(source = "cart.status.statusName", target = "status")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "totalItems", target = "totalItems")
    @Mapping(source = "grandTotal", target = "grandTotal")
    CartDetailsResponse toCartDetailsResponse(
            Cart cart,
            java.util.List<CartItemResponse> items,
            Integer totalItems,
            BigDecimal grandTotal);
}
