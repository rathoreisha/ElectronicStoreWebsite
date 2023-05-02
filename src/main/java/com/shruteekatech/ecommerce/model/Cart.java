package com.shruteekatech.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    private User user;

//   cart item Mapping
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "cart",orphanRemoval = true)
    private List<CartItem> items=new ArrayList<>();


}
