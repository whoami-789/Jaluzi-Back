package com.example.jaluzi.dto;

import lombok.Data;

@Data
public class SizesRequestDTO {
    private Long key;
    private String name;
    private double width;
    private double height;
    private int quantity;
    private double price;
    private double total;
    private String note;
    private double square;
    private double totalSquare;
}
