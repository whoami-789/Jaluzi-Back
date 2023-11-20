package com.example.jaluzi.services;

import com.example.jaluzi.models.Order;
import com.example.jaluzi.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) {
        // Устанавливаем поля total, deposit и reminder в 0
        order.setTotal(0);
        order.setDeposit(0);
        order.setReminder(0);
        // Устанавливаем completed в false
        order.setCompleted(false);
        return orderRepository.save(order);
    }

    public Order updateCompleted(Long orderId, boolean completed) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setCompleted(completed);
            return orderRepository.save(order);
        }
        return null; // Здесь можно выбрасывать исключение, если заказ не найден
    }

    public Order updateDeposit(Long orderId, int deposit) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setDeposit(deposit);
            return orderRepository.save(order);
        }
        return null; // Здесь можно выбрасывать исключение, если заказ не найден
    }

    public void updateOrderTotal(Long orderId) {
        // Получаем заказ по его идентификатору
        Order order = getOrderById(orderId);

        if (order != null) {
            // Рассчитываем новую общую стоимость на основе размеров заказа
            int newTotal = calculateTotal(order);

            // Устанавливаем новую общую стоимость заказа
            order.setTotal(newTotal);

            // Сохраняем обновленный заказ в репозиторий
            orderRepository.save(order);
        }
    }

    private int calculateTotal(Order order) {
        // Реализуйте логику расчета total в соответствии с вашими требованиями
        // Например, сумма всех размеров и других параметров заказа
        // Это просто пример, вам нужно адаптировать под ваш бизнес-логику
        int total = order.getSizes().stream()
                .mapToInt(size -> size.getWidth() * size.getHeight())
                .sum();

        // Добавьте другие параметры, если необходимо
        // total += ...

        return total;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
