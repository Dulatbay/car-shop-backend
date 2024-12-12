package com.example.shop.mappers;

import com.example.shop.dtos.request.ReviewCreateRequest;
import com.example.shop.dtos.response.ReviewResponse;
import com.example.shop.entities.Car;
import com.example.shop.entities.Review;
import com.example.shop.exceptions.DbNotFoundException;
import com.example.shop.repositories.CarRepository;
import com.example.shop.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Mapper(componentModel = "spring")
public abstract class ReviewMapper {
    @Autowired
    private FileService fileService;
    @Autowired
    private CarRepository carRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrls", qualifiedByName = "saveAndReturnUrl", source = "images")
    public abstract Review toEntity(ReviewCreateRequest reviewCreateRequest);

    @Mapping(target = "author", expression = "java(review.getAuthor().getUsername())")
    @Mapping(target = "authorId", expression = "java(review.getAuthor().getId())")
    @Mapping(target = "car", expression = "java(review.getCar().getModelName())")
    @Mapping(target = "carId", expression = "java(review.getCar().getId())")
    public abstract ReviewResponse toResponse(Review review);

    @Named("saveAndReturnUrl")
    protected List<String> saveAndReturnUrl(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        files.forEach(file -> {
            urls.add(fileService.save(file));
        });

        return urls;
    }
}
