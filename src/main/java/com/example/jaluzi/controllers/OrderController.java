package com.example.jaluzi.controllers;

import com.example.jaluzi.dto.OrderRequestDTO;
import com.example.jaluzi.dto.OrderResponseDTO;
import com.example.jaluzi.models.Order;
import com.example.jaluzi.services.OrderReportService;
import com.example.jaluzi.services.OrderService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    private final OrderService orderService;
    private final OrderReportService orderReportService;

    public OrderController(OrderService orderService, OrderReportService orderReportService) {
        this.orderService = orderService;
        this.orderReportService = orderReportService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.saveOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/completed")
    public ResponseEntity<OrderResponseDTO> updateCompleted(@PathVariable Long orderId, @RequestBody Map<String, Boolean> requestBody) {
        boolean completed = requestBody.get("completed");
        orderService.updateCompleted(orderId, completed);
        OrderResponseDTO orderResponseDTO = orderService.getOrderResponseById(orderId);
        return orderResponseDTO != null
                ? new ResponseEntity<>(orderResponseDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{orderId}/workshop-completed")
    public ResponseEntity<OrderResponseDTO> updateWorkshopCompleted(@PathVariable Long orderId, @RequestBody Map<String, Boolean> requestBody) {
        boolean completed = requestBody.get("completed");
        orderService.updateWorkshopCompleted(orderId, completed);
        OrderResponseDTO orderResponseDTO = orderService.getOrderResponseById(orderId);
        return orderResponseDTO != null
                ? new ResponseEntity<>(orderResponseDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("/{orderId}/deposit")
    public ResponseEntity<OrderResponseDTO> updateDeposit(@PathVariable Long orderId, @RequestBody Map<String, Integer> requestBody) {
        double deposit = requestBody.get("deposit");
        orderService.updateDeposit(orderId, deposit);
        OrderResponseDTO orderResponseDTO = orderService.getOrderResponseById(orderId);
        return orderResponseDTO != null
                ? new ResponseEntity<>(orderResponseDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{orderId}/note")
    public ResponseEntity<OrderResponseDTO> updateNote(@PathVariable Long orderId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        orderService.updateNote(orderId, note);
        OrderResponseDTO orderResponseDTO = orderService.getOrderResponseById(orderId);
        return orderResponseDTO != null
                ? new ResponseEntity<>(orderResponseDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = orderService.getOrderResponseById(id);
        return orderResponseDTO != null
                ? new ResponseEntity<>(orderResponseDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<OrderRequestDTO>> getAllOrdersWithSizes() {
        List<OrderRequestDTO> allOrdersDTO = orderService.getAllOrdersDTO();
        return new ResponseEntity<>(allOrdersDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{orderId}/generate-report")
    public ResponseEntity<Resource> generateOrderReport(@PathVariable Long orderId) {
        try {
            String folderPath = "/home/reports/order/";

            // Создайте абсолютный путь к файлу отчета
            String fileName = "report_" + orderId + ".xlsx";
            String filePath = Paths.get(folderPath, fileName).toString();

            // Генерируйте отчет
            orderReportService.generateOrderReport(orderId, filePath);

            // Подготовьте файл для отправки в ответе
            FileSystemResource resource = new FileSystemResource(filePath);

            // Настройте заголовки ответа
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // Верните ResponseEntity с файлом и заголовками
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.getFile().length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FileSystemResource("Error generating order report: " + e.getMessage()));
        }
    }
@GetMapping("/{orderId}/generate-workshop-report")
    public ResponseEntity<Resource> generateWorkshopOrderReport(@PathVariable Long orderId) {
        try {
            String folderPath = "/home/reports/workshop/";

            // Создайте абсолютный путь к файлу отчета
            String fileName = "report_" + orderId + ".xlsx";
            String filePath = Paths.get(folderPath, fileName).toString();

            // Генерируйте отчет
            orderReportService.generateWorkshopOrderReport(orderId, filePath);

            // Подготовьте файл для отправки в ответе
            FileSystemResource resource = new FileSystemResource(filePath);

            // Настройте заголовки ответа
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // Верните ResponseEntity с файлом и заголовками
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.getFile().length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FileSystemResource("Error generating order report: " + e.getMessage()));
        }
    }
}
