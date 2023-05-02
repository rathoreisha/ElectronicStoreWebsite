package com.shruteekatech.ecommerce.controller;


import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.constant.ValidationConstant;
import com.shruteekatech.ecommerce.dtos.*;
import com.shruteekatech.ecommerce.service.FileService;
import com.shruteekatech.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;
    @Value("${product.profile.image.path}")
    private String imageuploadpath;

    @PostMapping("/")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto)
    {
        log.info("Intiating request to Create Product");
        ProductDto product = this.productService.createProduct(productDto);
        log.info("Completed request to create the Product");
        return new ResponseEntity<>(product, HttpStatus.CREATED);

    }
    @PostMapping("/{catid}")
    public ResponseEntity<ProductDto> createProductwithcategory(@PathVariable Long catid,@Valid @RequestBody ProductDto productDto)
    {
        log.info("Intiating request to Create Product with category id :{}",catid);
        ProductDto product = this.productService.createProductwithCategory(catid,productDto);
        log.info("Completed request to create the Product with category id :{}",catid);
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
    @GetMapping("/allProducts")
    public ResponseEntity<PagableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pagenumber", defaultValue = ValidationConstant.PAGE_NUMBER, required = false) Integer pagenumber,
            @RequestParam(value = "pagesize", defaultValue = ValidationConstant.PAGE_SIZE, required = false) Integer pagesize,
            @RequestParam(value = "sortBy", defaultValue = ValidationConstant.SORT_BY_PRODUCT, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue =ValidationConstant.SORT_DIR, required = false) String sortDir)

    {
        log.info("Intiating request to GetAll Products");
        PagableResponse<ProductDto> products = this.productService.getAllProducts(pagesize, pagenumber, sortBy, sortDir);
        log.info("Completed request to GetAll Products");
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @GetMapping ("/{title}")
    public ResponseEntity<PagableResponse<ProductDto>> searchbytitle(
            @PathVariable String title,
            @RequestParam(value = "pagenumber", defaultValue = ValidationConstant.PAGE_NUMBER, required = false) Integer pagenumber,
            @RequestParam(value = "pagesize", defaultValue = ValidationConstant.PAGE_SIZE, required = false) Integer pagesize,
            @RequestParam(value = "sortBy", defaultValue = ValidationConstant.SORT_BY_PRODUCT, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue =ValidationConstant.SORT_DIR, required = false) String sortDir)

    {
        log.info("Intiating request to GetAll Products by searching title:{}",title);
        PagableResponse<ProductDto> products = this.productService.searchByTitle(title,pagesize, pagenumber, sortBy, sortDir);
        log.info("Completed request to GetAll Products by searching title:{}",title);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @GetMapping("/true/{live}")
    public ResponseEntity<PagableResponse<ProductDto>> searchBylive(
            @PathVariable Boolean live,
            @RequestParam(value = "pagenumber", defaultValue = ValidationConstant.PAGE_NUMBER, required = false) Integer pagenumber,
            @RequestParam(value = "pagesize", defaultValue = ValidationConstant.PAGE_SIZE, required = false) Integer pagesize,
            @RequestParam(value = "sortBy", defaultValue = ValidationConstant.SORT_BY_PRODUCT, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue =ValidationConstant.SORT_DIR, required = false) String sortDir)

    {
        log.info("Intiating request to GetAll Products by searching live:{}",live);
        PagableResponse<ProductDto> products = this.productService.searchByLive(live,pagesize, pagenumber, sortBy, sortDir);
        log.info("Intiating request to GetAll Products by searching live:{}",live);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @PostMapping("/image/{productid}/")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable Long productid, @RequestParam("userimage") MultipartFile file) throws IOException {
        log.info("Upload the image with productid:{}",productid);
        ProductDto productDto = this.productService.getById(productid);

        String uploadImage = this.fileService.uploadImage(file, imageuploadpath);

        productDto.setImageName(uploadImage);
         this.productService.updateProducts(productDto, productid);


        ImageResponse imageResponse=ImageResponse.builder().imagename(uploadImage).message("Image Uploaded").status(true).build();
        log.info("Completed the upload image process",productid);
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    //    To serve the user image
    @GetMapping("/images/{productid}")
    public void  serveimage(@PathVariable Long productid, HttpServletResponse response) throws IOException {
        log.info("initiated request to serve image with productid:{}",productid);
        ProductDto productDto = this.productService.getById(productid);
        InputStream resource = this.fileService.getResource(imageuploadpath, productDto.getImageName());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        log.info("Completed request to serve image with productid:{}",productid);
    }

}
