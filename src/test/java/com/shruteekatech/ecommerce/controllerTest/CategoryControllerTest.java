package com.shruteekatech.ecommerce.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shruteekatech.ecommerce.BaseTest;
import com.shruteekatech.ecommerce.controller.CategoryController;
import com.shruteekatech.ecommerce.dtos.CategoryDto;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.dtos.ProductDto;
import com.shruteekatech.ecommerce.dtos.UserDto;
import com.shruteekatech.ecommerce.model.Category;
import com.shruteekatech.ecommerce.service.CategoryService;
import com.shruteekatech.ecommerce.service.FileService;
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
public class CategoryControllerTest extends BaseTest {
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;
    @MockBean
    private FileService fileService;

    @Autowired
    private CategoryController categoryController;
    Category  category;

    CategoryDto categoryDto,categoryDto1,categoryDto2;
    List<CategoryDto> categories;

    @BeforeEach
    public void init()
    {
        category = Category.builder()
                .title("Electronics")
                .description("All Electronics items")
                .coverImage("xyz.png").build();


        categoryDto = CategoryDto.builder().categoryId(1l)
                .title("Cometics")
                .description("All cosmetics are avilable")
                .coverImage("xyz.png")
                .build();
        categoryDto1 = CategoryDto.builder()
                .title("Cometics")
                .description("All cosmetics are avilable")
                .coverImage("xyz.png")
                .build();
        categoryDto2= CategoryDto.builder()
                .title("Cometics")
                .description("All cosmetics are avilable")
                .coverImage("xyz.png")
                .build();

        categories = new ArrayList<>();
        categories.add(categoryDto);
        categories.add(categoryDto1);
        categories.add(categoryDto2);
    }
    @Test
    public void createCategoryTest() throws Exception {
        CategoryDto dto = this.modelMapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.createCategory(Mockito.any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/Category/create")
                .contentType(MediaType.APPLICATION_JSON).content(converobjectTojsonString(category))
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isCreated() ).andExpect(jsonPath("$.title").exists());
    }
    @Test
    public void updateUserTest() throws Exception {
        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        Long id=1l;
        Mockito.when(categoryService.updateCategory(Mockito.any(),Mockito.anyLong())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/Category/update/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converobjectTojsonString(category)).
                        accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void getAllcategoriesTest() throws Exception {
        PagableResponse<CategoryDto> pagableResponse=new PagableResponse<>();
        pagableResponse.setContent(categories);
        pagableResponse.setPageNumber(1);
        pagableResponse.setPageSize(3);
        pagableResponse.setTotalPages(2);

        Mockito.when(categoryService.getAllcategories(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pagableResponse);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/Category/getall")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getSingleCategoryTest() throws Exception {
        Long id=2l;
        Mockito.when(categoryService.getSingleCategory(Mockito.anyLong())).thenReturn(categoryDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/Category/getsingle/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

    }
    @Test
    public void searchCategoryTest() throws Exception {
        String keyword="Mobiles";
        Mockito.when(categoryService.searchCategory(Mockito.anyString())).thenReturn(categories);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/Category/"+keyword)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());


    }
    @Test
    public void deleteCategoryTest() throws Exception {
        doNothing().when(categoryService).deleteCategory(Mockito.<Long>any());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/Category/"+1l)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void serveCategoryimageTest() throws Exception {
        when(fileService.getResource(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ByteArrayInputStream("Image Uploaded".getBytes("UTF-8")));
        when(categoryService.getSingleCategory(Mockito.<Long>any())).thenReturn(new CategoryDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/Category/image/{catid}", 1L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("image/png"))
                .andExpect(MockMvcResultMatchers.content().string("Image uploaded"));
    }

    @Test
    void testGenerateReport() throws Exception {
        when(categoryService.exportrept(Mockito.<String>any())).thenReturn("Exportrept");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/Category/report/{format}", "Format");
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Exportrept"));
    }

    private String converobjectTojsonString(Category category) {
        try
        {
            return new ObjectMapper().writeValueAsString(category);
        } catch (RuntimeException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
