package com.shruteekatech.ecommerce.service.impl;


import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.dtos.UserDto;
import com.shruteekatech.ecommerce.exception.EmailNotFoundException;
import com.shruteekatech.ecommerce.exception.ResourcenotFoundException;
import com.shruteekatech.ecommerce.helper.Pageablemethod;
import com.shruteekatech.ecommerce.model.User;
import com.shruteekatech.ecommerce.repository.UserRepository;
import com.shruteekatech.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In this class we created the multiple Api for User To creating ,updating ,getting the data through the user.
 *
 * @author Isha Rathore
 * @see// deleteUser
 * @see //createUser
 * @see //updateUser
 * @see //getAlluser
 * @see //getByid
 * @since 1-02-2023
 */
@Service
@Slf4j
public class UserServiceimpl implements UserService {


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${user.profile.image.path}")
    private String imagepath;

    @Override
    public UserDto createUser(UserDto userdto) {
        log.info("Initiating dao call to Save the user data");
        User user = this.dtotoentity(userdto);

        User saveuser = this.userRepo.save(user);
        UserDto entitytouser1 = this.entitytouser(saveuser);
        log.info("Completed dao call to Save the user data");
        return entitytouser1;
    }


    @Override
    public UserDto updateUser(UserDto userdto, Long userid) {
        log.info("Initiating dao call to update the user data with userId:{}", userid);
        User user = this.userRepo.findById(userid).orElseThrow(() -> new ResourcenotFoundException(AppConstant.USER_ID, "With user id", userid));

        user.setName(userdto.getName());
        user.setAbout(userdto.getAbout());
        user.setGender(userdto.getGender());
        user.setPassword(userdto.getPassword());
        user.setEmail(userdto.getEmail());
        user.setImageName(userdto.getImageName());

        log.info("Completed dao call to update the user data with userId:{}", userid);

        User updateduser = this.userRepo.save(user);
        return this.modelMapper.map(updateduser, UserDto.class);
    }

    /**
     *
     * @param userid
     */

    @Override
    public void deleteUser(Long userid) {
        log.info("Initiating dao call to delete the user data with userId:{}", userid);

        User user = this.userRepo.findById(userid).orElseThrow(() -> new ResourcenotFoundException(AppConstant.USER_ID, "With id", userid));
//        Delete user profile image
//        abc.png
        String fullpath=imagepath+user.getImageName();
        try{
            Path path= Paths.get(fullpath);
            Files.delete(path);

        } catch (NoSuchFileException e) {
            log.info("User image not found in folder");
            e.printStackTrace();
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
//        delete user
        userRepo.delete(user);

        log.info("Completed dao call to delete the user data with userId:{}", userid);


    }

    /**
     *
     * @param pagenumber
     * @param pagesize
     * @param sortBy
     * @param sortDir
     * @return
     */

    @Override
    public PagableResponse<UserDto> getAllUsers(Integer pagenumber, Integer pagesize, String sortBy, String sortDir) {
        log.info("Initiating dao call to get the All users data ");

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : (Sort.by(sortBy).descending());
        Pageable page = PageRequest.of(pagenumber, pagesize, sort);
        Page<User> pages = this.userRepo.findAll(page);

        /*List<User> users = pages.getContent();
        List<UserDto> userDtoList = users.stream().map((user) -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());

        PagableResponse pagableResponse=new PagableResponse();
        pagableResponse.setContent(userDtoList);
        pagableResponse.setPageNumber(pages.getNumber());
        pagableResponse.setPageSize(pages.getSize());
        pagableResponse.setLastPage(pages.isLast());
        pagableResponse.setTotalPages(pages.getTotalPages());
        pagableResponse.setTotalElement(pages.getTotalElements());*/

        PagableResponse<UserDto> pageableresponse = Pageablemethod.getPageableresponse(pages, UserDto.class);
        log.info("Completed dao call to get the All users data ");
        return pageableresponse;

    }

    @Override
    public UserDto getSingleUser(Long userid) {
        log.info("Initiating dao call to get the single user data with userId:{}", userid);

        User user = this.userRepo.findById(userid).orElseThrow(() -> new ResourcenotFoundException(AppConstant.USER_ID, AppConstant.WITH_ID, userid));
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        log.info("Completed dao call to get the single user data with userId:{}", userid);

        return userDto;
    }

    @Override
    public UserDto getUserbyEmail(String email) {
        log.info("Initiating dao call to get the singleuser data with email:{}", email);

        User withEmail = this.userRepo.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(AppConstant.EMAIL_ID, "With email ", email));
        log.info("Completed dao call to get the singleuser data with email:{}", email);

        return this.modelMapper.map(withEmail, UserDto.class);
    }

    @Override
    public List<UserDto> searchUsers(String keyword) {
        log.info("Initiating dao call to get the users data with keyword:{}", keyword);

        List<User> userList = this.userRepo.findByNameContaining(keyword);
        List<UserDto> userDtos = userList.stream().map((user) -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        log.info("completed dao call to get the users data with keyword:{}", keyword);


        return userDtos;
    }

    private UserDto entitytouser(User user) {

        return UserDto.builder()
                .id(user.getId())
                .about(user.getAbout())
                .password(user.getPassword())
                .email(user.getEmail())
                .name(user.getName())
                .imageName(user.getImageName())
                .gender(user.getGender()).build();
    }

    private User dtotoentity(UserDto userDto) {

        return User.builder().id(userDto.getId())
                .name(userDto.getName())
                .about(userDto.getAbout())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .imageName(userDto.getImageName())
                .password(userDto.getPassword()).build();
    }

   /* @Override
    public String exportrept(String reportFormat) throws FileNotFoundException, JRException {
        log.info("Initiating dao call to Genrate the users report with reportformat:{}", reportFormat);

        String path = "F:\\Bikkadit\\ElectronicStore_Project";
        List<User> userList = this.userRepo.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:User.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Isha Rathore");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\Userdetails.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\Userdetails.pdf");
        }
        if (reportFormat.equalsIgnoreCase("xml")) {
            JasperExportManager.exportReportToXmlFile(jasperPrint,path + "\\Userdetails.xls",true);
        }
        log.info("Completed dao call to Genrate the users report with reportformat:{}", reportFormat);

        return "report generated in path : " + path;

    }
*/
}
