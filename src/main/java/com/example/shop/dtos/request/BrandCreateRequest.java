package com.example.shop.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BrandCreateRequest {
    private String name;
    private String country;
    private List<MultipartFile> images = new ArrayList<>();
}
