package com.example.shop.services.impl;

import com.example.shop.dtos.params.BrandSearchParams;
import com.example.shop.dtos.request.BrandCreateRequest;
import com.example.shop.dtos.request.CarBrandUpdateRequest;
import com.example.shop.dtos.response.CarBrandResponse;
import com.example.shop.entities.CarBrand;
import com.example.shop.exceptions.DbNotFoundException;
import com.example.shop.repositories.CarBrandRepository;
import com.example.shop.services.CarBrandService;
import com.example.shop.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarBrandServiceImpl implements CarBrandService {
    private final CarBrandRepository carBrandRepository;
    private final MongoTemplate mongoTemplate;
    private final FileService fileService;

    @Override
    @Transactional
    public void create(BrandCreateRequest brandCreateRequest) {
        List<String> urls = new ArrayList<>();
        brandCreateRequest.getImages()
                .forEach(image -> {
                    urls.add(fileService.save(image));
                });
        CarBrand carBrand = new CarBrand()
                .setName(brandCreateRequest.getName())
                .setCountry(brandCreateRequest.getCountry())
                .setImageUrls(urls);

        carBrandRepository.save(carBrand);
    }

    @Override
    @Transactional
    public void delete(String id) {
        var brandToDelete = carBrandRepository.findById(id)
                .orElseThrow(() -> new DbNotFoundException("Car brand not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (brandToDelete.isDeleted())
            throw new DbNotFoundException("Car brand deleted", HttpStatus.NOT_FOUND.getReasonPhrase());

        brandToDelete.getImageUrls()
                .forEach(fileService::deleteByFileName);

        brandToDelete.setDeleted(true);
        carBrandRepository.save(brandToDelete);
    }

    @Override
    @Transactional
    public void update(String id, CarBrandUpdateRequest carBrandUpdateRequest) {
        var brandToUpdate = carBrandRepository.findById(id)
                .orElseThrow(() -> new DbNotFoundException("Car brand not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (brandToUpdate.isDeleted())
            throw new DbNotFoundException("Car brand not found", HttpStatus.NOT_FOUND.getReasonPhrase());

        brandToUpdate.getImageUrls()
                .forEach(fileService::deleteByFileName);

        List<String> urls = new ArrayList<>();

        carBrandUpdateRequest
                .getImages()
                .forEach(image -> {
                    urls.add(fileService.save(image));
                });

        brandToUpdate.setImageUrls(urls);
        brandToUpdate.setName(carBrandUpdateRequest.getNewName());
        brandToUpdate.setCountry(carBrandUpdateRequest.getCountry());

        carBrandRepository.save(brandToUpdate);
    }

    @Override
    public Page<CarBrandResponse> getAll(BrandSearchParams brandSearchParams) {
        var pageable = PageRequest.of(brandSearchParams.getPage(), brandSearchParams.getSize());

        var query = getCarBrandQueryByParams(brandSearchParams)
                .with(pageable);

        var brands = mongoTemplate.find(query, CarBrand.class);
        long count = mongoTemplate.count(query.skip(0).limit(0), CarBrand.class);

        List<CarBrandResponse> carResponses = brands.stream()
                .map(carBrand -> new CarBrandResponse(carBrand.getId(), carBrand.getName(), carBrand.getCountry(), carBrand.getImageUrls()))
                .toList();

        return new PageImpl<>(carResponses, pageable, count);
    }

    private static Query getCarBrandQueryByParams(BrandSearchParams brandSearchParams) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").is(false));

        return query;
    }
}
