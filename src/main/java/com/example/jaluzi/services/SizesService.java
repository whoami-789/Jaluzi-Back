package com.example.jaluzi.services;

import com.example.jaluzi.dto.SizesInfoDTO;
import com.example.jaluzi.dto.SizesRequestDTO;
import com.example.jaluzi.models.Order;
import com.example.jaluzi.models.Sizes;
import com.example.jaluzi.repositories.SizesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SizesService {

    @Autowired
    private SizesRepository sizesRepository;

    @Autowired
    private OrderService orderService;

    public Sizes addSizes(Long orderId, SizesRequestDTO sizesRequestDTO) throws ChangeSetPersister.NotFoundException {
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            throw new ChangeSetPersister.NotFoundException();
        }

        Sizes sizes = new Sizes();
        // Заполните размеры данными из DTO
        sizes.setWidth(sizesRequestDTO.getWidth());
        sizes.setHeight(sizesRequestDTO.getHeight());
        sizes.setQuantity(sizesRequestDTO.getQuantity());
        sizes.setPrice(sizesRequestDTO.getPrice());

        sizes.setOrder(order);

        Sizes savedSizes = sizesRepository.save(sizes);

        orderService.updateOrderTotal(orderId);

        return savedSizes;
    }

    public SizesInfoDTO getSizesInfoById(Long id) {
        Sizes sizes = sizesRepository.findById(id).orElse(null);
        if (sizes != null) {
            return new SizesInfoDTO(
                    sizes.getId(),
                    sizes.getWidth(),
                    sizes.getHeight(),
                    sizes.getQuantity(),
                    sizes.getPrice(),
                    sizes.getNote(),
                    sizes.getOrder().getId() // получаем идентификатор заказа
            );
        }
        return null;
    }

    public List<SizesInfoDTO> getAllSizesInfo() {
        List<Sizes> allSizes = sizesRepository.findAll();
        List<SizesInfoDTO> allSizesInfoDTO = new ArrayList<>();

        for (Sizes sizes : allSizes) {
            SizesInfoDTO sizesInfoDTO = new SizesInfoDTO(sizes.getId(), sizes.getWidth(), sizes.getHeight(), sizes.getQuantity(), sizes.getPrice(), sizes.getNote(), sizes.getOrder().getId());
            // Заполните DTO данными из размеров
            sizesInfoDTO.setId(sizes.getId());
            sizesInfoDTO.setWidth(sizes.getWidth());
            sizesInfoDTO.setHeight(sizes.getHeight());
            sizesInfoDTO.setQuantity(sizes.getQuantity());
            sizesInfoDTO.setPrice(sizes.getPrice());
            sizesInfoDTO.setNote(sizes.getNote());
            sizesInfoDTO.setOrder(sizes.getOrder().getId());

            allSizesInfoDTO.add(sizesInfoDTO);
        }

        return allSizesInfoDTO;
    }



    public Sizes getSizesById(Long id) {
        return sizesRepository.findById(id).orElse(null);
    }

    public List<Sizes> getAllSizes() {
        return sizesRepository.findAll();
    }

    public void deleteSizes(Long id) {
        sizesRepository.deleteById(id);
    }

    public Sizes updateNote(Long sizeId, String note) {
        Sizes sizes = getSizesById(sizeId);
        if (sizes != null) {
            sizes.setNote(note);
            return sizesRepository.save(sizes);
        }
        return null;
    }
}
