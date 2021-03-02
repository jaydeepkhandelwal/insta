package com.oci.insta.security;

import com.oci.insta.entities.models.User;
import com.oci.insta.exception.InstaErrorCode;
import com.oci.insta.exception.InstaException;
import com.oci.insta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public MyUserDetailService(){
        super();
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByName(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return InstaUserDetails.create(user);
    }

    public UserDetails loadUserById(Long id) throws InstaException {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            throw new InstaException(InstaErrorCode.NOT_FOUND,"user with given username does not exist");
        }
        return InstaUserDetails.create(user);
    }
}
