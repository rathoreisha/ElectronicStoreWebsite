package com.shruteekatech.ecommerce.controller;

import com.shruteekatech.ecommerce.dtos.AdditemToCart;
import com.shruteekatech.ecommerce.dtos.ApiResponse;
import com.shruteekatech.ecommerce.dtos.CartDto;
import com.shruteekatech.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Api/Carts")
public class CartController {
    @Autowired
    private CartService cartService;


    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addCartItemTocart(@PathVariable Long userId, @RequestBody AdditemToCart request) {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/userid/{userId}/item/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
        cartService.removeItemfromCart(userId, itemId);
        ApiResponse itemIsRemoved = ApiResponse.builder().message("Item is removed").success(true).status(HttpStatus.OK).build();

        return new ResponseEntity<>(itemIsRemoved, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        ApiResponse apiResponse = ApiResponse.builder().message("Now cart is clear !!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long userId) {
        CartDto cartByUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartByUser, HttpStatus.OK);
    }


}
