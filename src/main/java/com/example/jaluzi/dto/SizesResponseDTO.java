package com.example.jaluzi.dto;

import com.example.jaluzi.models.Order;
import lombok.Data;

@Data
public class SizesResponseDTO {
    private Long id;
    private double width;
    private double height;
    private int quantity;
    private double price;
    private String note;
    private Long order;
}
