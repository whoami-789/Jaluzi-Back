package com.example.jaluzi.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private String address;
    private String phoneNumber;
    private String date;
    private Double total;
    private Double deposit;
    private Double reminder;
    private String note;
    private List<SizesResponseDTO> sizes;

    public OrderResponseDTO(Long id, String customerName, String address, String phoneNumber, String date,
                            Double total, Double deposit, Double reminder, String note, List<SizesResponseDTO> sizes) {
        this.id = id;
        this.customerName = customerName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.total = total;
        this.deposit = deposit;
        this.reminder = reminder;
        this.note = note;
        this.sizes = sizes;
    }
}
