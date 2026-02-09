package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class TestEntity {

    @Id
    @GeneratedValue
    private Long id;
}