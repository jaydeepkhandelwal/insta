package com.oci.insta.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "config.otp-config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpConfig {
    private int otpExpiryMs;
}
