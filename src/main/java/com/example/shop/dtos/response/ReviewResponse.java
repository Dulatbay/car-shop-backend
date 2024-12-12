package com.example.shop.dtos.response;

import com.example.shop.entities.Car;
import com.example.shop.entities.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Getter
@Setter
public class ReviewResponse {
    private String id;
    private String content;
    private double rating;
    private String author;
    private String authorId;
    private String car;
    private String carId;
    private List<String> imageUrls;
}
