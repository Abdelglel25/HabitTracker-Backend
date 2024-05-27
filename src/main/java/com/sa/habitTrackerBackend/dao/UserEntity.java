package com.sa.habitTrackerBackend.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sa.habitTrackerBackend.utils.enums.RolesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
@ToString(exclude = {"password"})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RolesEnum role = RolesEnum.USER;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate = new Date();

    @Column(name = "last_login_Date", nullable = false)
    private Date lastLoginDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "is_suspended", nullable = false)
    private Boolean isSuspended = false;
}