package com.shruteekatech.ecommerce.repository;


import com.shruteekatech.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category,Long> {


    List<Category> findAByTitleContaining(String keyword);
}
