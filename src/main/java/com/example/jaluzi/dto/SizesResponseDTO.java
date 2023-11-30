package com.example.jaluzi.dto;

import lombok.Data;

@Data
public class SizesResponseDTO {
    private Long id;
    private double width;
    private double height;
    private int quantity;
    private double total;
    private double price;
    private String note;
    private Long order;
    private double square;

    public SizesResponseDTO() {

    }

    public SizesResponseDTO(Long id, double width, double height, double square, double price, int quantity, double total, String note) {

        this.id = id;
        this.width = width;
        this.height = height;
        this.square = square;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.note = note;
    }
}
