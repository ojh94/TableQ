package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.domain.enumclass.MemberRole;
import com.itschool.tableq.network.request.UserRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User extends AuditableEntity<UserRequest> implements UserDetails {

    private static final long serialVersionUID = 1L; // 고정된 값으로 선언

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

    private LocalDateTime lastLoginAt;

    private String address;

    @Column(nullable = false)
    private String name;

    private String socialType;

    private String socialId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public void update(UserRequest userRequest) {
        this.password = userRequest.getPassword() == null? this.password : userRequest.getPassword();
        this.nickname = userRequest.getNickname() == null? this.nickname : userRequest.getNickname();
        this.phoneNumber = userRequest.getPhoneNumber() == null? this.phoneNumber : userRequest.getPhoneNumber();
        this.address = userRequest.getAddress() == null? this.address : userRequest.getAddress();
        this.name = userRequest.getName() == null? this.name : userRequest.getName();
        this.email = userRequest.getEmail() == null? this.email : userRequest.getEmail();
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(memberRole.getRole()));
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

    public void setPassword(String password) {
        this.password = password;
    }
}
