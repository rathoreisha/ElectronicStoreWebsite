package com.shruteekatech.ecommerce.dtos;


import com.shruteekatech.ecommerce.constant.ValidationConstant;
import com.shruteekatech.ecommerce.validation.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto extends BaseEntityDto {
//Data transfer object-DTO

    private Long id;

    @NotEmpty
    @Size(min=3,max=15,message = ValidationConstant.USER )
    private String name;

    @NotEmpty
    @Email(message = ValidationConstant.EMAIL)
    private String email;

    @NotEmpty
    @Pattern(regexp = ValidationConstant.PASSWORD_PATTERN,message= ValidationConstant.PASSWORD)
    private String password;

    @NotEmpty
    @Size(min = 4,max = 6,message = ValidationConstant.GENDER_MSG)
    private String gender;

    @NotEmpty
    @Size(min = 10,max = 100,message = ValidationConstant.ABOUT)
    private String about;

    @NotEmpty
    @ImageNameValid
    private String imageName;



}
