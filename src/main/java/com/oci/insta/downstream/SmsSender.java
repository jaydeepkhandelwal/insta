package com.oci.insta.downstream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class SmsSender {



    public boolean send(String authKey,
                        String phoneNumber,
                        Integer otp,
                        Integer otpExpiryMs,
                        Integer otpLen,
                        String senderId)  {
      return true;
    }
}
