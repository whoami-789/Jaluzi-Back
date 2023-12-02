package com.example.jaluzi.services;

import com.example.jaluzi.dto.SizesInfoDTO;
import com.example.jaluzi.dto.SizesRequestDTO;
import com.example.jaluzi.models.Order;
import com.example.jaluzi.models.Sizes;
import com.example.jaluzi.repositories.OrderRepository;
import com.example.jaluzi.repositories.SizesRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SizesService {

    private final SizesRepository sizesRepository;

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    public SizesService(SizesRepository sizesRepository, OrderService orderService, OrderRepository orderRepository) {
        this.sizesRepository = sizesRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    public Sizes addSizes(Long orderId, SizesRequestDTO sizesRequestDTO) throws ChangeSetPersister.NotFoundException {
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            throw new ChangeSetPersister.NotFoundException();
        }

        Sizes sizes = new Sizes();
        // Заполните размеры данными из DTO
        sizes.setName(sizesRequestDTO.getName());
        sizes.setWidth(sizesRequestDTO.getWidth());
        sizes.setHeight(sizesRequestDTO.getHeight());
        sizes.setQuantity(sizesRequestDTO.getQuantity());
        sizes.setPrice(sizesRequestDTO.getPrice());
        sizes.setSquare(sizesRequestDTO.getWidth() * sizesRequestDTO.getHeight());

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
                    sizes.getName(),
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
            SizesInfoDTO sizesInfoDTO = new SizesInfoDTO(sizes.getId(), sizes.getName(), sizes.getWidth(), sizes.getHeight(), sizes.getQuantity(), sizes.getPrice(), sizes.getNote(), sizes.getOrder().getId());
            // Заполните DTO данными из размеров
            sizesInfoDTO.setId(sizes.getId());
            sizesInfoDTO.setName(sizes.getName());
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

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

//    @Transactional
//    public void deleteSizes(Long id) {
//        try {
//            // Получаем размер для получения связанного заказа
//            Optional<Sizes> sizeOptional = sizesRepository.findById(id);
//
//            sizeOptional.ifPresent(size -> {
//                // Получаем связанный заказ
//                Order order = size.getOrder();
//
//                // Удаляем размер
//                sizesRepository.deleteById(id);
//
//                // Пересчитываем итоговую стоимость заказа
//                recalculateOrderTotal(order.getId());
//
//                // Обновляем заказ в репозитории
//                orderRepository.save(order);
//            });
//        } catch (Exception e) {
//            // Обработка ошибок, если необходимо
//            e.printStackTrace();
//        }
//    }

    public void deleteSizes(Long id, Order orderId) {
        sizesRepository.deleteById(id);
        recalculateOrderTotal(orderId);
    }

    private void recalculateOrderTotal(Order id) {

        if (id != null) {
            double newTotal = calculateTotal(id);

            id.setTotal(newTotal);
            id.setReminder(newTotal - id.getReminder());

            orderRepository.save(id);
        }
    }

    public void updateOrderTotal(Long orderId) {

    }

    private double calculateTotal(Order order) {
        return order.getSizes().stream()
                .mapToDouble(size -> size.getPrice() * size.getQuantity())
                .sum();
    }

    public void updateNote(Long sizeId, String note) {
        Sizes sizes = getSizesById(sizeId);
        if (sizes != null) {
            sizes.setNote(note);
            sizesRepository.save(sizes);
        }
    }
}
