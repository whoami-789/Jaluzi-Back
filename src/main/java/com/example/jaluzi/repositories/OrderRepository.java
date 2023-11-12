package com.example.jaluzi.repositories;

import com.example.jaluzi.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Здесь вы можете добавить собственные методы для запросов к базе данных, если это необходимо
}
