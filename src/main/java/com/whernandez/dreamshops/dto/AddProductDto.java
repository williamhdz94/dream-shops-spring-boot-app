package com.whernandez.dreamshops.dto;

import java.math.BigDecimal;

import com.whernandez.dreamshops.model.Category;

import lombok.Data;

@Data
public class AddProductDto {

    private Long id;

    private String name;

    private String brand;

    private BigDecimal price;

    private int inventory;

    private String description;

    private Category category;
}
