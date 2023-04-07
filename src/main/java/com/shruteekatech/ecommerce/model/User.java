package com.shruteekatech.ecommerce.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_name")
    private String name;
    @Column(name="user_email",unique = true)
    private String email;
    @Column(name="user_password")
    private String password;

    private String gender;

    @Column()
    private String about;

    @Column(name="user_image_name")
    private String imageName;



}
