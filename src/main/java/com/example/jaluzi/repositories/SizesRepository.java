package com.example.jaluzi.repositories;

import com.example.jaluzi.models.Sizes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizesRepository extends JpaRepository<Sizes, Long> {
    Optional<Sizes> findSizesById(Long id);
}
