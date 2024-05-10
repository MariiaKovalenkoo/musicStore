package com.storeproject.musicstore.data;

import java.io.Serializable;

public class OrderIdGenerator implements Serializable {
    private int nextOrderId = 1;

    public String generateOrderId() {
        String orderId = String.format("ORDER%03d", nextOrderId);
        nextOrderId++;
        return orderId;
    }
}