package com.oci.insta.service;

import com.oci.insta.cache.OtpCache;
import com.oci.insta.cache.UserTokenCache;
import com.oci.insta.config.OtpConfig;
import com.oci.insta.constants.Constants;
import com.oci.insta.downstream.SmsSender;
import com.oci.insta.entities.dto.LoginRequest;
import com.oci.insta.entities.models.User;
import com.oci.insta.entities.models.cache.OtpCacheObject;
import com.oci.insta.entities.models.cache.TokenObject;
import com.oci.insta.exception.InstaErrorCode;
import com.oci.insta.exception.InstaException;
import com.oci.insta.repository.UserRepository;
import com.oci.insta.util.GeneralUtils;
import com.oci.insta.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private OtpCache otpCache;

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private OtpConfig otpConfig;

    @Autowired
    private UserTokenCache userTokenCache;
    @Autowired
    private PasswordEncoder passwordEncoder;




    public User signin(HttpServletResponse httpServletResponse,
                                            LoginRequest loginRequest) throws InstaException {
        User user;
        if(loginRequest.getUsername()!=null) {
            try {
                final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                user = userRepository.findByName(loginRequest.getUsername());

            } catch (AuthenticationException e) {
                throw new InstaException(InstaErrorCode.UNAUTHORISED, "Invalid Username/Password");
            }
        }
        else{
            if(!verifyOtp(loginRequest.getPhoneNumber(),loginRequest.getOtp())){
                log.error("otp invalid");
                throw new InstaException(InstaErrorCode.UNAUTHORISED,"Otp is invalid");
            }
            user = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber());
        }


        String authToken = JwtTokenUtil.generateToken(user.getId(), null);
        TokenObject tokenObject = TokenObject.builder().id(user.getId()).roles(null).token(authToken).issuedAt(new Date(System.currentTimeMillis())).
                expiresAt(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS*1000)).build();
        userTokenCache.putToken(authToken,tokenObject);
        setHeaders(httpServletResponse, tokenObject);
        return user;
        //return JwtAuthenticationResponse.builder().token(authToken).build();
    }

    /* This function will create a new User. Here, I am keeping phoneNo as unique identifier for users.
     */
    @Transactional
    public User createUser(User user) throws InstaException {

        if(userRepository.findByPhoneNumber(user.getPhoneNumber())!=null){
            log.error("user with given phone number already exists");
            throw new InstaException(InstaErrorCode.BAD_REQUEST, "user with given phone number already exists");
        }

        User newUser = new User().
                 setEmail(user.getEmail())
                .setName(user.getName())
                .setPhoneNumber(user.getPhoneNumber());

        if(user.getName()!=null) {
            if(user.getPassword()==null){
                throw new InstaException(InstaErrorCode.BAD_REQUEST,"Password can not be null if username is provided");
            }
            else {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                newUser.setPassword(encodedPassword);
            }
        }
        if(!GeneralUtils.isValidMobile(user.getPhoneNumber())) {
            throw new InstaException(InstaErrorCode.BAD_REQUEST, "not a valid mobile number");

        }

        newUser.setDeleted(Boolean.FALSE);
        newUser.setCreatedBy(user.getPhoneNumber());
        newUser.setLastUpdatedBy(user.getPhoneNumber());
        newUser.setExtId(UUID.randomUUID().toString());
        return userRepository.save(newUser);

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

    private void setHeaders(HttpServletResponse httpServletResponse, TokenObject tokenObject) {
        //httpServletResponse.setHeader(Constants.USER_ID_HEADER, tokenObject.getId().toString());
        httpServletResponse.setHeader(Constants.TOKEN_HEADER, tokenObject.getToken());
    }
}
