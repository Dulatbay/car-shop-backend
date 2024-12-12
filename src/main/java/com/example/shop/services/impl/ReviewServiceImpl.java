package com.example.shop.services.impl;

import com.example.shop.dtos.params.PaginationParams;
import com.example.shop.dtos.request.ReviewCreateRequest;
import com.example.shop.dtos.response.ReviewResponse;
import com.example.shop.entities.User;
import com.example.shop.exceptions.DbNotFoundException;
import com.example.shop.mappers.ReviewMapper;
import com.example.shop.repositories.CarRepository;
import com.example.shop.repositories.ReviewRepository;
import com.example.shop.services.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final CarRepository carRepository;

    @Override
    @Transactional
    public void create(User author, ReviewCreateRequest reviewCreateRequest) {
        var car = carRepository.findByIdAndDeletedIsFalse(reviewCreateRequest.getCarId())
                .orElseThrow(() -> new DbNotFoundException("Car Not Found", HttpStatus.BAD_REQUEST.getReasonPhrase()));
        reviewCreateRequest.setAuthor(author);
        reviewCreateRequest.setCar(car);

        var toSave = reviewMapper.toEntity(reviewCreateRequest);
        reviewRepository.save(toSave);

        car.setRating(reviewRepository.findAverageRatingByCarId(car.getId()));
        carRepository.save(car);
    }

    @Override
    public void delete(User author, String reviewId) {
        var reviewToDelete = reviewRepository.findByIdAndDeletedIsFalse(reviewId)
                .orElseThrow(() -> new DbNotFoundException("Review not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (!reviewToDelete.getAuthor().getId().equals(author.getId()) && !author.getRole().toString().equals("ADMIN"))
            throw new MethodNotAllowedException("You are not allowed to delete this review", Collections.singleton(HttpMethod.DELETE));

        reviewToDelete.setDeleted(true);
        reviewRepository.save(reviewToDelete);
    }

    @Override
    public Page<ReviewResponse> getByCar(String carId, PaginationParams paginationParams) {
        var car = carRepository.findByIdAndDeletedIsFalse(carId)
                .orElseThrow(() -> new DbNotFoundException("Car not found", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        var reviews = reviewRepository.findByCar_IdAndDeletedIsFalse(carId, PageRequest.of(paginationParams.getPage(), paginationParams.getSize()));

        return reviews
                .map(reviewMapper::toResponse);
    }

}
