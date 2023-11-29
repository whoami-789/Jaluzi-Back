package com.example.jaluzi.exceptions;

public class OrderNotFoundException extends Throwable {
    public OrderNotFoundException(String s) {
        System.out.println(s);
    }
}
