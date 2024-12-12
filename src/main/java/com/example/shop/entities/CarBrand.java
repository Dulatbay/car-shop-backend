package com.example.shop.entities;

import com.example.shop.entities.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "car-brands")
@Accessors(chain = true)
@Getter
@Setter
public class CarBrand extends BaseEntity {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String name;
    private List<String> imageUrls;
    private String country;
}
