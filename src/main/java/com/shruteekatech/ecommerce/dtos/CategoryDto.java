package com.shruteekatech.ecommerce.dtos;

import com.shruteekatech.ecommerce.constant.ValidationConstant;

import com.shruteekatech.ecommerce.validation.ImageNameValid;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto extends BaseEntityDto {

    private Long categoryId;

    @NotEmpty
    @Size(min=5,max = 50,message = ValidationConstant.TITLE_MSG)
    private String title;


    @NotEmpty
    @Size(min=5,max = 150,message =ValidationConstant.DESCRIPTION_MSG)
    private String description;


    @ImageNameValid
    private String coverImage;

    private List<ProductDto> products;
}
