package com.example.shop.controllers;

import com.example.shop.dtos.params.BrandSearchParams;
import com.example.shop.dtos.params.CarSearchParams;
import com.example.shop.dtos.request.BrandCreateRequest;
import com.example.shop.dtos.request.CarBrandUpdateRequest;
import com.example.shop.dtos.request.CarCreateRequest;
import com.example.shop.dtos.request.CarUpdateRequest;
import com.example.shop.dtos.response.CarBrandResponse;
import com.example.shop.dtos.response.CarResponse;
import com.example.shop.dtos.response.PaginatedResponse;
import com.example.shop.services.CarBrandService;
import com.example.shop.services.CarService;
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
@RequestMapping("/car-brands")
@RequiredArgsConstructor
public class CarBrandController {
    private final CarBrandService carBrandService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> createBrand(@ModelAttribute
                                            @Valid
                                            BrandCreateRequest brandCreateRequest) {
        carBrandService.create(brandCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable String carId) {
        carBrandService.delete(carId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "{carId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCar(@PathVariable
                                          String carId,
                                          @ModelAttribute
                                          @Valid
                                          CarBrandUpdateRequest carBrandUpdateRequest) {
        carBrandService.update(carId, carBrandUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<CarBrandResponse>> getCars(@ModelAttribute
                                                                       @Valid
                                                                       BrandSearchParams searchParams) {
        Page<CarBrandResponse> cars = carBrandService.getAll(searchParams);

        return ResponseEntity.status(HttpStatus.OK).body(new PaginatedResponse<>(cars));
    }


}
