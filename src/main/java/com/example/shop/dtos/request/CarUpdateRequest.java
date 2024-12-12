package com.example.shop.dtos.request;

import com.example.shop.entities.enums.Driving;
import com.example.shop.entities.enums.FuelType;
import com.example.shop.entities.enums.Transmission;
import com.example.shop.entities.enums.VehicleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarUpdateRequest {

    @NotNull
    private String modelName;

    @Min(1886)
    @Max(2100)
    private int year;

    @Min(0)
    private double mileage;

    @Min(0)
    private double price;

    @NotNull
    private List<MultipartFile> images = new ArrayList<>();

    @NotNull
    private String description;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private Transmission transmission;

    @NotNull
    private String brandId;

    private double volumeOfEngine;
    private Driving driving;
    private VehicleType vehicleType;
    private Integer quantity;
}

