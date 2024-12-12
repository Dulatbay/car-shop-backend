package com.example.shop.services;

import com.example.shop.dtos.params.PaginationParams;
import com.example.shop.dtos.response.CarResponse;
import com.example.shop.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BasketService {
    Page<CarResponse> getBasketByUser(String userId, PaginationParams paginationParams);

    void deleteFromBasket(User author, String carId, Integer quantity);

    void addToBasket(User user,String carId, Integer quantity);
}
