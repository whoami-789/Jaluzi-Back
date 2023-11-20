package com.example.jaluzi.services;

import com.example.jaluzi.DTO.OrderDTO;
import com.example.jaluzi.DTO.SizeDTO;
import com.example.jaluzi.models.Order;
import com.example.jaluzi.models.Sizes;
import com.example.jaluzi.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) {
        order.setTotal(0);
        order.setDeposit(0);
        order.setReminder(0);
        order.setCompleted(false);
        return orderRepository.save(order);
    }

    public Order updateCompleted(Long orderId, boolean completed) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setCompleted(completed);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order updateDeposit(Long orderId, int deposit) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setDeposit(deposit);

            int total = order.getTotal();
            int newReminder = total - deposit;
            order.setReminder(newReminder);

            return orderRepository.save(order);
        }
        return null;
    }


    public void updateOrderTotal(Long orderId) {
        Order order = getOrderById(orderId);

        if (order != null) {
            int newTotal = calculateTotal(order);

            order.setTotal(newTotal);

            orderRepository.save(order);
        }
    }

    private int calculateTotal(Order order) {
        return order.getSizes().stream()
                .mapToInt(size -> size.getPrice() * size.getQuantity())
                .sum();
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

    public List<OrderDTO> getAllOrdersDTO() {
        List<Order> allOrders = orderRepository.findAllWithSizes();
        return mapOrdersToDTO(allOrders);
    }

    private List<OrderDTO> mapOrdersToDTO(List<Order> orders) {
        return orders.stream()
                .map(this::mapOrderToDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO mapOrderToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setCustomerName(order.getCustomerName());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setPhoneNumber(order.getPhoneNumber());
        orderDTO.setDate(String.valueOf(order.getDate()));
        orderDTO.setTotal((double) order.getTotal());
        orderDTO.setDeposit((double) order.getDeposit());
        orderDTO.setReminder((double) order.getReminder());
        orderDTO.setSizes(mapSizesToDTO(order.getSizes()));
        return orderDTO;
    }

    private List<SizeDTO> mapSizesToDTO(List<Sizes> sizes) {
        return sizes.stream()
                .map(this::mapSizeToDTO)
                .collect(Collectors.toList());
    }

    private SizeDTO mapSizeToDTO(Sizes size) {
        SizeDTO sizeDTO = new SizeDTO();
        sizeDTO.setId(size.getId());
        sizeDTO.setWidth(size.getWidth());
        sizeDTO.setHeight(size.getHeight());
        sizeDTO.setQuantity(size.getQuantity());
        sizeDTO.setPrice(size.getPrice());
        return sizeDTO;
    }

}
