package com.shruteekatech.ecommerce.exception;


import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.dtos.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourcenotFoundException.class)
    public ResponseEntity<ApiResponse> usernotfoundExceptionhandle(ResourcenotFoundException e)
    {
//        String message = e.getMessage();
//        ApiResponse apiResponse = new ApiResponse(message, false);
        ApiResponse apiResponse = ApiResponse.builder().message(e.getMessage()).status(HttpStatus.NOT_FOUND).success(false).build();
        return  new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse> emailnotfoundExceptionhandle(EmailNotFoundException e)
    {
        ApiResponse apiResponse =ApiResponse.builder().message(e.getMessage()).status(HttpStatus.NOT_FOUND).success(false).build();
        return  new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodargsNotValidException(MethodArgumentNotValidException ex)
    {
        Map<String, String> resp = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String field = ((FieldError)error).getField();
            String defaultMessage = error.getDefaultMessage();
            resp.put(field, defaultMessage);
        });

        return new ResponseEntity<Map<String, String>>(resp,HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleMethodargsNotValidException(DataIntegrityViolationException ex)
    {
        ApiResponse apiResponse = new ApiResponse(AppConstant.EMAIL_MSG, false,HttpStatus.NOT_FOUND);
        return  new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiException.class)
    public ResponseEntity<ApiResponse> handleBadApiException(BadApiException ex)
    {
        ApiResponse apiResponse =ApiResponse.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(false).build();

        return  new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

}
