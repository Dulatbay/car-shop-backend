package com.example.shop.services.impl;

import com.example.shop.dtos.params.CarSearchParams;
import com.example.shop.dtos.request.CarCreateRequest;
import com.example.shop.dtos.request.CarUpdateRequest;
import com.example.shop.dtos.response.CarResponse;
import com.example.shop.entities.Car;
import com.example.shop.exceptions.DbNotFoundException;
import com.example.shop.mappers.CarMapper;
import com.example.shop.repositories.CarRepository;
import com.example.shop.services.CarService;
import com.example.shop.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarMapper carMapper;
    private final CarRepository carRepository;
    private final MongoTemplate mongoTemplate;
    private final FileService fileService;

    @Override
    @Transactional
    public void createCar(CarCreateRequest carCreateRequest) {
        Car toSave = carMapper.toEntity(carCreateRequest);
        carRepository.save(toSave);
    }

    @Override
    @Transactional
    public void updateCar(String carId, CarUpdateRequest carUpdateRequest) {
        var carToUpdate = carRepository.findById(carId)
                .orElseThrow(() -> new DbNotFoundException("Car not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (carToUpdate.isDeleted())
            throw new DbNotFoundException("Car not found", HttpStatus.NOT_FOUND.getReasonPhrase());

        carMapper.updateEntity(carToUpdate, carUpdateRequest);

        carRepository.save(carToUpdate);
    }

    @Override
    @Transactional
    public void deleteCar(String id) {
        var carToDelete = carRepository.findById(id)
                .orElseThrow(() -> new DbNotFoundException("Car not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (carToDelete.isDeleted())
            throw new DbNotFoundException("Car deleted", HttpStatus.NOT_FOUND.getReasonPhrase());

        carToDelete.getImageUrls()
                .forEach(fileService::deleteByFileName);
        carToDelete.setDeleted(true);
        carRepository.save(carToDelete);
    }

    @Override
    public Page<CarResponse> getAllCars(CarSearchParams carSearchParams) {
        var pageable = PageRequest.of(carSearchParams.getPage(), carSearchParams.getSize());

        var query = getCarQueryByParams(carSearchParams)
                .with(pageable);

        var cars = mongoTemplate.find(query, Car.class);
        long count = mongoTemplate.count(query.skip(0).limit(0), Car.class);

        List<String> carIds = cars.stream().map(Car::getId).collect(Collectors.toList());

        if (!carIds.isEmpty()) {
            Query incrementQuery = new Query(Criteria.where("id").in(carIds));
            Update incrementUpdate = new Update().inc("viewed", 1);
            mongoTemplate.updateMulti(incrementQuery, incrementUpdate, Car.class);
        }

        List<CarResponse> quizResponses = cars.stream()
                .map(carMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(quizResponses, pageable, count);

    }

    private static Query getCarQueryByParams(CarSearchParams carSearchParams) {
        Query query = new Query();

        query.addCriteria(Criteria.where("deleted").is(false));

        if (carSearchParams.getYear() != null) {
            query.addCriteria(Criteria.where("year").is(carSearchParams.getYear()));
        }
        if (carSearchParams.getMinMileage() != null || carSearchParams.getMaxMileage() != null) {
            Criteria mileageCriteria = new Criteria("mileage");
            if (carSearchParams.getMinMileage() != null) {
                mileageCriteria = mileageCriteria.gte(carSearchParams.getMinMileage());
            }
            if (carSearchParams.getMaxMileage() != null) {
                mileageCriteria = mileageCriteria.lte(carSearchParams.getMaxMileage());
            }
            query.addCriteria(mileageCriteria);
        }
        if (carSearchParams.getMinPrice() != null || carSearchParams.getMaxPrice() != null) {
            Criteria priceCriteria = new Criteria("price");
            if (carSearchParams.getMinPrice() != null) {
                priceCriteria = priceCriteria.gte(carSearchParams.getMinPrice());
            }
            if (carSearchParams.getMaxPrice() != null) {
                priceCriteria = priceCriteria.lte(carSearchParams.getMaxPrice());
            }
            query.addCriteria(priceCriteria);
        }
        if (carSearchParams.getFuelType() != null) {
            query.addCriteria(Criteria.where("fuelType").is(carSearchParams.getFuelType()));
        }
        if (carSearchParams.getTransmission() != null) {
            query.addCriteria(Criteria.where("transmission").is(carSearchParams.getTransmission()));
        }
        if (carSearchParams.getVehicleType() != null) {
            query.addCriteria(Criteria.where("vehicleType").is(carSearchParams.getVehicleType()));
        }
        if (carSearchParams.getMinVolumeOfEngine() != null || carSearchParams.getMaxVolumeOfEngine() != null) {
            Criteria volumeCriteria = new Criteria("volumeOfEngine");
            if (carSearchParams.getMinVolumeOfEngine() != null) {
                volumeCriteria = volumeCriteria.gte(carSearchParams.getMinVolumeOfEngine());
            }
            if (carSearchParams.getMaxVolumeOfEngine() != null) {
                volumeCriteria = volumeCriteria.lte(carSearchParams.getMaxVolumeOfEngine());
            }
            query.addCriteria(volumeCriteria);
        }
        if (carSearchParams.getSearchText() != null && !carSearchParams.getSearchText().isBlank()) {
            String searchText = carSearchParams.getSearchText();
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("modelName").regex(searchText, "i"),
                    Criteria.where("brandName").regex(searchText, "i"),
                    Criteria.where("description").regex(searchText, "i")
            ));
        }

        return query;
    }


}
