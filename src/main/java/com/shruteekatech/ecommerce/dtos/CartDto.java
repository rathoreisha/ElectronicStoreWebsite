package com.shruteekatech.ecommerce.dtos;

import com.shruteekatech.ecommerce.model.CartItem;
import com.shruteekatech.ecommerce.model.User;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDto {

    private Long cartId;


    private UserDto user;


    private List<CartItemDto> items=new ArrayList<>();
}
