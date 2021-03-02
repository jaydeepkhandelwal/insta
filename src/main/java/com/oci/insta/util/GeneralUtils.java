package com.oci.insta.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtils {
    private final static Pattern pattern = Pattern.compile("^\\d{10}$");

    public static Integer generateOtp(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return otp;
    }

    public static boolean isValidMobile(String mobileNo) {
        Matcher matcher = pattern.matcher(mobileNo);
        return matcher.matches();
    }
}
