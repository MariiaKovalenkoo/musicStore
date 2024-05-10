package com.storeproject.musicstore.interfaces;

import com.storeproject.musicstore.model.OrderedProduct;

public interface IOrderManagement {
    void addProductToOrder(OrderedProduct product);

    void createOrder();

    void deleteProductFromOrder();
}