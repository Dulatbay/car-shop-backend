package com.example.shop.services.impl;

import com.example.shop.dtos.params.PaginationParams;
import com.example.shop.dtos.response.CarResponse;
import com.example.shop.entities.BasketItem;
import com.example.shop.entities.User;
import com.example.shop.exceptions.DbNotFoundException;
import com.example.shop.mappers.CarMapper;
import com.example.shop.repositories.BasketItemRepository;
import com.example.shop.repositories.CarRepository;
import com.example.shop.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketItemRepository basketItemRepository;
    private final CarMapper carMapper;
    private final CarRepository carRepository;


    @Override
    public Page<CarResponse> getBasketByUser(String userId, PaginationParams paginationParams) {
        var pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());

        return basketItemRepository.findByAuthor_Id(userId, pageable)
                .map(carMapper::toBasketResponse);
    }

    @Override
    @Transactional
    public void deleteFromBasket(User author, String carId, Integer quantity) {
        var car = carRepository.findByIdAndDeletedIsFalse(carId)
                .orElseThrow(() -> new DbNotFoundException("Car not found", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        var basketItem = basketItemRepository.findByAuthor_IdAndCar_Id(author.getId(), carId)
                .orElseThrow(() -> new DbNotFoundException("Car not found", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        if (basketItem.getQuantity() < quantity)
            throw new IllegalArgumentException("Request to delete quantity bigger");

        basketItem.setQuantity(basketItem.getQuantity() - quantity);
        basketItemRepository.save(basketItem);

        car.setQuantity(car.getQuantity() + quantity);
        carRepository.save(car);
    }

    @Override
    @Transactional
    public void addToBasket(User author, String carId, Integer quantity) {
        var car = carRepository.findByIdAndDeletedIsFalse(carId)
                .orElseThrow(() -> new DbNotFoundException("Car not found", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        var basketItemOptional = basketItemRepository.findByAuthor_IdAndCar_Id(author.getId(), carId);

        if(car.getQuantity() < quantity)
            throw new IllegalArgumentException("Request to add quantity bigger");


        car.setQuantity(car.getQuantity() - quantity);
        carRepository.save(car);

        if(basketItemOptional.isEmpty()){
            var basketItem = new BasketItem()
                    .setAuthor(author)
                    .setCar(car)
                    .setQuantity(quantity);

            basketItemRepository.save(basketItem);
            return;
        }


        var basketItem = basketItemOptional.get();
        basketItem.setQuantity(basketItem.getQuantity() + quantity);
        basketItemRepository.save(basketItem);
    }
}
