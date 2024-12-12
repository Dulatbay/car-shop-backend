package com.example.shop.dtos.params;

import com.example.shop.entities.enums.FuelType;
import com.example.shop.entities.enums.Transmission;
import com.example.shop.entities.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarSearchParams extends PaginationParams {
    private Integer year;
    private Double minMileage;
    private Double maxMileage;
    private Double minPrice;
    private Double maxPrice;
    private FuelType fuelType;
    private Transmission transmission;
    private String searchText;
    private VehicleType vehicleType;
    private Double minVolumeOfEngine;
    private Double maxVolumeOfEngine;
}
