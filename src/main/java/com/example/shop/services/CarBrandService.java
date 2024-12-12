package com.example.shop.services;

import com.example.shop.dtos.params.BrandSearchParams;
import com.example.shop.dtos.request.BrandCreateRequest;
import com.example.shop.dtos.request.CarBrandUpdateRequest;
import com.example.shop.dtos.request.CarUpdateRequest;
import com.example.shop.dtos.response.CarBrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CarBrandService {
    void create(BrandCreateRequest brandCreateRequest);

    void delete(String id);

    void update(String id, CarBrandUpdateRequest carBrandUpdateRequest);

    Page<CarBrandResponse> getAll(BrandSearchParams brandSearchParams);
}
