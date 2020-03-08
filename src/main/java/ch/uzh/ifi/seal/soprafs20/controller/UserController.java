package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserSingleGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.hibernate.type.TrueFalseType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserSingleGetDTO getUserByUserName(@PathVariable Long userId){
        User user = userService.getByUserId(userId);
        UserSingleGetDTO userSingleGetDTO = DTOMapper.INSTANCE.convertSingleEntityToUserGetDTO(user);
        return userSingleGetDTO;
    }


    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserPutDTO loginUser(@RequestBody UserPostDTO userPostDTO){
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User user = userService.getByUserName(userInput.getUsername());
        UserPutDTO userPutDTO = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(user);
        if(user.getPassword().equals(userInput.getPassword())){
            userPutDTO.setLoginCheckStatus(true);
            User tempUser = user;
            tempUser.setStatus(UserStatus.ONLINE);
            userService.saveOrUpdate(tempUser);

        }
        else{
            userPutDTO.setLoginCheckStatus(false);
        }

        return userPutDTO;

    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        userInput.setCreationDate(new java.sql.Date(Calendar.getInstance().getTimeInMillis()));

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PutMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserPostDTO updateUser(@RequestBody UserPostDTO userPostDTO, @PathVariable Long userId){
        User user = userService.getByUserId(userId);
        if(!userPostDTO.getUsername().isEmpty()){
            user.setUsername(userPostDTO.getUsername());
        }
        if(null!=userPostDTO.getBirthDate()) {
            user.setBirthDate(userPostDTO.getBirthDate());
        }
        userService.saveOrUpdate(user);
        return userPostDTO;
    }

    @PutMapping("/logout/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logOutUser(@PathVariable Long userId){
        User user = userService.getByUserId(userId);
        user.setStatus(UserStatus.OFFLINE);
        userService.saveOrUpdate(user);

    }



}
