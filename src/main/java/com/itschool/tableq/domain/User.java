package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastLoginAt;

    @Column
    private String address;

    @Column(nullable = false)
    private String name;

    @Column
    private String socialType;

    @Column
    private String socialId;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder
    public User(long id, String email, String password, String nickName, String phone_Number, LocalDateTime created_at, LocalDateTime last_login_at
            , String address, String name, String socialType, String socialId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickName;
        this.phoneNumber = phone_Number;
        this.createdAt = created_at;
        this.lastLoginAt = last_login_at;
        this.address = address;
        this.name = name;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", nickName='" + nickname + '\'' +
                ", phone_Number='" + phoneNumber + '\'' +
                ", created_at=" + createdAt +
                ", last_login_at=" + lastLoginAt +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", social_type='" + socialType + '\'' +
                ", social_id='" + socialId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
