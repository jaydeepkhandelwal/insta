package com.oci.insta.repository;

import com.oci.insta.entities.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByIdIn(List<Long> id);

    User findByName(String username);
    User findByPhoneNumber(String phoneNumber);
    User findByEmail(String email);
}
