package com.eckrin.test.deserialization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodDeserializationDto {
    private Long id;
    private String name;
}
