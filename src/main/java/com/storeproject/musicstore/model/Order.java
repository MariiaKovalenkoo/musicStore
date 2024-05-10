package com.storeproject.musicstore.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private List<OrderedProduct> orderedProducts;

    private Customer customer;

    private double totalPrice;

    private LocalDateTime orderDateTime;

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public Order() {
        orderedProducts = new ArrayList<>();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setCustomer(Customer customer){

        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setOrderedProducts(List<OrderedProduct> orderedProducts) {

        this.orderedProducts = orderedProducts;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }
}