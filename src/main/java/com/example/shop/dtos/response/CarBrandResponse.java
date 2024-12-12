package com.example.shop.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CarBrandResponse {
    private String id;
    private String name;
    private String country;
    private List<String> imageUrls;
}
