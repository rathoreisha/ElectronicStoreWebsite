package com.shruteekatech.ecommerce.service.impl;

import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.dtos.AdditemToCart;
import com.shruteekatech.ecommerce.dtos.CartDto;
import com.shruteekatech.ecommerce.exception.BadApiException;
import com.shruteekatech.ecommerce.exception.ResourcenotFoundException;
import com.shruteekatech.ecommerce.model.Cart;
import com.shruteekatech.ecommerce.model.CartItem;
import com.shruteekatech.ecommerce.model.Product;
import com.shruteekatech.ecommerce.model.User;
import com.shruteekatech.ecommerce.repository.CartItemRepository;
import com.shruteekatech.ecommerce.repository.CartRepository;
import com.shruteekatech.ecommerce.repository.ProductRepo;
import com.shruteekatech.ecommerce.repository.UserRepository;
import com.shruteekatech.ecommerce.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(Long userId, AdditemToCart request) {

        Integer quantity = request.getQuantity();
        Long productId = request.getProductId();
        if(quantity<=0)
        {
            throw new BadApiException("Requested quantity is not valid !!");
        }
//        Fetch the product
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourcenotFoundException(AppConstant.PRODUCT, AppConstant.WITH_ID, productId));

//        fetch the user
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourcenotFoundException(AppConstant.USER_ID, AppConstant.WITH_ID, userId));

        Cart cart = null;
        try {
            cart = cartRepo.findByUser(user).get();

        } catch (NoSuchElementException ex) {
            cart = new Cart();

        }
//        perform cart operation
//        if cart item already present than update

//        boolean updated=false;
        AtomicReference<Boolean> updated=new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items.stream().map(item->{

            if(item.getProduct().getPid().equals(productId))
            {
//                if item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getPrice());
                updated.set(true);
            }
            return item;

        }).collect(Collectors.toList());
//        create cartItem

        if(!updated.get())
        {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product).build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updateCart = cartRepo.save(cart);

        return mapper.map(updateCart,CartDto.class);
    }

    @Override
    public void removeItemfromCart(Long userId, Long cartItem) {
        CartItem cartItem1 = cartItemRepo.findById(cartItem).orElseThrow(() -> new ResourcenotFoundException("CartItem Not found!!"));

        cartItemRepo.delete(cartItem1);

    }

    @Override
    public void clearCart(Long userId) {

        User user = userRepo.findById(userId).orElseThrow(() -> new ResourcenotFoundException(AppConstant.USER_ID, AppConstant.WITH_ID, userId));
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourcenotFoundException("Cart Not found"));
        cart.getItems().clear();
        cartRepo.save(cart);

    }

    @Override
    public CartDto getCartByUser(Long userID) {
        User user = userRepo.findById(userID).orElseThrow(() -> new ResourcenotFoundException(AppConstant.USER_ID, AppConstant.WITH_ID, userID));
        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourcenotFoundException("Cart Not found"));

        return mapper.map(cart,CartDto.class);
    }
}
