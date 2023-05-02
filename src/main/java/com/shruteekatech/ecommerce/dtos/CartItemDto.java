package com.shruteekatech.ecommerce.dtos;

import com.shruteekatech.ecommerce.model.Cart;
import com.shruteekatech.ecommerce.model.Product;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemDto {

    private Long cartItemId;

    private ProductDto product;

    private Integer quantity;

    private double totalPrice;

}
