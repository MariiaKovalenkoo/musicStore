package com.storeproject.musicstore.interfaces;

import com.storeproject.musicstore.model.Product;

public interface IProductManagement {
    void editProduct(Product product) throws Exception;

    void addNewProduct(Product product);

    void deleteProduct();
}