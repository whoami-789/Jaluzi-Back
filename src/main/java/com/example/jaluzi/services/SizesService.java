package com.example.jaluzi.services;

import com.example.jaluzi.models.Sizes;
import com.example.jaluzi.repositories.SizesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizesService {
    @Autowired
    private SizesRepository sizesRepository;

    public List<Sizes> getAllSizes() {
        return sizesRepository.findAll();
    }

    public Sizes getSizesById(Long id) {
        return sizesRepository.findById(id).orElse(null);
    }

    public Sizes saveSizes(Sizes sizes) {
        return sizesRepository.save(sizes);
    }

    public void deleteSizes(Long id) {
        sizesRepository.deleteById(id);
    }

    // Дополнительные методы по необходимости
}
