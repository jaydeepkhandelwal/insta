package com.oci.insta.controller;

import com.oci.insta.entities.dto.JwtAuthenticationResponse;
import com.oci.insta.entities.dto.LoginRequest;
import com.oci.insta.entities.dto.UserDto;
import com.oci.insta.entities.models.User;
import com.oci.insta.exception.InstaException;
import com.oci.insta.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("insta/api/v1/auth")
@Slf4j
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public UserDto login(HttpServletResponse httpServletResponse,
                                           @RequestBody LoginRequest loginRequest) throws InstaException {
         User user = userService.signin(httpServletResponse, loginRequest);
         return new UserDto().setExtId(user.getExtId());
    }

    @PostMapping(value = "/signup")
    public UserDto createUser(@Valid @RequestBody UserDto userDto) throws InstaException {

        User user = userService.createUser(new User(userDto));
        return new UserDto().setExtId(user.getExtId());
    }






}

