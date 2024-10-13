package com.cart_service.repository;

import com.cart_service.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Override
    Optional<Cart> findById(Long id);

    Optional<Cart> findByToken(String token);

    @Query("SELECT c FROM Cart c WHERE (:token IS NOT NULL AND c.token = :token) OR (:id IS NOT NULL AND c.id = :id)")
    Optional<Cart> findByTokenOrId(@Param("token") String token, @Param("id") Long id);

    @Query("SELECT c FROM Cart c WHERE (:token IS NOT NULL AND c.token = :token) AND (:id IS NOT NULL AND c.id = :id)")
    Optional<Cart> findByTokenAndId(@Param("token") String token, @Param("id") Long id);
}
