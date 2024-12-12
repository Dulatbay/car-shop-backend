package com.example.shop.dtos.response;

import com.example.shop.entities.enums.Driving;
import com.example.shop.entities.enums.FuelType;
import com.example.shop.entities.enums.Transmission;
import com.example.shop.entities.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {
    private String id;
    private String modelName;
    private Integer year;
    private Double mileage;
    private Double price;
    private List<String> imageUrls;
    private String description;
    private FuelType fuelType;
    private Transmission transmission;
    private String brandName;
    private String brandId;
    private String createdBy;
    private Double volumeOfEngine;
    private Driving driving;
    private VehicleType vehicleType;
    private Double rating;
    private Integer quantity;
}
