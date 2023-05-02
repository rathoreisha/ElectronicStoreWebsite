package com.shruteekatech.ecommerce.repository;

import com.shruteekatech.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
