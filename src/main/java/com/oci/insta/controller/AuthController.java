package com.oci.insta.controller;

import com.oci.insta.entities.dto.LoginRequest;
import com.oci.insta.entities.dto.UserDto;
import com.oci.insta.entities.models.User;
import com.oci.insta.exception.InstaException;
import com.oci.insta.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("insta/api/v1/auth")
@Slf4j
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public UserDto login(HttpServletResponse httpServletResponse,
                                           @RequestBody LoginRequest loginRequest) throws InstaException {
         User user = authService.signin(httpServletResponse, loginRequest);
         return new UserDto().setExtId(user.getExtId());
    }

    @PostMapping(value = "/signup")
    public UserDto createUser(@Valid @RequestBody UserDto userDto) throws InstaException {

        User user = authService.createUser(new User(userDto));
        return new UserDto().setExtId(user.getExtId());
    }

    @GetMapping(value = "/otp/{phoneNumber}")
    public void generateOtp(@NotNull @PathVariable("phoneNumber") String phoneNumber) throws IOException, InstaException {
        authService.sendOtp(phoneNumber);
    }







}

