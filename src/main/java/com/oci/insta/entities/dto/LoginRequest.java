package com.oci.insta.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String username;
    private  String password;
    private String phoneNumber;
    private Integer otp;
}

