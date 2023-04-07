package com.shruteekatech.ecommerce.service.impl;


import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.dtos.ProductDto;
import com.shruteekatech.ecommerce.exception.ResourcenotFoundException;
import com.shruteekatech.ecommerce.helper.Pageablemethod;
import com.shruteekatech.ecommerce.model.Product;
import com.shruteekatech.ecommerce.repository.ProductRepo;
import com.shruteekatech.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductImpl implements ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ModelMapper modelMapper;
    /**
     * @param productDto
     * @return
     */
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Intiating dao call to save the Product");
        Product product = this.modelMapper.map(productDto, Product.class);
        product.setIsactive(AppConstant.ACTIVE);
        Product products = this.productRepo.save(product);
        log.info("Completed dao call to save the Product");
        return this.modelMapper.map(products,ProductDto.class);
    }

    /**
     * @param productDto
     * @param productid
     * @return
     */
    @Override
    public ProductDto updateProducts(ProductDto productDto, Long productid) {
        log.info("Initiating dao call  For Updating Product with product id:{}",productid);
        Product product = this.productRepo.findById(productid).orElseThrow(() -> new ResourcenotFoundException(AppConstant.PRODUCT, AppConstant.WITH_ID, productid));
        product.setTitle(productDto.getTitle());
        product.setLive(productDto.getLive());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        Product updatedproduct = this.productRepo.save(product);
        log.info("Completed dao call  For Updating Product with product id:{}",productid);
        return this.modelMapper.map(updatedproduct,ProductDto.class);
    }

    /**
     * @param productID
     * @return
     */
    @Override
    public ProductDto getById(Long productID) {
        log.info("Intiating Dao call to Get the Product With productid :{}",productID);
        Product product = this.productRepo.findById(productID).orElseThrow(() -> new ResourcenotFoundException(AppConstant.PRODUCT, AppConstant.WITH_ID, productID));
        log.info("Completed Dao call to Get the Product With productid :{}",productID);
        return  this.modelMapper.map(product,ProductDto.class);
    }

    /**
     * @param productId
     */
    @Override
    public void deleteProduct(Long productId) {
        log.info("Intiating Dao call to Delete the Product With productid :{}",productId);
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourcenotFoundException(AppConstant.PRODUCT, AppConstant.WITH_ID, productId));
        this.productRepo.delete(product);
        log.info("Completed Dao call to Delete the Product With productid :{}",productId);

    }

    /**
     * @param pagesize
     * @param pagenumber
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Override
    public PagableResponse<ProductDto> getAllProducts(Integer pagesize, Integer pagenumber, String sortBy, String sortDir) {
        log.info("Intiating dao call to GetAllproducts ");
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending()) ;
        Pageable page= PageRequest.of(pagenumber,pagesize,sort);
        Page<Product> products = this.productRepo.findAll(page);

        PagableResponse<ProductDto> pageableresponse = Pageablemethod.getPageableresponse(products, ProductDto.class);

        log.info("Completed dao call to GetAllproducts ");
        return pageableresponse;
    }

    /**
     * @param title
     * @param pagesize
     * @param pagenumber
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Override
    public PagableResponse<ProductDto> searchByTitle(String title, Integer pagesize, Integer pagenumber, String sortBy, String sortDir) {
        log.info("Intiating dao call to GetAllproducts through search by title:{} ",title);
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending()) ;
        Pageable page= PageRequest.of(pagenumber,pagesize,sort);
        Page<Product> titleContaining = this.productRepo.findAllByTitleContaining(title,page);

        PagableResponse<ProductDto> pageableresponse = Pageablemethod.getPageableresponse(titleContaining, ProductDto.class);

        log.info("completed dao call to GetAllproducts through search by title:{} ",title);
        return pageableresponse;
    }

    /**
     * @param live
     * @param pagesize
     * @param pagenumber
     * @param sortBy
     * @param sortDir
     * @return
     */
    @Override
    public PagableResponse<ProductDto> searchByLive(Boolean live, Integer pagesize, Integer pagenumber, String sortBy, String sortDir) {
        log.info("Intiating dao call to GetAllproducts through search live :{}",live);
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending()) ;
        Pageable page= PageRequest.of(pagenumber,pagesize,sort);
        Page<Product> products = this.productRepo.findAllByLiveTrue(page);

        PagableResponse<ProductDto> pageableresponse = Pageablemethod.getPageableresponse(products, ProductDto.class);

        log.info("Completed dao call to GetAllproducts through search live :{}",live);
        return pageableresponse;
    }
}
