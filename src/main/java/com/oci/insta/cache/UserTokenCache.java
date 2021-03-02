package com.oci.insta.cache;

import com.oci.insta.entities.models.cache.TokenObject;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.oci.insta.constants.Constants.TOKEN_CACHE_MAP;


@Component
public class UserTokenCache extends BaseImpl<String, TokenObject>{

    private RedisClient redisClient;

    @Autowired
    public UserTokenCache(RedisClient redisClient) {
        this.redisClient = redisClient;
        super.map = getMap();

    }

    public RMap<String, TokenObject> getMap(){
        return redisClient.getRedissonClient().getMap(TOKEN_CACHE_MAP);
    }
}