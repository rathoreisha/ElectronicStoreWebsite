package com.shruteekatech.ecommerce.controller;


import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.dtos.ApiResponse;
import com.shruteekatech.ecommerce.dtos.CategoryDto;
import com.shruteekatech.ecommerce.dtos.ImageResponse;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.service.CategoryService;
import com.shruteekatech.ecommerce.service.FileService;
import lombok.extern.slf4j.Slf4j;

import net.sf.jasperreports.engine.JRException;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/Category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Value("${category.profile.image.path}")
    private String imageuploadpath;
    @Autowired
    private FileService fileService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@Valid  @RequestBody CategoryDto categoryDto)
    {

        log.info("Initiating request to Create Category");
        CategoryDto category = this.categoryService.createCategory(categoryDto);
        log.info("Completed request to Create Category");
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @PutMapping("/update/{catid}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Long catid )
    {
        log.info("Initiating the request to update the Category :{}",catid);
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, catid);
        log.info("Completed the request to update the Category :{}",catid);
        return new ResponseEntity<>(updateCategory,HttpStatus.CREATED);
    }

    @GetMapping("/getsingle/{catid}")
    public ResponseEntity<CategoryDto> getsinglecategory(@PathVariable Long catid)
    {
        log.info("Initiating the request to Get the Category :{}",catid);
        CategoryDto singleCategory = this.categoryService.getSingleCategory(catid);
        log.info("Completed the request to Get the Category :{}",catid);
        return new ResponseEntity<>(singleCategory,HttpStatus.OK);
    }
    @GetMapping("/")
    public ResponseEntity<PagableResponse> getAllcategories(@RequestParam(value = "pagenumber", defaultValue = "0", required = false) Integer pagenumber,
                                                            @RequestParam(value = "pagesize", defaultValue = "10", required = false) Integer pagesize
                                                          , @RequestParam(value = "sortBy", defaultValue = "categoryId", required = false) String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)
    {
        log.info("Initiated request to get all the Category details");
        PagableResponse<CategoryDto> allcategories = this.categoryService.getAllcategories(pagenumber, pagesize, sortBy, sortDir);

        log.info("Completed request to get all the Category details");
        return new ResponseEntity<>(allcategories,HttpStatus.OK);
    }

    @DeleteMapping("/{catid}")
    public ResponseEntity<ApiResponse> deletecategory(@PathVariable Long catid)
    {
        log.info("Initiating the request to delete the Category :{}",catid);
        this.categoryService.deleteCategory(catid);
        ApiResponse apiResponse =ApiResponse.builder().message(AppConstant.DELETE).status(HttpStatus.OK).success(true).build();
        log.info("Completed the request to update the Category :{}",catid);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/{keyword}")
    public  ResponseEntity<List<CategoryDto>> searchCategory(@PathVariable String keyword)
    {
        log.info("Initiating the request to Search the Category :{}",keyword);
        List<CategoryDto> searchCategory = this.categoryService.searchCategory(keyword);
        log.info("Completed the request to Search the Category :{}",keyword);
        return new ResponseEntity<>(searchCategory,HttpStatus.OK);
    }

    @PostMapping("/image/{categoryid}/")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable Long categoryid, @RequestParam("userimage") MultipartFile file) throws IOException {
        log.info("Initiating the request to Upload the image with categoryid:{}",categoryid);
        CategoryDto category = this.categoryService.getSingleCategory(categoryid);

        String uploadImage = this.fileService.uploadImage(file, imageuploadpath);

        category.setCoverImage(uploadImage);
        this.categoryService.updateCategory(category,categoryid);

        ImageResponse imageResponse=ImageResponse.builder().imagename(uploadImage).message("Image Uploaded").status(true).build();
        log.info(" Completed the request to upload image process",categoryid);
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }
    //    To serve the user image
    @GetMapping("/image/{catid}")
    public void  serveUserimage(@PathVariable Long catid, HttpServletResponse response) throws IOException {
        log.info("Initiating the request to Serve the image with categoryid:{}",catid);
        CategoryDto category = this.categoryService.getSingleCategory(catid);
        InputStream resource = this.fileService.getResource(imageuploadpath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        log.info("Completed the request to Serve the image with categoryid:{}",catid);

    }

    @GetMapping("/report/{format}")
    public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException, JRException, FileNotFoundException {
        log.info("initiated request to genrate the reports  with Format:{}",format);
        log.info("completed request to genrate the reports  with Format:{}",format);
        return this.categoryService.exportrept(format);
    }
}
