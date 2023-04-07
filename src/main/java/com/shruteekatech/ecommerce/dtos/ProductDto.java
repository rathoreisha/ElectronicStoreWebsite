package com.shruteekatech.ecommerce.dtos;


import com.shruteekatech.ecommerce.constant.ValidationConstant;
import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ProductDto extends BaseEntityDto {

    private Long pid;
    @NotEmpty
    @Size(min=3,max=15,message = ValidationConstant.PRODUCT )
    private String title;

    @NotEmpty
    @Size(min=10,max = 10000,message = "Please Enter at least 10 Character")
    private String description;
    @DecimalMin(value = "0.1", inclusive = false)
    private Double price;
//    @NotNull
//    @Size(min=10,max = 10000,message = "Please Enter at least 10 Character")
    private Integer quantity;

    @AssertTrue
    private  Boolean live;
    @AssertTrue
    private Boolean stock;

    @NotEmpty
    private String discount;

    @NotEmpty
    private String brand;
}
