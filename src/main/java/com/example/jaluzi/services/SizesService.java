package com.example.jaluzi.services;

import com.example.jaluzi.models.Order;
import com.example.jaluzi.models.Sizes;
import com.example.jaluzi.repositories.SizesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizesService {

    @Autowired
    private SizesRepository sizesRepository;

    @Autowired
    private OrderService orderService;

    public Sizes addSizes(Long orderId, Sizes sizes) throws ChangeSetPersister.NotFoundException {
        // Получаем заказ по его идентификатору
        Order order = orderService.getOrderById(orderId);

        // Проверяем, что заказ существует
        if (order == null) {
            throw new ChangeSetPersister.NotFoundException();
        }

        // Устанавливаем связь между размерами и заказом
        sizes.setOrder(order);

        // Сохраняем размеры в репозиторий
        Sizes savedSizes = sizesRepository.save(sizes);

        // Обновляем общую стоимость заказа
        orderService.updateOrderTotal(orderId);

        return savedSizes;
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
}
