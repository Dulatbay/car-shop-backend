package com.example.shop.repositories;

import com.example.shop.entities.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends MongoRepository<Car, String> {
    List<Car> findByDeletedIsFalse();
    Optional<Car> findByIdAndDeletedIsFalse(String id);
}
