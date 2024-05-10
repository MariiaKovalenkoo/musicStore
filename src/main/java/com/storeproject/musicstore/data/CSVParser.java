package com.storeproject.musicstore.data;

import com.storeproject.musicstore.model.Product;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {
    public List<Product> parseCSV(File csvFile) throws IOException {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String name = parts[0].trim();
                    String category = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    String description = parts[3].trim();
                    int stock = Integer.parseInt(parts[4].trim());

                    Product product = new Product(name, category, price, description, stock);
                    products.add(product);
                }
            }
        }
        return products;
    }
}