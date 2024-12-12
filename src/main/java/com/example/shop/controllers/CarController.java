package com.example.shop.controllers;

import com.example.shop.constants.Utils;
import com.example.shop.dtos.params.CarSearchParams;
import com.example.shop.dtos.params.PaginationParams;
import com.example.shop.dtos.request.CarCreateRequest;
import com.example.shop.dtos.request.CarUpdateRequest;
import com.example.shop.dtos.response.CarResponse;
import com.example.shop.dtos.response.PaginatedResponse;
import com.example.shop.dtos.response.ReviewResponse;
import com.example.shop.entities.Car;
import com.example.shop.services.CarService;
import com.example.shop.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> createCar(@Valid
                                          @ModelAttribute
                                          CarCreateRequest carCreateRequest) {
        carService.createCar(carCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable String carId) {
        carService.deleteCar(carId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "{carId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCar(@PathVariable
                                          String carId,
                                          @ModelAttribute
                                          @Valid
                                          CarUpdateRequest carUpdateRequest) {
        carService.updateCar(carId, carUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<CarResponse>> getCars(@ModelAttribute
                                                                  @Valid
                                                                  CarSearchParams carSearchParams) {
        var cars = carService.getAllCars(carSearchParams);

        return ResponseEntity.status(HttpStatus.OK).body(new PaginatedResponse<>(cars));
    }

    @GetMapping("/{carId}/reviews")
    public ResponseEntity<PaginatedResponse<ReviewResponse>> getReviewsByCarId(@PathVariable
                                                                               String carId,
                                                                               @ModelAttribute
                                                                               @Valid
                                                                               PaginationParams paginationParams) {
        Page<ReviewResponse> reviews = reviewService.getByCar(carId, paginationParams);

        return ResponseEntity.ok(new PaginatedResponse<>(reviews));
    }

}
