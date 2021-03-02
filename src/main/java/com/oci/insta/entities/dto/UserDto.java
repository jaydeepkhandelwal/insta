package com.oci.insta.entities.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
    private String extId;
    private String name;
    private String email;
    private  String  password;
    private String phoneNumber;
}
