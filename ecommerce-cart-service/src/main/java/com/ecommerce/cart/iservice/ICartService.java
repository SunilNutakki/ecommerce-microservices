
package com.ecommerce.cart.iservice;

import com.ecommerce.cartservice.dto.AddCartItemRequest;
import com.ecommerce.cartservice.dto.CartDetailsResponse;
import com.ecommerce.cartservice.dto.CartItemResponse;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.CreateCartRequest;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;

public interface ICartService {

    CartResponse createOrGetCart(CreateCartRequest request);
    
    // Add this method for API 2
    CartItemResponse addCartItem(
            String cartId,
            AddCartItemRequest request);
    
    CartDetailsResponse getCartDetails(String cartId);
    
    CartItemResponse updateCartItem(
            String cartId,
            String itemId,
            UpdateCartItemRequest request);
    
    String removeCartItem(String cartId, String itemId);
    
    String clearCart(String cartId);
    
    CartDetailsResponse getActiveCartByUserId(String userId);
    CartDetailsResponse checkoutCart(String cartId);

}
