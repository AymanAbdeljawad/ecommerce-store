package com.cart_service.repository;

import com.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
    Integer deleteByCartId(Long cartId);

    Optional<CartItem> findByProductName(String productName);

    @Query("SELECT c FROM CartItem c WHERE c.productName = :productName AND c.token = :token")
    Optional<CartItem> findByProductNameAndToken(@Param("productName") String productName, @Param("token") String token);

    boolean existsByCartId(Long cartId);


}
