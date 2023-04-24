package com.shruteekatech.ecommerce.controller;


import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.constant.ValidationConstant;
import com.shruteekatech.ecommerce.dtos.ImageResponse;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.dtos.UserDto;
import com.shruteekatech.ecommerce.service.FileService;
import com.shruteekatech.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Isha Rathore
 * In this class we created the multiple Api for User To creating ,updating ,getting the data through the user.
 * @see //CreateUser
 * @see //updateUser
 * @see //deleteUser
 * @see //getAlluser
 * @see //getByid
 * @see //getByemail
 * @see //searchUser
 */


@RestController
@RequestMapping("/Api/Users")
@Slf4j
public class UserController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @Value("${user.profile.image.path}")
    private String imageuploadpath;

    /**
     * This method save the user details .
     * {@code 201 CREATED}.
     *
     * @return UserDto return all details of user from userDto class
     * @see //createuser method using  {@link @PostMapping}
     */
    //    create
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Initiated request to save the user details ");
        UserDto user = this.userService.createUser(userDto);
        log.info("Completed request to save the user details");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
// getAll

    /**
     * This method get all  users details .with Sorting and pagination.
     *
     * @param pagenumber
     * @param pagesize
     * @param sortBy
     * @param sortDir
     * @return Pagableresponse
     * @return UserDto return all details of user from userDto class
     */
    @GetMapping("/")
    public ResponseEntity<PagableResponse> getAllUser(
            @RequestParam(value = "pagenumber", defaultValue = ValidationConstant.PAGE_NUMBER, required = false) Integer pagenumber,
            @RequestParam(value = "pagesize", defaultValue = ValidationConstant.PAGE_SIZE, required = false) Integer pagesize,
            @RequestParam(value = "sortBy", defaultValue = ValidationConstant.SORT_BY_USER, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ValidationConstant.SORT_DIR, required = false) String sortDir) {
        log.info("Initiated request to get all the user details");
        PagableResponse allUsers = this.userService.getAllUsers(pagenumber, pagesize, sortBy, sortDir);
        log.info("Completed request to get all the user details");
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    /**
     * This method get the singleuser details by userid .
     *
     * @param userId @pathvaribale takes the parameter as userid.
     * @return ResponseEntity<UserDto> userdto single data of user.
     */
    @GetMapping("/getbyid/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        log.info("Initiated request to get the single user with UserId :{} ", userId);
        UserDto singleUser = this.userService.getSingleUser(userId);
        log.info("Completed request to get the single user with UserId:{}", userId);
        return new ResponseEntity<>(singleUser, HttpStatus.OK);
    }

    /**
     * This method Update the user details .
     *
     * @param user   it takes the parameter from userDto
     * @param userid @pathvaribale takes the parameter as Integer userid
     * @return UserDto return all details of user from userDto class
     * @see //updateUser method using  {@link @PutMapping}
     */
    @PutMapping("/{userid}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user, @PathVariable Long userid) {
        log.info("Initiated request for update the user with userId :{} ", userid);
        UserDto userDto = this.userService.updateUser(user, userid);
        log.info("Completed request for update the user with userId :{} ", userid);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    /**
     * This method delele the user details by userid .
     * {@code 200 OK}
     *
     * @param userid @pathvaribale takes the parameter as userid
     * @return response as string ,User deleted Succesfully
     */
    @DeleteMapping("/delete/{userid}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userid) {
        log.info("Initiated request for delete the user with userId :{} ", userid);
        this.userService.deleteUser(userid);
        log.info("completed request for delete the user with userId :{} ", userid);
        return new ResponseEntity<>(AppConstant.DELETE, HttpStatus.OK);
    }

    /**
     * This method get the singleuser details by emailid .
     *
     * @param email @pathvaribale takes the parameter as string  email.
     * @return ResponseEntity<UserDto> userdto single data of user.
     */
    @GetMapping("/byemail/{email}")
    public ResponseEntity<UserDto> getUserbyemail(@PathVariable String email) {
        log.info("Initiated request to get all the user with email :{} ", email);
        UserDto userbyEmail = this.userService.getUserbyEmail(email);
        log.info("completed request to get all the user with email :{} ", email);
        return new ResponseEntity<>(userbyEmail, HttpStatus.OK);
    }

    /**
     * This method get all the user with the help of searching keyword  .
     *
     * @param keyword @pathvaribale takes the parameter as string  keyword.
     * @return ResponseEntity<List < UserDto>> userdto All data of user.
     */

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchuser(@PathVariable String keyword) {
        log.info("Initiated request to get all the user with keyword :{} ", keyword);

        List<UserDto> userDtos = this.userService.searchUsers(keyword);

        log.info("Completed request to get all the user with keyword :{} ", keyword);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }


    @PostMapping("/image/{userid}/")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable Long userid, @RequestParam("userimage") MultipartFile file) throws IOException {
        log.info("Upload the image with userid:{}", userid);
        UserDto user = this.userService.getSingleUser(userid);

        String uploadImage = this.fileService.uploadImage(file, imageuploadpath);

        user.setImageName(uploadImage);
        UserDto userDto = this.userService.updateUser(user, userid);

        ImageResponse imageResponse = ImageResponse.builder().imagename(uploadImage).message("Image Uploaded").status(true).build();
        log.info("Completed the upload image process", userid);
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    //    To serve the user image
    @GetMapping("/image/{userId}")
    public void serveUserimage(@PathVariable Long userId, HttpServletResponse response) throws IOException {
        log.info("initiated request to serve image with userid:{}", userId);
        UserDto user = this.userService.getSingleUser(userId);
        InputStream resource = this.fileService.getResource(imageuploadpath, user.getImageName());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Completed request to serve image with userid:{}", userId);
    }

    @GetMapping("/report/{format}")
    public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException, JRException, FileNotFoundException {
        log.info("initiated request to genrate the reports  with Format:{}", format);
        log.info("completed request to genrate the reports  with Format:{}", format);
        return this.userService.exportrept(format);
    }
}
