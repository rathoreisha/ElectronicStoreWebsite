package com.shruteekatech.ecommerce.controller;


import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.dtos.ApiResponse;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.dtos.ProductDto;
import com.shruteekatech.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto)
    {
        log.info("Intiating request to Create Product");
        ProductDto product = this.productService.createProduct(productDto);
        log.info("Completed request to create the Product");
        return new ResponseEntity<>(product, HttpStatus.CREATED);

    }
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,@PathVariable Long productId)
    {
        log.info("Intiating request to Update Product with productid:{}",productId);
        ProductDto productDto1 = this.productService.updateProducts(productDto, productId);
        log.info("Completed request to Update Product with productid:{}",productId);
        return new ResponseEntity<>(productDto1,HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId)
    {
        log.info("Intiating request to Delete Product with productid:{}",productId);

        this.productService.deleteProduct(productId);
        ApiResponse apiResponse =ApiResponse.builder().message(AppConstant.DELETE).status(HttpStatus.OK).success(true).build();

        log.info("Completed request to Delete Product with productid:{}",productId);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/byId/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId)
    {
        log.info("Intiating request to Get Product with productid:{}",productId);

        ProductDto productDto = this.productService.getById(productId);

        log.info("Completed request to Delete Product with productid:{}",productId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<PagableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pagenumber", defaultValue = "0", required = false) Integer pagenumber,
            @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize
            , @RequestParam(value = "sortBy", defaultValue = "pid", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)

    {
        log.info("Intiating request to GetAll Products");
        PagableResponse<ProductDto> products = this.productService.getAllProducts(pagesize, pagenumber, sortBy, sortDir);
        log.info("Completed request to GetAll Products");
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @GetMapping ("/{title}")
    public ResponseEntity<PagableResponse<ProductDto>> searchbytitle(
            @PathVariable String title,
            @RequestParam(value = "pagenumber", defaultValue = "0", required = false) Integer pagenumber,
            @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize
            , @RequestParam(value = "sortBy", defaultValue = "pid", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)

    {
        log.info("Intiating request to GetAll Products by searching title:{}",title);
        PagableResponse<ProductDto> products = this.productService.searchByTitle(title,pagesize, pagenumber, sortBy, sortDir);
        log.info("Completed request to GetAll Products by searching title:{}",title);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @GetMapping("/true")
    public ResponseEntity<PagableResponse<ProductDto>> searchBylive(
            @PathVariable Boolean live,
            @RequestParam(value = "pagenumber", defaultValue = "0", required = false) Integer pagenumber,
            @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize
            , @RequestParam(value = "sortBy", defaultValue = "pid", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)

    {
        log.info("Intiating request to GetAll Products by searching live:{}",live);
        PagableResponse<ProductDto> products = this.productService.searchByLive(live,pagesize, pagenumber, sortBy, sortDir);
        log.info("Intiating request to GetAll Products by searching live:{}",live);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

}
