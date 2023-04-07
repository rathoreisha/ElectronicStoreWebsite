package com.shruteekatech.ecommerce.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid,String> {


    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    private  Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        logger.info("Message from isValid : {} ",value);
        //logic

        if (value.isBlank()){
            return false;
        }else {
            return true;
        }
}
}
