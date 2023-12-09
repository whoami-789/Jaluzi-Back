package com.example.jaluzi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private LocalDate date;
    private String address;
    private String phoneNumber;
    private boolean completed = false;
    private boolean workshopCompleted = false;
    private double total;
    private double deposit;
    private double reminder;
    private String note = "";


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Sizes> sizes = new ArrayList<>();

    @PrePersist
    private void init() {
        date = LocalDate.now();
    }

    public double calculateTotalSquare() {
        double totalSquare = 0.0;
        for (Sizes size : sizes) {
            totalSquare += size.getSquare();
        }
        return totalSquare;
    }
}
