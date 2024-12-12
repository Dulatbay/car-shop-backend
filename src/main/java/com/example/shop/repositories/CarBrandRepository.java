package com.example.shop.repositories;

import com.example.shop.entities.CarBrand;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarBrandRepository extends MongoRepository<CarBrand, String> {
}
