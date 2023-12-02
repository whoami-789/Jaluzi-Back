package com.example.jaluzi.services;

import com.example.jaluzi.dto.OrderRequestDTO;
import com.example.jaluzi.dto.OrderResponseDTO;
import com.example.jaluzi.dto.SizesRequestDTO;
import com.example.jaluzi.dto.SizesResponseDTO;
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
        order.setWorkshopCompleted(false);
        return orderRepository.save(order);
    }

    public void updateCompleted(Long orderId, boolean completed) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setCompleted(completed);
            orderRepository.save(order);
        }
    }

    public void updateWorkshopCompleted(Long orderId, boolean workshopCompleted) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setWorkshopCompleted(workshopCompleted);
            orderRepository.save(order);
        }
    }

    public void updateNote(Long orderId, String note) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setNote(note);
            orderRepository.save(order);
        }
    }

    public void updateDeposit(Long orderId, double deposit) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setDeposit(deposit);

            double total = order.getTotal();
            double newReminder = total - deposit;
            order.setReminder(newReminder);

            orderRepository.save(order);
        }
    }


    public void updateOrderTotal(Long orderId) {
        Order order = getOrderById(orderId);

        if (order != null) {
            double newTotal = calculateTotal(order);

            order.setTotal(newTotal);

            orderRepository.save(order);
        }
    }

    private double calculateTotal(Order order) {
        return order.getSizes().stream()
                .mapToDouble(size -> size.getPrice() * size.getQuantity())
                .sum();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    public OrderResponseDTO getOrderResponseById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        return order != null ? convertToOrderResponseDTO(order) : null;
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        List<SizesResponseDTO> sizesResponseDTOList = order.getSizes().stream()
                .map(this::convertToSizesResponseDTO)
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerName(),
                order.getAddress(),
                order.getPhoneNumber(),
                order.getDate().toString(),
                order.getTotal(),
                order.getDeposit(),
                order.getReminder(),
                order.getNote(),
                order.isCompleted(),
                order.isWorkshopCompleted(),
                sizesResponseDTOList
        );
    }

    private SizesResponseDTO convertToSizesResponseDTO(Sizes sizes) {
        return new SizesResponseDTO(
                sizes.getId(),
                sizes.getName(),
                sizes.getWidth(),
                sizes.getHeight(),
                sizes.getSquare(),
                sizes.getPrice(),
                sizes.getQuantity(),
                sizes.getTotal(),
                sizes.getNote()
        );
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<OrderRequestDTO> getAllOrdersDTO() {
        List<Order> allOrders = orderRepository.findAllWithSizes();
        return mapOrdersToDTO(allOrders);
    }


    private List<OrderRequestDTO> mapOrdersToDTO(List<Order> orders) {
        return orders.stream()
                .map(this::mapOrderToDTO)
                .collect(Collectors.toList());
    }

    private OrderRequestDTO mapOrderToDTO(Order order) {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setKey(order.getId());
        orderRequestDTO.setCustomerName(order.getCustomerName());
        orderRequestDTO.setAddress(order.getAddress());
        orderRequestDTO.setPhoneNumber(order.getPhoneNumber());
        orderRequestDTO.setDate(String.valueOf(order.getDate()));
        orderRequestDTO.setTotal(order.getTotal());
        orderRequestDTO.setDeposit(order.getDeposit());
        orderRequestDTO.setReminder(order.getReminder());
        orderRequestDTO.setNote(order.getNote());
        orderRequestDTO.setSizes(mapSizesToDTO(order.getSizes()));
        return orderRequestDTO;
    }

    private List<SizesRequestDTO> mapSizesToDTO(List<Sizes> sizes) {
        return sizes.stream()
                .map(this::mapSizeToDTO)
                .collect(Collectors.toList());
    }

    private SizesRequestDTO mapSizeToDTO(Sizes size) {
        SizesRequestDTO sizesRequestDTO = new SizesRequestDTO();
        sizesRequestDTO.setKey(size.getId());
        sizesRequestDTO.setName(size.getName());
        sizesRequestDTO.setWidth(size.getWidth());
        sizesRequestDTO.setHeight(size.getHeight());
        sizesRequestDTO.setQuantity(size.getQuantity());
        sizesRequestDTO.setSquare(size.getSquare());
        sizesRequestDTO.setTotal(size.getTotal());
        sizesRequestDTO.setNote(size.getNote());
        sizesRequestDTO.setPrice(size.getPrice());
        return sizesRequestDTO;
    }

}
