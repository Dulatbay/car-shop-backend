package com.example.shop.dtos.request;

import com.example.shop.entities.Car;
import com.example.shop.entities.Review;
import com.example.shop.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReviewCreateRequest {
    @NotBlank
    private String content;

    @Min(0)
    @Max(5)
    private double rating;

    @NotBlank
    private String carId;

    private List<MultipartFile> images = new ArrayList<>();

    @JsonIgnore
    private User author;

    @JsonIgnore
    private Car car;
}
