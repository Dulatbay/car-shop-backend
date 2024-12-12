package com.example.shop.dtos.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationParams {
    private int page;
    private int size;
}
