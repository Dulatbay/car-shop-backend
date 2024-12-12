package com.example.shop.controllers;

import com.example.shop.constants.Utils;
import com.example.shop.dtos.params.PaginationParams;
import com.example.shop.dtos.response.CarResponse;
import com.example.shop.dtos.response.PaginatedResponse;
import com.example.shop.services.BasketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/baskets")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/my")
    public ResponseEntity<PaginatedResponse<CarResponse>> getCart(@ModelAttribute
                                                                  @Valid
                                                                  PaginationParams paginationParams) {
        var user = Utils.getCurrentUser();
        Page<CarResponse> response = basketService.getBasketByUser(user.getId(), paginationParams);

        return ResponseEntity.ok(new PaginatedResponse<>(response));
    }

    @PostMapping("/my")
    public ResponseEntity<Void> createCart(@NotNull
                                           String carId,
                                           @NotNull
                                           @Min(1)
                                           Integer quantity) {
        var user = Utils.getCurrentUser();

        basketService.addToBasket(user, carId, quantity);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/my")
    public ResponseEntity<Void> deleteFromBasket(@NotNull
                                                 String carId,
                                                 @NotNull
                                                 @Min(1)
                                                 Integer quantity) {
        var user = Utils.getCurrentUser();

        basketService.deleteFromBasket(user, carId, quantity);

        return ResponseEntity.noContent().build();
    }
}
