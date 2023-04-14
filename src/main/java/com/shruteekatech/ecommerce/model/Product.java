package com.shruteekatech.ecommerce.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    private String title;
    private String description;
    private Double price;
    private Integer quantity;
    private  Boolean live;
    private Boolean stock;

    private String discount;

    private String brand;



}
