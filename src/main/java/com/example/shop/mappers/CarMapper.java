package com.example.shop.mappers;


import com.example.shop.dtos.request.CarCreateRequest;
import com.example.shop.dtos.request.CarUpdateRequest;
import com.example.shop.dtos.response.CarResponse;
import com.example.shop.entities.BasketItem;
import com.example.shop.entities.Car;
import com.example.shop.entities.CarBrand;
import com.example.shop.exceptions.DbNotFoundException;
import com.example.shop.repositories.CarBrandRepository;
import com.example.shop.services.FileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CarMapper {
    @Autowired
    protected CarBrandRepository carBrandRepository;

    @Autowired
    protected FileService fileService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carBrand", source = "brandId", qualifiedByName = "findByModelId")
    @Mapping(target = "imageUrls", qualifiedByName = "saveAndReturnUrl", source = "images")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "viewed", expression = "java(1)")
    public abstract Car toEntity(CarCreateRequest carCreateRequest);

    @Mapping(target = "brandId", expression = "java(car.getCarBrand().getId())")
    @Mapping(target = "brandName", expression = "java(car.getCarBrand().getName())")
    public abstract CarResponse toResponse(Car car);

    public CarResponse toBasketResponse(BasketItem basketItem) {
        CarResponse carResponse = toResponse(basketItem.getCar());
        carResponse.setQuantity(basketItem.getQuantity());
        return carResponse;
    }


    @Named("saveAndReturnUrl")
    public List<String> saveAndReturnUrl(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        files.forEach(file -> {
            urls.add(fileService.save(file));
        });

        return urls;
    }

    @Named("findByModelId")
    public CarBrand findByBrandId(String brandId) {
        return carBrandRepository.findById(brandId)
                .orElseThrow(() -> new DbNotFoundException("Incorrect module", HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    public void updateEntity(Car existingCar, CarUpdateRequest carUpdateRequest) {
        if (carUpdateRequest.getYear() > 0) {
            existingCar.setYear(carUpdateRequest.getYear());
        }
        if (carUpdateRequest.getMileage() > 0) {
            existingCar.setMileage(carUpdateRequest.getMileage());
        }
        if (carUpdateRequest.getPrice() > 0) {
            existingCar.setPrice(carUpdateRequest.getPrice());
        }
        if (carUpdateRequest.getImages() != null) {
            existingCar.getImageUrls()
                    .forEach(img -> fileService.deleteByFileName(img));
            List<String> newUrls = new ArrayList<>();
            carUpdateRequest.getImages()
                    .forEach(img -> newUrls.add(fileService.save(img)));
            existingCar.setImageUrls(newUrls);
        }
        if (carUpdateRequest.getDescription() != null) {
            existingCar.setDescription(carUpdateRequest.getDescription());
        }
        if (carUpdateRequest.getFuelType() != null) {
            existingCar.setFuelType(carUpdateRequest.getFuelType());
        }
        if (carUpdateRequest.getTransmission() != null) {
            existingCar.setTransmission(carUpdateRequest.getTransmission());
        }
        if (carUpdateRequest.getBrandId() != null) {
            existingCar.setCarBrand(findByBrandId(carUpdateRequest.getBrandId()));
        }
    }
}
