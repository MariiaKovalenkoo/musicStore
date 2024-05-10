package com.storeproject.musicstore.data;

import java.io.Serializable;

public class ProductIdGenerator implements Serializable {
    private int nextProductId = 1;

    public String generateProductId() {
        String productId = String.format("PROD%03d", nextProductId);
        nextProductId++;
        return productId;
    }
}
/*  % : It indicates the start of a format specifier.
        0 : It specifies that the number should be padded with leading zeros.
        3 : It specifies the minimum width of the resulting string. In this case, it's set to 3 characters wide.
        d : It indicates that the argument to be formatted is an integer.
    */