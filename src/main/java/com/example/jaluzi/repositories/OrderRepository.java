package com.example.jaluzi.repositories;

import com.example.jaluzi.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.sizes")
    List<Order> findAllWithSizes();

    @Query("SELECT o FROM Order o WHERE MONTH(o.date) = :month AND YEAR(o.date) = :year")
    List<Order> findByMonthAndYear(@Param("month") int month, @Param("year") int year);}
