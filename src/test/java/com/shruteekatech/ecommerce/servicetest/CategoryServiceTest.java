package com.shruteekatech.ecommerce.servicetest;

import com.shruteekatech.ecommerce.BaseTest;
import com.shruteekatech.ecommerce.dtos.CategoryDto;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.model.Category;
import com.shruteekatech.ecommerce.repository.CategoryRepo;
import com.shruteekatech.ecommerce.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryServiceTest extends BaseTest {

    @MockBean
    private CategoryRepo categoryRepo;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;


    Category category1, category2, category3;

    List<Category> categories;

    CategoryDto categoryDto;


    @BeforeEach
    public void init() {
        category1 = Category.builder()
                .title("Electronics")
                .description("All Electronics items")
                .coverImage("xyz.png").build();
        category2 = Category.builder()
                .title("Electronics")
                .description("All Electronics items")
                .coverImage("xyz.png").build();
        category3 = Category.builder()
                .title("Electronics")
                .description("All Electronics items")
                .coverImage("xyz.png").build();

        categoryDto = CategoryDto.builder()
                .title("Cometics")
                .description("All cosmetics are avilable")
                .coverImage("xyz.png")

                .build();

        categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);

    }

    @Test
    public void createCategoryTest() {
        Mockito.when(categoryRepo.save(Mockito.any())).thenReturn(category1);

        CategoryDto category = categoryService.createCategory(modelMapper.map(category1, CategoryDto.class));
        Assertions.assertNotNull(category);
        Assertions.assertEquals(category1.getTitle(), category.getTitle());


    }

    @Test
    public void updateCategoryTest() {
        Long id = 1l;
        Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(category1));
        Mockito.when(categoryRepo.save(Mockito.any())).thenReturn(category1);
        CategoryDto categoryDto1 = categoryService.updateCategory(categoryDto, id);

        Assertions.assertNotNull(categoryDto1);
        Assertions.assertEquals(category1.getTitle(), categoryDto1.getTitle());

    }

    @Test
    public void deleteCategoryTest() {
        Long id = 10l;
        Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(category1));

        categoryService.deleteCategory(id);
        Mockito.verify(categoryRepo, Mockito.times(1)).delete(category1);

    }

    @Test
    public void getSingleCategoryTest() {
        Long id = 10l;
        Mockito.when(categoryRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(category1));

        CategoryDto singleCategory = categoryService.getSingleCategory(id);

        Assertions.assertEquals(category1.getCategoryId(), singleCategory.getCategoryId());

    }

    @Test
    public void getAllcategoriesTest() {
        Page page = new PageImpl(categories);
        Mockito.when(categoryRepo.findAll((Pageable) Mockito.any())).thenReturn(page);

        PagableResponse<CategoryDto> allcategories = categoryService.getAllcategories(1, 5, "brand", "asc");


        Assertions.assertEquals(3, allcategories.getContent().size());

    }

    @Test
    public void searchCategoryTest() {
        Mockito.when(categoryRepo.findAByTitleContaining(Mockito.anyString())).thenReturn(categories);

        List<CategoryDto> categoryDtoList = categoryService.searchCategory("Cometics");

        Assertions.assertEquals(3, categoryDtoList.size(), "Not present data!");

    }
}
