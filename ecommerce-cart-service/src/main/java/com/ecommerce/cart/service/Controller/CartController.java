
package com.ecommerce.cart.service.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.cart.iservice.ICartService;
import com.ecommerce.cartservice.dto.AddCartItemRequest;
import com.ecommerce.cartservice.dto.CartDetailsResponse;
import com.ecommerce.cartservice.dto.CartItemResponse;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.CreateCartRequest;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@Tag(
	    name = "Cart Management",
	    description = "APIs for creating, managing and checking out carts"
	)
public class CartController {
    private final ICartService cartService;

	public CartController(ICartService cartService) {
	    this.cartService = cartService;
	}

    // API 1: Create or Get Active Cart
    @PostMapping("/saveCart")
    public ResponseEntity<CartResponse> createOrGetCart(
            @Valid @RequestBody CreateCartRequest request) {

        CartResponse response = cartService.createOrGetCart(request);

        HttpStatus status = response.getMessage()
                .equals("Cart created successfully")
                ? HttpStatus.CREATED
                : HttpStatus.OK;

        return ResponseEntity.status(status).body(response);
    }
    
 // API 2: Add product to cart
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemResponse> addCartItem(
            @PathVariable String cartId,
            @Valid @RequestBody AddCartItemRequest request) {

        CartItemResponse response =
                cartService.addCartItem(cartId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
 // API 3: Get Cart Details
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDetailsResponse> getCartDetails(
            @PathVariable String cartId) {

        CartDetailsResponse response =
                cartService.getCartDetails(cartId);

        return ResponseEntity.ok(response);
    }
    

@PutMapping("/{cartId}/items/{itemId}")
public ResponseEntity<CartItemResponse> updateCartItem(
        @PathVariable String cartId,
        @PathVariable String itemId,
        @Valid @RequestBody UpdateCartItemRequest request) {

    CartItemResponse response =
            cartService.updateCartItem(cartId, itemId, request);

    return ResponseEntity.ok(response);
}
@GetMapping("/test")
public String test() {
    return "Cart Controller is working";
}

@GetMapping("/health-check")
public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Cart Controller is working");
}

@DeleteMapping("/{cartId}/items/{itemId}")
public ResponseEntity<String> removeCartItem(
        @PathVariable String cartId,
        @PathVariable String itemId) {

    String message = cartService.removeCartItem(cartId, itemId);

    return ResponseEntity.ok(message);
}

//API 6: Clear Cart
@DeleteMapping("/{cartId}/items")
public ResponseEntity<String> clearCart(
     @PathVariable String cartId) {

 String message = cartService.clearCart(cartId);

 return ResponseEntity.ok(message);
}

@GetMapping("/user/{userId}")
public ResponseEntity<CartDetailsResponse> getActiveCartByUserId(
        @PathVariable String userId) {

    CartDetailsResponse response =
            cartService.getActiveCartByUserId(userId);

    return ResponseEntity.ok(response);
}

@PostMapping("/{cartId}/checkout")
public ResponseEntity<CartDetailsResponse> checkoutCart(
        @PathVariable String cartId) {

    CartDetailsResponse response =
            cartService.checkoutCart(cartId);

    return ResponseEntity.ok(response);
}

}


