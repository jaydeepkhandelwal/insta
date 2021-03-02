package com.oci.insta.entities.models;

import com.oci.insta.entities.dto.UserDto;
import com.oci.insta.entities.models.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Where(clause = "deleted!=true")
public class User extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "ext_id")
    private String extId;

    public User(UserDto userDto) {
        this.name = userDto.getName();
        this.password = userDto.getPassword();
        this.email = userDto.getEmail();
        this.phoneNumber = userDto.getPhoneNumber();
    }

    public User(Long id) {
        this.id = id;
    }

}