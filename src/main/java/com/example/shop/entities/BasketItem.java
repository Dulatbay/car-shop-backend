package com.example.shop.entities;

import com.example.shop.entities.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "cars")
@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
public class BasketItem extends BaseEntity {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @DBRef
    private User author;

    @DBRef
    private Car car;


    private int quantity;
}
