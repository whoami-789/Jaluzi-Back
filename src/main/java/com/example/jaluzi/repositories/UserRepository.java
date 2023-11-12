package com.example.jaluzi.repositories;

import com.example.jaluzi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Ваши собственные методы
}
