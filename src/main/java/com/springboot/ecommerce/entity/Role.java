package com.springboot.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

/*
*       id : name
*       ---------
*       1  : Admin
*       2  : User
* */

@Entity
@Data
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
