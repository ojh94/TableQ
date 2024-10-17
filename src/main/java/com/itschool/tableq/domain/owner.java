package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Table(name = "owner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

  /*  @Column(nullable = false, unique = true)
    private String buisness_number;*/

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact_number;

    @Column(nullable = false)
    private Timestamp created_at;

    @Column(nullable = false)
    private Timestamp last_modified_at;

}
