package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    private String country;
    private String city;
    private String street;
    private String zipCode;
    private boolean primary;
}
