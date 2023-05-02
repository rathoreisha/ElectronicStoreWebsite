package com.shruteekatech.ecommerce.repository;

import com.shruteekatech.ecommerce.model.Cart;
import com.shruteekatech.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {


    Optional<Cart> findByUser(User user);
}
