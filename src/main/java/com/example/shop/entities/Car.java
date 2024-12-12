package com.example.shop.entities;


import com.example.shop.entities.base.BaseEntity;
import com.example.shop.entities.enums.Driving;
import com.example.shop.entities.enums.FuelType;
import com.example.shop.entities.enums.Transmission;
import com.example.shop.entities.enums.VehicleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@Document(collection = "cars")
@Accessors(chain = true)
@Getter
@Setter
public class Car extends BaseEntity {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String modelName;

    @Min(1400)
    @Max(2100)
    private int year;

    @Min(0)
    private double mileage;

    @Min(0)
    private double price;

    private List<String> imageUrls;
    private String description;

    @Field(targetType = FieldType.STRING)
    private FuelType fuelType;

    @Field(targetType = FieldType.STRING)
    private Transmission transmission;

    @DBRef
    @NotNull
    private CarBrand carBrand;

    private double volumeOfEngine;
    private Driving driving;
    private VehicleType vehicleType;

    private Integer viewed = 0;
    private Double rating;
    private Integer quantity;
}
