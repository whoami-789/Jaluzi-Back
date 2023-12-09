package com.example.jaluzi.controllers;

import com.example.jaluzi.models.Order;
import com.example.jaluzi.services.OrderReportService;
import com.example.jaluzi.services.OrderService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final OrderReportService orderReportService;
    private final OrderService orderService;// Inject your OrderService

    public ReportController(OrderReportService orderReportService, OrderService orderService) {
        this.orderReportService = orderReportService;
        this.orderService = orderService;
    }


    @GetMapping("/monthly-report")
    public ResponseEntity<Resource> generateMonthlyReport() {
        try {
            String folderPath = "/home/reports/month/";
            List<Order> allOrders = orderService.getAllOrders();

            // Создайте абсолютный путь к файлу отчета
            String fileName = "monthly_report" + ".xlsx";
            String filePath = Paths.get(folderPath, fileName).toString();

            // Генерируйте отчет
            orderReportService.generateMonthlyOrderReport(allOrders, filePath);

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
