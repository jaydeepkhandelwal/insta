package com.oci.insta.service;

import com.oci.insta.cache.OtpCache;
import com.oci.insta.config.OtpConfig;
import com.oci.insta.downstream.SmsSender;
import com.oci.insta.entities.models.cache.OtpCacheObject;
import com.oci.insta.exception.InstaErrorCode;
import com.oci.insta.exception.InstaException;
import com.oci.insta.repository.UserRepository;
import com.oci.insta.util.GeneralUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@Slf4j
public class OtpService {
    private OtpCache otpCache;
    private SmsSender smsSender;
    private OtpConfig otpConfig;
    private UserRepository userRepository;

    @Autowired
    public OtpService(OtpCache otpCache, SmsSender smsSender, OtpConfig otpConfig, UserRepository userRepository) {
        this.otpCache = otpCache;
        this.smsSender = smsSender;
        this.otpConfig = otpConfig;
        this.userRepository = userRepository;
    }

    public void sendOtp(String phoneNumber) throws InstaException, IOException {
        if(userRepository.findByPhoneNumber(phoneNumber)==null){
            log.error("no user with given phone number");
            throw new InstaException(InstaErrorCode.UNAUTHORISED,"no user with given phone number");
        }

        Integer otp = GeneralUtils.generateOtp();

        log.info("Generated otp: "+otp);
        boolean response = smsSender.send(phoneNumber,otp);
        if(response) {
            OtpCacheObject cacheObject = OtpCacheObject.builder().otp(otp).issuedAt(new Date(System.currentTimeMillis())).
                    expiresAt(new Date(System.currentTimeMillis() + otpConfig.getOtpExpiryMs())).build();
            otpCache.putToken(phoneNumber,cacheObject);
        }
        else {
            throw new InstaException(InstaErrorCode.INTERNAL_SERVER_ERROR,"error in sending otp");
        }
    }

    public boolean verifyOtp(String phoneNumber,Integer otp) throws InstaException {
        OtpCacheObject cacheOtp = otpCache.getTokenByKey(phoneNumber);
        if(cacheOtp == null){
            throw new InstaException(InstaErrorCode.UNAUTHORISED,"Auth failed, Otp invalid");
        }

        Date expiryDate = new Date(cacheOtp.getIssuedAt().getTime() +  otpConfig.getOtpExpiryMs());
        if( expiryDate.before(new Date()) ){
            log.error("otp expired");
            otpCache.deleteToken(phoneNumber);
            throw new InstaException(InstaErrorCode.BAD_REQUEST,"Otp expired");
        }

        if(otp.equals(cacheOtp.getOtp())) {
            otpCache.deleteToken(phoneNumber);
            return true;
        }
        else {
            throw new InstaException(InstaErrorCode.UNAUTHORISED,"otp does not match");
        }
    }
}
