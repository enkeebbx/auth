package com.ably.auth.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user")
@SuperBuilder
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String phone;
}
