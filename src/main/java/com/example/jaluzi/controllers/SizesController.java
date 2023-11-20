package com.example.jaluzi.controllers;

import com.example.jaluzi.models.Sizes;
import com.example.jaluzi.services.SizesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
public class SizesController {

    @Autowired
    private SizesService sizesService;

    @PostMapping("/add/{orderId}")
    public ResponseEntity<Sizes> addSizesToOrder(@PathVariable Long orderId, @RequestBody Sizes sizes) throws ChangeSetPersister.NotFoundException {
        Sizes addedSizes = sizesService.addSizes(orderId, sizes);
        return new ResponseEntity<>(addedSizes, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sizes> getSizesById(@PathVariable Long id) {
        Sizes sizes = sizesService.getSizesById(id);
        return sizes != null
                ? new ResponseEntity<>(sizes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Sizes>> getAllSizes() {
        List<Sizes> allSizes = sizesService.getAllSizes();
        return new ResponseEntity<>(allSizes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSizes(@PathVariable Long id) {
        sizesService.deleteSizes(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
