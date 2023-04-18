package com.shruteekatech.ecommerce.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shruteekatech.ecommerce.BaseTest;
import com.shruteekatech.ecommerce.controller.UserController;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
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
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {
    @MockBean
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    User user,user1,user2;

    UserDto userDto,userDto1,userDto2;

    List<UserDto> userDtos;



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
                .email("isha@gmail.com")
                .password("123Isha@3isa").build();

        userDto1 = UserDto.builder().about("My name").name("Isha Rathore")
                .imageName("xyz.png")
                .email("Abc@gmail.com")
                .password("123Isha@3isa").build();
        userDto2 = UserDto.builder().about("My name").name("Isha Rathore")
                .imageName("xyz.png")
                .email("Abc@gmail.com")
                .password("123Isha@3isa").build();

        /*users=new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);*/

        userDtos=new ArrayList<>();
        userDtos.add(userDto);
        userDtos.add(userDto1);
        userDtos.add(userDto2);



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

    @Test
    public void updateUserTest() throws Exception {
        UserDto dto = modelMapper.map(user, UserDto.class);
        Long id=1l;
        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyLong())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/Api/Users/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(converobjectTojsonString(user)).
                accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void getAllUserTest() throws Exception {
        PagableResponse<UserDto> pagableResponse=new PagableResponse<>();
        pagableResponse.setContent(userDtos);
        pagableResponse.setPageNumber(1);
        pagableResponse.setPageSize(3);
        pagableResponse.setTotalPages(2);

        Mockito.when(userService.getAllUsers(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pagableResponse);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Api/Users/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getUserTest() throws Exception {
        Long userid=2l;
        Mockito.when(userService.getSingleUser(Mockito.anyLong())).thenReturn(userDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Api/Users/getbyid/"+userid)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

    }
    @Test
    public void getUserbyemailTest() throws Exception {
        String email="isha@gmail.com";
        Mockito.when(userService.getUserbyEmail(Mockito.anyString())).thenReturn(userDto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Api/Users/byemail/"+email)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());


    }
    @Test
    public void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(Mockito.<Long>any());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/Api/Users/delete/"+1l)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }
    @Test
    public void searchuserTest() throws Exception {
        Mockito.when(userService.searchUsers(Mockito.anyString())).thenReturn(userDtos);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/Api/Users/search/"+"isha")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

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
