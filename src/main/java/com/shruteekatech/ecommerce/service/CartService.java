package com.shruteekatech.ecommerce.service;

import com.shruteekatech.ecommerce.dtos.AdditemToCart;
import com.shruteekatech.ecommerce.dtos.CartDto;


public interface CartService {

//    add item to cart
//    case 1:cart for user is not available  :we will create the cart  and
//    case 2: if cart avaible add item to the cart

    CartDto addItemToCart(Long userId, AdditemToCart request);

//    remove item from cart
    void removeItemfromCart(Long userId,Long cartItem);
    void clearCart(Long userId);

//    Get cart by user

    CartDto getCartByUser(Long userID);
}
