package com.oci.insta.cache;

import com.oci.insta.constants.Constants;
import com.oci.insta.entities.models.cache.OtpCacheObject;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OtpCache extends BaseImpl<String, OtpCacheObject> {
    private RedisClient redisClient;

    @Autowired
    public OtpCache(RedisClient redisClient) {
        this.redisClient = redisClient;
        super.map = getMap();
    }

    public RMap<String, OtpCacheObject> getMap(){
        return redisClient.getRedissonClient()
                .getMap(Constants.OTP_CACHE_MAP);
    }
}
