package com.shruteekatech.ecommerce.servicetest;

import com.shruteekatech.ecommerce.BaseTest;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.dtos.UserDto;
import com.shruteekatech.ecommerce.exception.EmailNotFoundException;
import com.shruteekatech.ecommerce.exception.ResourcenotFoundException;
import com.shruteekatech.ecommerce.model.User;
import com.shruteekatech.ecommerce.repository.UserRepository;
import com.shruteekatech.ecommerce.service.UserService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;




public class UserServiceImplTest extends BaseTest {

    @MockBean
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;


    User user,user1,user2;
    List<User> users;
    UserDto userDto;

    @BeforeEach
    public  void init()
    {
        user= User.builder()
                .name("Isha")
                .about("Software Enginner")
                .email("isha@gmil.com").gender("Female")
                .imageName("abc.png").password("1234@12").build();
        user1= User.builder()
                .name("Ashu")
                .about("Software Enginner")
                .email("Ashu@gmil.com").gender("Female")
                .imageName("abc.png").password("1234@12").build();
        user2= User.builder()
                .name("Amruta")
                .about("Software Enginner")
                .email("Amruta@gmil.com").gender("Female")
                .imageName("abc.png").password("1234@12").build();
        userDto = UserDto.builder().about("My name").name("Isha Rathore")
                .imageName("xyz.png")
                .email("abc@gmail.com")
                .password("123@3isa").build();

        users=new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);


    }

    @Test
    public void createuserTest()
    {
//       Arrange
        Mockito.when(repository.save(Mockito.<User>any())).thenReturn(user);
//        Act
        UserDto user1 = userService.createUser(modelMapper.map(user, UserDto.class));
        //System.out.println(user1.getName());
//        Assert
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Isha",user1.getName());
//        verify(repository).save(Mockito.<User>any());
    }

    @Test
    public void updateuserTest()
    {
        Long id=1l;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);
        UserDto updateUser = userService.updateUser(userDto, id);
        //UserDto updateUser = modelMapper.map(user, UserDto.class);
        System.out.println(updateUser.getName());
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getName(),updateUser.getName(),"Name is not valid ");

    }
    @Test
    public  void deleteUserTest()
    {
       Long id=1l;
       Mockito.when(repository.findById(id)).thenReturn(Optional.of(user));
       userService.deleteUser(id);
       Mockito.verify(repository,Mockito.times(1)).delete(user);

    }

    @Test
    public void getallUserTest()
    {
          List<User> userList= Arrays.asList(user,user1,user2);
        Page page=new PageImpl(userList);
        Mockito.when(repository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PagableResponse allUsers = userService.getAllUsers(1, 5, "name", "asc");
        Assertions.assertEquals(3,allUsers.getContent().size());


    }
    @Test
    public void getSingleUserTest()
    {
        Long id=1l;
//        Arrange
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(user));
//        Act
        UserDto singleUser = userService.getSingleUser(id);

//        Assert
        Assertions.assertEquals(user.getName(),singleUser.getName());
//       Negative scenerio
        Assertions.assertThrows( ResourcenotFoundException.class,()-> userService.getSingleUser(2l));
    }

    @Test
    public void getUserbyEmailTest()
    {
        String email="isha@gmail.com";
//        Arrange
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));
//        Act
        UserDto userbyEmail = userService.getUserbyEmail(email);

//        Assert
        Assertions.assertEquals(user.getEmail(),userbyEmail.getEmail());
        Assertions.assertThrows(EmailNotFoundException.class,()->userService.getUserbyEmail("abc123@gmail.com"));

    }

    @Test
    public void searchUsersTest()
    {
        String keyword="isha";
        Mockito.when(repository.findByNameContaining(keyword)).thenReturn(users);

        List<UserDto> userDtos = userService.searchUsers(keyword);
        Assertions.assertEquals(3,userDtos.size(),"Size not matched !!");

    }


}
