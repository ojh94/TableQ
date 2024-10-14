package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "users")
@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String phone_Number;

    @Column(updatable = false)
    private Timestamp created_at;

    @Column
    private Timestamp last_login_at;

    @Column
    private String address;

    @Column(nullable = false)
    private String name;

    @Column
    private String social_type;

    @Column
    private String social_id;

    @Column(nullable = false, unique = true)
    private String email;


    public User(long id, String email, String password, String nickName, String phoneNumber, Timestamp timestamp, Timestamp timestamp1
            , String address, String name, String socialType, String socialId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phone_Number = phoneNumber;
        this.created_at = timestamp;
        this.last_login_at = timestamp1;
        this.address = address;
        this.name = name;
        this.social_type = socialType;
        this.social_id = socialId;
    }
}
