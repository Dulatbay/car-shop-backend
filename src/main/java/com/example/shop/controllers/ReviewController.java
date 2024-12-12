package com.example.shop.controllers;

import com.example.shop.constants.Utils;
import com.example.shop.dtos.request.ReviewCreateRequest;
import com.example.shop.entities.Review;
import com.example.shop.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createReview(@ModelAttribute
                                             @Valid
                                             ReviewCreateRequest reviewCreateRequest) {
        var author = Utils.getCurrentUser();

        reviewService.create(author, reviewCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId) {
        var author = Utils.getCurrentUser();

        reviewService.delete(author, reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
