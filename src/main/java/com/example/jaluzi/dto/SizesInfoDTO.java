package com.example.jaluzi.dto;

import lombok.Data;

@Data
public class SizesInfoDTO {
    private Long id;
    private String name;
    private double width;
    private double height;
    private double quantity;
    private double price;
    private String note;
    private Long order;

    public SizesInfoDTO(Long id, String name, double width, double height, double quantity, double price, String note, Long order) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.quantity = quantity;
        this.price = price;
        this.note = note;
        this.order = order;
    }
}
