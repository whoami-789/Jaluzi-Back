package com.example.jaluzi.DTO;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String customerName;
    private String address;
    private String phoneNumber;
    private String date;
    private Double total;
    private Double deposit;
    private Double reminder;
    private List<SizeDTO> sizes;
}
