package com.shruteekatech.ecommerce.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shruteekatech.ecommerce.BaseTest;
import com.shruteekatech.ecommerce.controller.ProductController;
import com.shruteekatech.ecommerce.dtos.CategoryDto;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.dtos.ProductDto;
import com.shruteekatech.ecommerce.model.Product;
import com.shruteekatech.ecommerce.service.FileService;
import com.shruteekatech.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ProductControllerTest extends BaseTest {
    @MockBean
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Autowired
    private ProductController productController;
    Product product1;

    List<ProductDto> products;

    ProductDto productDto, productDto1, productDto2;

    @BeforeEach
    public void init() {
        product1 = Product.builder()
                .brand("sugar")
                .title("Cosmetics")
                .price(1000.0).live(true).stock(true)
                .description("All cosmetics are available")
                .quantity(10).discount("20%").imageName("abc.png").build();


        productDto = ProductDto.builder().pid(1l)
                .brand("sugar")
                .title("Cometics")
                .price(1000.0).live(true).stock(true)
                .description("All cosmetics are avilable")
                .quantity(10).discount("20%").build();
        productDto1 = ProductDto.builder()
                .brand("sugar")
                .title("Cometics")
                .price(1000.0).live(true).stock(true)
                .description("All cosmetics are avilable")
                .quantity(10).discount("20%").build();
        productDto2 = ProductDto.builder()
                .brand("sugar")
                .title("Cometics")
                .price(1000.0).live(true).stock(true)
                .description("All cosmetics are avilable")
                .quantity(10).discount("20%").build();

        products = new ArrayList<>();
        products.add(productDto);
        products.add(productDto1);
        products.add(productDto2);

    }

    @Test
    public void createProductTest() throws Exception {
        ProductDto dto = this.modelMapper.map(product1, ProductDto.class);
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products/")
                        .contentType(MediaType.APPLICATION_JSON).content(converobjectTojsonString(product1))
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated()).andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void createProductwithCategoryTest() throws Exception {
        ProductDto map = this.modelMapper.map(product1, ProductDto.class);
        Long catid = 1l;
        Mockito.when(productService.createProductwithCategory(Mockito.anyLong(), Mockito.any())).thenReturn(map);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products/category/" + catid)
                .contentType(MediaType.APPLICATION_JSON).content(converobjectTojsonString(product1))
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.title").exists());


    }

    @Test
    public void updateProductsTest() throws Exception {
        ProductDto dto = this.modelMapper.map(product1, ProductDto.class);

        Long id = 1l;
        Mockito.when(productService.updateProducts(Mockito.any(), Mockito.anyLong())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converobjectTojsonString(product1)).
                        accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void getAllProductsTest() throws Exception {
        PagableResponse<ProductDto> pagableResponse = new PagableResponse<>();
        pagableResponse.setContent(products);
        pagableResponse.setPageNumber(1);
        pagableResponse.setPageSize(3);
        pagableResponse.setTotalPages(2);

        Mockito.when(productService.getAllProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pagableResponse);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getByIdTest() throws Exception {
        Long id = 2l;
        Mockito.when(productService.getById(Mockito.anyLong())).thenReturn(productDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products/byId/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void searchByTitleTest() throws Exception {
        String keyword = "cosmetics";
        PagableResponse<ProductDto> pagableResponse = new PagableResponse<>();
        pagableResponse.setContent(products);
        pagableResponse.setPageNumber(1);
        pagableResponse.setPageSize(3);
        pagableResponse.setTotalPages(2);
        Mockito.when(productService.searchByTitle(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pagableResponse);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + keyword)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());


    }

    @Test
    public void searchByLiveTest() throws Exception {
        Boolean live = true;
        PagableResponse<ProductDto> pagableResponse = new PagableResponse<>();
        pagableResponse.setContent(products);
        pagableResponse.setPageNumber(1);
        pagableResponse.setPageSize(3);
        pagableResponse.setTotalPages(2);
        Mockito.when(productService.searchByLive(Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pagableResponse);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products/true/" + live)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());


    }

    @Test
    public void deleteProductTest() throws Exception {
        doNothing().when(productService).deleteProduct(Mockito.<Long>any());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + 1l)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }


    @Test
    void serveCategoryimageTest() throws Exception {
        when(fileService.getResource(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ByteArrayInputStream("Image uploaded".getBytes("UTF-8")));
        when(productService.getById(Mockito.<Long>any())).thenReturn(productDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/images/{productid}", 1L);
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("image/png"))
                .andExpect(MockMvcResultMatchers.content().string("Image uploaded"));
    }

    private String converobjectTojsonString(Product product) {
        try {
            return new ObjectMapper().writeValueAsString(product);
        } catch (RuntimeException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
