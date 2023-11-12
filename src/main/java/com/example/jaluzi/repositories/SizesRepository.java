package com.example.jaluzi.repositories;

import com.example.jaluzi.models.Sizes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizesRepository extends JpaRepository<Sizes, Long> {
    // Ваши собственные методы
}
