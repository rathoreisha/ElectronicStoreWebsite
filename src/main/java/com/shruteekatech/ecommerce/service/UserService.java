package com.shruteekatech.ecommerce.service;


import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.dtos.UserDto;
import net.sf.jasperreports.engine.JRException;


import java.io.FileNotFoundException;
import java.util.List;

public interface UserService {


//    create
    UserDto createUser(UserDto user);
//    update

    UserDto updateUser(UserDto user,Long userid);

//    delete
    void deleteUser(Long userid);

//    getalluser

    PagableResponse getAllUsers(Integer pagenumber, Integer pagesize, String sortBy, String sortDir);

//    getsingleuser

    UserDto getSingleUser(Long userid);
//    get user by email
    UserDto getUserbyEmail(String email);
//    search user

    List<UserDto> searchUsers(String keyword);
//    other specific user
  String exportrept(String reportformat) throws FileNotFoundException, JRException;
}
