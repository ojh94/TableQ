package com.itschool.tableq.domain;

import com.itschool.tableq.network.request.UserRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime lastLoginAt;

    private String address;

    @Column(nullable = false)
    private String name;

    private String socialType;

    private String socialId;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder
    public User(Long id, String email, String password, String nickname, String phoneNumber, LocalDateTime createdAt, LocalDateTime lastLoginAt,
                String address, String name, String socialType, String socialId) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.address = address;
        this.name = name;
        this.socialType = socialType;
        this.socialId = socialId;
        this.email = email;
    }

    public void update(UserRequest dto) {
        this.password = dto.getPassword() == null? this.password : dto.getPassword();
        this.nickname = dto.getNickname() == null? this.nickname : dto.getNickname();
        this.phoneNumber = dto.getPhoneNumber() == null? this.phoneNumber : dto.getPhoneNumber();
        this.address = dto.getAddress() == null? this.address : dto.getAddress();
        this.name = dto.getName() == null? this.name : dto.getName();
        this.email = dto.getEmail() == null? this.email : dto.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return this.email;
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
}
