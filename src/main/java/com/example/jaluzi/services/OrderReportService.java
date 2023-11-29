package com.example.jaluzi.services;

import com.example.jaluzi.exceptions.OrderNotFoundException;
import com.example.jaluzi.models.Order;
import com.example.jaluzi.models.Sizes;
import com.example.jaluzi.repositories.OrderRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class OrderReportService {

    private final OrderRepository orderRepository;

    public OrderReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void generateOrderReport(Long orderId, String filePath) throws IOException {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Workbook workbook = new XSSFWorkbook();

            // Лист для основной информации о заказе
            Sheet orderSheet = workbook.createSheet("Заказ");
            fillOrderSheet(orderSheet, order, workbook);

            // Лист для информации о размерах
            Sheet sizesSheet = workbook.createSheet("Размеры");
            fillSizesSheet(sizesSheet, order.getSizes(), workbook);

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                orderSheet.autoSizeColumn(0);
                sizesSheet.autoSizeColumn(0);
                workbook.write(fileOut);
            }

            workbook.close();
        } else {
            try {
                throw new OrderNotFoundException("Order not found with id: " + orderId);
            } catch (OrderNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void fillOrderSheet(Sheet orderSheet, Order order, Workbook workbook) {
        String[] orderHeaders = {"ID", "ФИО", "Дата", "Адрес", "Номер телефона",
                "Завершен", "Завершен в мастерской", "Итого", "Залог", "Остаток", "Примечение"};

        // Стили для заголовка
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.BRICKS);
        headerStyle.setAlignment(HorizontalAlignment.FILL);

        Row orderHeaderRow = orderSheet.createRow(0);
        for (int i = 0; i < orderHeaders.length; i++) {
            Cell cell = orderHeaderRow.createCell(i);
            cell.setCellValue(orderHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        Row orderDataRow = orderSheet.createRow(1);
        orderDataRow.createCell(0).setCellValue(order.getId());
        orderDataRow.createCell(1).setCellValue(order.getCustomerName());
        orderDataRow.createCell(2).setCellValue(order.getDate().toString());
        orderDataRow.createCell(3).setCellValue(order.getAddress());
        orderDataRow.createCell(4).setCellValue(order.getPhoneNumber());
        orderDataRow.createCell(5).setCellValue(order.isCompleted());
        orderDataRow.createCell(6).setCellValue(order.isWorkshopCompleted());
        orderDataRow.createCell(7).setCellValue(order.getTotal());
        orderDataRow.createCell(8).setCellValue(order.getDeposit());
        orderDataRow.createCell(9).setCellValue(order.getReminder());
        orderDataRow.createCell(10).setCellValue(order.getNote());
    }

    private void fillSizesSheet(Sheet sizesSheet, List<Sizes> sizes, Workbook workbook) {
        String[] sizesHeaders = {"ID", "Ширина", "Высота", "Цена", "Количество", "Итого", "Примечание"};

        // Стили для заголовка
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.BRICKS);
        headerStyle.setAlignment(HorizontalAlignment.FILL);

        Row sizesHeaderRow = sizesSheet.createRow(0);
        for (int i = 0; i < sizesHeaders.length; i++) {
            Cell cell = sizesHeaderRow.createCell(i);
            cell.setCellValue(sizesHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        // Стили для данных
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);

        int sizesRowIndex = 1;
        for (Sizes size : sizes) {
            Row sizeDataRow = sizesSheet.createRow(sizesRowIndex++);
            sizeDataRow.createCell(0).setCellValue(size.getId());
            sizeDataRow.createCell(1).setCellValue(size.getWidth());
            sizeDataRow.createCell(2).setCellValue(size.getHeight());
            sizeDataRow.createCell(3).setCellValue(size.getPrice());
            sizeDataRow.createCell(4).setCellValue(size.getQuantity());
            sizeDataRow.createCell(5).setCellValue(size.getTotal());
            sizeDataRow.createCell(6).setCellValue(size.getNote());

            // Применение стилей данных
            for (int i = 0; i < sizesHeaders.length; i++) {
                sizeDataRow.getCell(i).setCellStyle(dataStyle);
            }
        }
    }
}
