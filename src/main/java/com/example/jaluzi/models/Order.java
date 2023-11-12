package com.example.jaluzi.models;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String address;
    private String phoneNumber;
    private int total;
    private int deposit;
    private int reminder;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Sizes> sizes = new ArrayList<>();
}
