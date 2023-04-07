package com.shruteekatech.ecommerce.repository;


import com.shruteekatech.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

   Optional<User> findByEmail(String email);
   Optional<User> findByEmailAndPassword(String email,String password);
   List<User> findByNameContaining(String keyword);
}
