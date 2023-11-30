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
import java.math.BigDecimal;
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
                "Завершен", "Завершен в мастерской", "Итого", "Залог", "Остаток", "Примечание"};

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
        String[] sizesHeaders = {"ID", "Ширина", "Высота", "Квадрат", "Цена", "Количество", "Итого", "Примечание"};

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
            sizeDataRow.createCell(3).setCellValue(size.getSquare());
            sizeDataRow.createCell(4).setCellValue(size.getPrice());
            sizeDataRow.createCell(5).setCellValue(size.getQuantity());
            sizeDataRow.createCell(6).setCellValue(size.getTotal());
            sizeDataRow.createCell(7).setCellValue(size.getNote());

            // Применение стилей данных
            for (int i = 0; i < sizesHeaders.length; i++) {
                sizeDataRow.getCell(i).setCellStyle(dataStyle);
            }
        }
    }

    public void generateWorkshopOrderReport(Long orderId, String filePath) throws IOException {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Workbook workbook = new XSSFWorkbook();

            // Лист для основной информации о заказе
            Sheet orderSheet = workbook.createSheet("Заказ");
            fillWorkshopOrderSheet(orderSheet, order, workbook);

            // Лист для информации о размерах
            Sheet sizesSheet = workbook.createSheet("Размеры");
            fillWorkshopSizesSheet(sizesSheet, order.getSizes(), workbook);

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

    private void fillWorkshopOrderSheet(Sheet orderSheet, Order order, Workbook workbook) {
        String[] orderHeaders = {"ID", "ФИО", "Дата", "Адрес", "Номер телефона",
                "Завершен в мастерской", "Примечение"};

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
        orderDataRow.createCell(5).setCellValue(order.isWorkshopCompleted());
        orderDataRow.createCell(6).setCellValue(order.getNote());
    }

    private void fillWorkshopSizesSheet(Sheet sizesSheet, List<Sizes> sizes, Workbook workbook) {
        String[] sizesHeaders = {"ID", "Ширина", "Высота", "Квадрат", "Количество", "Итого", "Примечание"};

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
            sizeDataRow.createCell(3).setCellValue(size.getSquare());
            sizeDataRow.createCell(4).setCellValue(size.getQuantity());
            sizeDataRow.createCell(5).setCellValue(size.getTotal());
            sizeDataRow.createCell(6).setCellValue(size.getNote());

            // Применение стилей данных
            for (int i = 0; i < sizesHeaders.length; i++) {
                sizeDataRow.getCell(i).setCellStyle(dataStyle);
            }
        }
    }

    public void generateMonthlyOrderReport(List<Order> orders, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // Группировка заказов по месяцам
        Map<String, List<Order>> ordersByMonth = groupOrdersByMonth(orders);

        for (Map.Entry<String, List<Order>> entry : ordersByMonth.entrySet()) {
            String monthYear = entry.getKey();
            List<Order> ordersForMonth = entry.getValue();

            // Создаем лист для каждого месяца
            Sheet monthSheet = workbook.createSheet(monthYear);
            CellStyle headerStyle = createHeaderStyle(workbook);

            int rowIndex = 0;

            for (Order order : ordersForMonth) {
                // Заполняем лист информацией о заказе
                rowIndex = fillOrderSheet(monthSheet, order, headerStyle, rowIndex);

                // Заполняем лист информацией о размерах
                rowIndex = fillSizesSheet(monthSheet, order.getSizes(), headerStyle, rowIndex);
            }

            // После того, как заполнены заказы и размеры, добавляем Total
            addTotalRow(monthSheet, monthYear, headerStyle, rowIndex);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }



    private void addTotalRow(Sheet sheet, String monthYear, CellStyle headerStyle, int rowIndex) {
        // Создаем строку для подсчета Total
        Row totalRow = sheet.createRow(rowIndex);
        Cell totalCell = totalRow.createCell(0);
        totalCell.setCellValue("Итого");
        totalCell.setCellStyle(headerStyle);

        // Получаем сумму Total заказов за месяц из базы данных
        double totalAmount = getTotalAmountForMonth(monthYear); // Подстраивайте метод под вашу логику

        // Создаем ячейку для записи суммы Total заказов за месяц
        Cell totalValueCell = totalRow.createCell(7);
        totalValueCell.setCellValue(totalAmount);

        // Возвращаем индекс следующей строки после подсчета Total
    }

    public double getTotalAmountForMonth(String monthYear) {
        // Разбиваем строку monthYear на месяц и год
        String[] parts = monthYear.split(" ");
        String month = parts[0];
        int year = Integer.parseInt(parts[1]);

        // Получаем заказы для указанного месяца и года
        List<Order> orders = orderRepository.findByMonthAndYear(getMonthNumber(month), year);

        // Вычисляем сумму Total заказов
        return orders.stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }


    // Вспомогательный метод для получения номера месяца по его названию
    private int getMonthNumber(String month) {
        return switch (month) {
            case "JANUARY" -> 1;
            case "FEBRUARY" -> 2;
            case "MARCH" -> 3;
            case "APRIL" -> 4;
            case "MAY" -> 5;
            case "JUNE" -> 6;
            case "JULY" -> 7;
            case "AUGUST" -> 8;
            case "SEPTEMBER" -> 9;
            case "OCTOBER" -> 10;
            case "NOVEMBER" -> 11;
            case "DECEMBER" -> 12;
            default -> throw new IllegalArgumentException("Неверное название месяца: " + month);
        };
    }

    private int fillSizesSheet(Sheet sizesSheet, List<Sizes> sizes, CellStyle headerStyle, int rowIndex) {
        String[] sizesHeaders = {"ID", "Ширина", "Высота", "Квадрат", "Цена", "Количество", "Итого", "Примечание"};

        // Используйте rowIndex для создания строк
        Row sizesHeaderRow = sizesSheet.createRow(rowIndex);
        for (int i = 0; i < sizesHeaders.length; i++) {
            Cell cell = sizesHeaderRow.createCell(i + 2);
            cell.setCellValue(sizesHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        int sizesRowIndex = rowIndex + 1;
        for (Sizes size : sizes) {
            Row sizeDataRow = sizesSheet.createRow(sizesRowIndex++);
            sizeDataRow.createCell(2).setCellValue(size.getId());
            sizeDataRow.createCell(3).setCellValue(size.getWidth());
            sizeDataRow.createCell(4).setCellValue(size.getHeight());
            sizeDataRow.createCell(5).setCellValue(size.getSquare());
            sizeDataRow.createCell(6).setCellValue(size.getPrice());
            sizeDataRow.createCell(7).setCellValue(size.getQuantity());
            sizeDataRow.createCell(8).setCellValue(size.getTotal());
            sizeDataRow.createCell(9).setCellValue(size.getNote());
        }

        return sizesRowIndex + 1; // Возвращаем индекс следующей строки после размеров
    }

    private Map<String, List<Order>> groupOrdersByMonth(List<Order> orders) {
        // Группировка заказов по месяцам
        Map<String, List<Order>> ordersByMonth = new TreeMap<>();
        for (Order order : orders) {
            String monthYear = getSheetName(order);
            ordersByMonth.computeIfAbsent(monthYear, k -> new ArrayList<>()).add(order);
        }
        return ordersByMonth;
    }

    private String getSheetName(Order order) {
        // Возвращает имя листа в формате "Месяц Год"
        // Например, "Ноябрь 2023"
        // Вам может потребоваться настроить форматирование даты в соответствии с вашими требованиями
        return order.getDate().getMonth().toString() + " " + order.getDate().getYear();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setBorderBottom(workbook.createCellStyle().getBorderBottom());
        style.setBorderTop(workbook.createCellStyle().getBorderTop());
        style.setBorderLeft(workbook.createCellStyle().getBorderLeft());
        style.setBorderRight(workbook.createCellStyle().getBorderRight());
        return style;
    }


    private int fillOrderSheet(Sheet orderSheet, Order order, CellStyle headerStyle, int rowIndex) {
        String[] orderHeaders = {"ID", "ФИО", "Дата", "Адрес", "Номер телефона",
                "Завершен", "Завершен в мастерской", "Итого", "Залог", "Остаток", "Примечание"};

        // Используйте rowIndex для создания строк
        Row orderHeaderRow = orderSheet.createRow(rowIndex);
        for (int i = 0; i < orderHeaders.length; i++) {
            Cell cell = orderHeaderRow.createCell(i);
            cell.setCellValue(orderHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        Row orderDataRow = orderSheet.createRow(rowIndex + 1);
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

        // Установим автоматическое подгонку ширины столбцов
        for (int i = 0; i < orderHeaders.length; i++) {
            orderSheet.autoSizeColumn(i);
        }


        // Возвращаем индекс следующей строки после данных о заказе
        return rowIndex + 3;
    }


}
