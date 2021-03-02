package com.oci.insta.util;

import com.oci.insta.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.oci.insta.constants.Constants.*;

@Slf4j
public class JwtTokenUtil implements Serializable {



    public static String generateToken(Long id,List<Pair<Long,String>> roleList) {
        return doGenerateToken(String.valueOf(id),roleList);
    }

    private static String doGenerateToken(String subject,List<Pair<Long,String>> roleList) {

        Claims claims = Jwts.claims().setSubject(subject);
        if(!CollectionUtils.isEmpty(roleList))
        claims.put("auth", roleList.stream().map(s->new SimpleGrantedAuthority(s.getValue1())).filter(Objects::nonNull).collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(SecurityContextHolder.getContext().getAuthentication().getName())  //who should be token issuer?
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }
}
