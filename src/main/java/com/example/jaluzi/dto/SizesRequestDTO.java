package com.example.jaluzi.dto;

import lombok.Data;

@Data
public class SizesRequestDTO {
    private Long id;
    private double width;
    private double height;
    private int quantity;
    private double price;
}
