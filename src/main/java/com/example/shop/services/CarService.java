package com.example.shop.services;

import com.example.shop.dtos.params.CarSearchParams;
import com.example.shop.dtos.request.CarCreateRequest;
import com.example.shop.dtos.request.CarUpdateRequest;
import com.example.shop.dtos.response.CarResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CarService {
    void createCar(CarCreateRequest carCreateRequest);

    void updateCar(String carId, CarUpdateRequest carUpdateRequest);

    void deleteCar(String id);

    Page<CarResponse> getAllCars(CarSearchParams carSearchParams);
}
