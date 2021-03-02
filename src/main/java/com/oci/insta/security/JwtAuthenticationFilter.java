package com.oci.insta.security;

import com.oci.insta.cache.UserTokenCache;
import com.oci.insta.constants.Constants;
import com.oci.insta.entities.models.cache.TokenObject;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.oci.insta.constants.Constants.TOKEN_HEADER;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailService userDetailsService;


    @Autowired
    private UserTokenCache userTokenCache;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        // get the authentication header. Tokens are supposed to be passed in the authentication header
        String header = req.getHeader(TOKEN_HEADER);
        String authToken = null;

        // validate the header and check the prefix. If there's no header,
        // pass to next filter. It may be signup or login request.
        if(header == null) {
            chain.doFilter(req, res);
            return;
        }


        authToken = header;
        logger.debug("checking authentication for user");
        if (authToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.debug("security context was null, so authorizing user");
            TokenObject tokenObject = userTokenCache.getTokenByKey(authToken);
            InstaUserDetails instaUserDetails;
            if(tokenObject==null){
                logger.error("token not found in cache");
                HttpServletResponse httpServletResponse = (HttpServletResponse) res;
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Failed, token invalid");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                throw new ServletException("Auth Failed");
//                return;
            }

            try {
                instaUserDetails = (InstaUserDetails) userDetailsService.loadUserById(tokenObject.getId());
            }catch (Exception e){
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
                return;
            }

            if(!tokenObject.getExpiresAt().before(new Date())){
                List<Pair<Long,String>> roleList = tokenObject.getRoles();
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                String roleStrList = null;

                /* I've put provision of roles also in cache.
                So that if we want to support multiple roles in future, we can do it */

                if(!CollectionUtils.isEmpty(roleList)){
                    authorities = roleList.stream().map(s->new SimpleGrantedAuthority(s.getValue1())).filter(Objects::nonNull).collect(Collectors.toList());
                    roleStrList = roleList.stream().map(s->s.getValue1()).collect(Collectors.joining(","));
                }


                //Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(instaUserDetails, null,authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user with id " + tokenObject.getId() + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                res.setHeader(
                        Constants.ROLES_HEADER, roleStrList);
                res.setHeader(Constants.USER_ID_HEADER, tokenObject.getId().toString());
            }
            else {
                logger.error("token expired, re-login");
                userTokenCache.deleteToken(authToken);
                SecurityContextHolder.clearContext();
                HttpServletResponse httpServletResponse = (HttpServletResponse) res;
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Failed, token expired");
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType("application/json");
                return;
            }
        }
        if(res.isCommitted()){
            throw new ServletException("Auth Failed");
        }
        chain.doFilter(req, res);
    }
}