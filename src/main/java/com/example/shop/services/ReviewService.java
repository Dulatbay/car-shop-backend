package com.example.shop.services;

import com.example.shop.dtos.params.PaginationParams;
import com.example.shop.dtos.request.ReviewCreateRequest;
import com.example.shop.dtos.response.ReviewResponse;
import com.example.shop.entities.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {

    void create(User author, ReviewCreateRequest reviewCreateRequest);

    void delete(User author, String reviewId);

    Page<ReviewResponse> getByCar(String carId, PaginationParams paginationParams);
}
