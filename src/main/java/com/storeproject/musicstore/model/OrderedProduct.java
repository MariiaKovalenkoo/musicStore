package com.storeproject.musicstore.model;

import java.io.Serializable;

public class OrderedProduct implements Serializable {
    private Product product;

    private int quantity;

    public OrderedProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return product.getName();
    }

    public String getProductCategory() {
        return product.getCategory();
    }

    public double getProductPrice() {
        return product.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}