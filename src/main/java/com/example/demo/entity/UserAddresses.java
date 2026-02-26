package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "user_addresses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "zip_code"})
)
public class UserAddresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String country;
    private String city;
    private String street;
    @Column(name = "zip_code")
    private String zipCode;
    private Boolean primaryAddress;
}
