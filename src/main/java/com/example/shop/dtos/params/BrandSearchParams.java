package com.example.shop.dtos.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandSearchParams extends PaginationParams {
    private String searchText;
}
