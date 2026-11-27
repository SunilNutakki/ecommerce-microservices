
package com.ecommerce.cartservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.cartservice.entity.CartItem;

@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, String> {

    Optional<CartItem> findByCart_CartIdAndProductItemId(
            String cartId,
            String productItemId);
    List<CartItem> findByCart_CartId(String cartId);
    

Optional<CartItem> findByCart_CartIdAndCartItemId(
        String cartId,
        String cartItemId);

void deleteByCart_CartId(String cartId);

}
