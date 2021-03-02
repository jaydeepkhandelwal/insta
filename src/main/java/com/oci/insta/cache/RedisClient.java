package com.oci.insta.cache;

import com.oci.insta.config.RedisConfig;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class RedisClient {
    private RedissonClient redissonClient;
    private RedisConfig redisConfig;

    @Autowired
    public RedisClient(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
        String address = String.format("%s:%s", redisConfig.getHost(), redisConfig.getPort());
        Config config = new Config();
        config.useSingleServer().setAddress(address).setDatabase(redisConfig.getDb());
        this.redissonClient = Redisson.create(config);
    }
}

