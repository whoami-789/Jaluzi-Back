package com.example.jaluzi.controllers;

import com.example.jaluzi.dto.SizesInfoDTO;
import com.example.jaluzi.dto.SizesRequestDTO;
import com.example.jaluzi.dto.SizesResponseDTO;
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
    public ResponseEntity<SizesResponseDTO> addSizesToOrder(@PathVariable Long orderId, @RequestBody SizesRequestDTO sizesRequestDTO) throws ChangeSetPersister.NotFoundException {
        Sizes addedSizes = sizesService.addSizes(orderId, sizesRequestDTO);

        // Создаем DTO для ответа
        SizesResponseDTO responseDTO = new SizesResponseDTO(addedSizes.getId(), addedSizes.getName(), addedSizes.getWidth(), addedSizes.getHeight(), addedSizes.getSquare(), addedSizes.getPrice(), addedSizes.getQuantity(), addedSizes.getTotal(), addedSizes.getNote());
        // Заполните DTO данными из добавленных размеров
        responseDTO.setId(addedSizes.getId());
        responseDTO.setWidth(addedSizes.getWidth());
        responseDTO.setHeight(addedSizes.getHeight());
        responseDTO.setQuantity(addedSizes.getQuantity());
        responseDTO.setPrice(addedSizes.getPrice());
        responseDTO.setNote(addedSizes.getNote());

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SizesInfoDTO> getSizesInfoById(@PathVariable Long id) {
        SizesInfoDTO sizesInfoDTO = sizesService.getSizesInfoById(id);

        return sizesInfoDTO != null
                ? new ResponseEntity<>(sizesInfoDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<SizesInfoDTO>> getAllSizesInfo() {
        List<SizesInfoDTO> allSizesInfo = sizesService.getAllSizesInfo();

        return new ResponseEntity<>(allSizesInfo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSizes(@PathVariable Long id) {
        sizesService.deleteSizes(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
