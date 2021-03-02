package com.oci.insta.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "config.redis-config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisConfig {
    private String host;
    private String port;
    private int db;
    private Boolean testOnBorrow;
    private int maxTotal;
}
