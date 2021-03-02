package com.oci.insta.entities.models.cache;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class OtpCacheObject implements Serializable {
    private Integer otp;
    @NotNull
    private Date issuedAt;
    @NotNull Date expiresAt;
    private static final long serialVersionUID = -1L;
}
