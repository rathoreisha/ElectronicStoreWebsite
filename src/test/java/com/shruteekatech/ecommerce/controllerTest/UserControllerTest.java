package com.shruteekatech.ecommerce.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shruteekatech.ecommerce.BaseTest;
import com.shruteekatech.ecommerce.controller.UserController;
import com.shruteekatech.ecommerce.dtos.UserDto;
import com.shruteekatech.ecommerce.model.User;
import com.shruteekatech.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {
    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    User user,user1,user2;

    UserDto userDto;

    @BeforeEach
    public void init()
    {
        user= User.builder()
                .name("Isha")
                .about("Software Enginner")
                .email("isha@gmail.com").gender("Female")
                .imageName("abc.png").password("Isha@12234").build();
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

        /*users=new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);*/



    }

    @Test
    public void createUserTest() throws Exception {

//        users+post+json user data as json
//        json +status created

        UserDto dto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);
//        Actual request for url

        this.mockMvc.perform(MockMvcRequestBuilders.post("/Api/Users/create")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(converobjectTojsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").exists());


    }

    private String converobjectTojsonString(User user) {
        try
        {
           return new ObjectMapper().writeValueAsString(user);
        } catch (RuntimeException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
