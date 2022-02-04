package com.techomeck.test.Library.booking.system.controller;

import com.techomeck.test.Library.booking.system.dto.UserDto;
import com.techomeck.test.Library.booking.system.entity.User;
import com.techomeck.test.Library.booking.system.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private UserService userService;
    private static final ModelMapper modelMapper = new ModelMapper();


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUsers")
    public List<UserDto> getUsers() {

        List<User> users = userService.getUsers();
        if (!users.isEmpty()) {
            return users.stream().map(item -> modelMapper.map(item, UserDto.class)).collect(Collectors.toList());
        }
        return null;
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
        userService.addUser(modelMapper.map(userDto, User.class));
        return new ResponseEntity<String>("User Saved Successfully", HttpStatus.OK);
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") int id) {
        boolean response = userService.deleteUser(id);
        if (response) {
            return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Deletion failed. Please check ID", HttpStatus.BAD_REQUEST);
    }

}
