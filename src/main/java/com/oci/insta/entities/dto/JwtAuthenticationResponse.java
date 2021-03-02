package com.oci.insta.entities.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class JwtAuthenticationResponse implements Serializable {
    private boolean status;
   // private String token;
}
