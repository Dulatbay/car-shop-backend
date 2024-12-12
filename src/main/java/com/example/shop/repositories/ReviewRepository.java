package com.example.shop.repositories;

import com.example.shop.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Page<Review> findByCar_IdAndDeletedIsFalse(String carId, Pageable pageable);
    Optional<Review> findByIdAndDeletedIsFalse(String id);
    @Aggregation(pipeline = {
            "{ $match: { 'car.id': ?0, 'deleted': false } }",
            "{ $group: { _id: null, averageRating: { $avg: '$rating' } } }"
    })
    Double findAverageRatingByCarId(String carId);
}
