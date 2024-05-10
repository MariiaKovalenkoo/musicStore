package com.storeproject.musicstore.data;

import com.storeproject.musicstore.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Database implements Serializable{
    private ProductIdGenerator productIdGenerator;

    private OrderIdGenerator orderIdGenerator;

    public void createDefaultData(){
        productIdGenerator = new ProductIdGenerator();
        orderIdGenerator = new OrderIdGenerator();
        orderHistory = new LinkedHashMap<>();
        createUsers();
        createProductsInventory();
    }

    private List<User> users;

    private List<Product> productsInventory;

    private Map<String, Order> orderHistory;

    // users
    public List<User> getUsers(){
        return users;
    }

    // inventory
    public List<Product> getProductsInventory() {
        return productsInventory;
    }

    public List<Product> getProductsInStock() {
        return productsInventory.stream()
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
    }


    public void addNewProduct(Product product){
        product.setProductId(productIdGenerator.generateProductId());
        productsInventory.add(product);
    }

    public void deleteProduct(Product product){
        productsInventory.remove(product);
    }

    public void editProduct(Product editedProduct) throws Exception {
        int index = getProductIndex(editedProduct.getProductId());
        productsInventory.set(index, editedProduct);
    }

    private int getProductIndex(String productId) throws Exception {
        for (Product product : productsInventory) {
            if (product.getProductId().equals(productId)) {
                return productsInventory.indexOf(product);
            }
        }
        throw new Exception("product not found");
    }

    // history
    public Map<String, Order> getOrderHistory() {
        return orderHistory;
    }

    // creating order
    public void createOrder(Order order) {
        String orderNumber = orderIdGenerator.generateOrderId();
        orderHistory.put(orderNumber, order);
    }

    // initialize the database
    private void createUsers(){
        users = new ArrayList<>();
        users.add(new User("markL", "mark1234!", "Mark","Lee", User.Role.SALES));
        users.add(new User("aliceS", "alice1234!", "Alice", "Smith", User.Role.MANAGER));
        users.add(new User("davidBr", "user3456!", "David", "Brown", User.Role.MANAGER));
        users.add(new User("BobJn", "user4536!","Bob", "Johnson", User.Role.SALES));
    }

    private void createProductsInventory(){
        productsInventory = new ArrayList<>();
        productsInventory.add(0, new Product(productIdGenerator.generateProductId(),"Flute", "Woodwind Instruments", 249.99, "Student flute with case", 3));
        productsInventory.add(1, new Product(productIdGenerator.generateProductId(),"Acoustic Guitar", "Guitars", 299.99, "High-quality acoustic guitar", 10));
        productsInventory.add(2, new Product(productIdGenerator.generateProductId(),"Electric Keyboard", "Keyboards", 499.99, "61-key electric keyboard", 5));
        productsInventory.add(3, new Product(productIdGenerator.generateProductId(),"Drum Set", "Drums & Percussion", 799.99, "Complete drum set with cymbals", 3));
        productsInventory.add(4, new Product(productIdGenerator.generateProductId(),"Alto Saxophone", "Woodwind Instruments", 599.99, "Professional alto saxophone", 2));
        productsInventory.add(5, new Product(productIdGenerator.generateProductId(),"Violin", "String Instruments", 199.99, "Student-level violin", 7));
        productsInventory.add(6, new Product(productIdGenerator.generateProductId(),"Microphone", "Audio Equipment", 49.99, "Dynamic vocal microphone", 15));
        productsInventory.add(7, new Product(productIdGenerator.generateProductId(),"Trumpet", "Brass Instruments", 349.99, "Intermediate trumpet with case", 4));
        productsInventory.add(8, new Product(productIdGenerator.generateProductId(),"Digital Piano", "Keyboards", 699.99, "88-key digital piano with stand", 6));
        productsInventory.add(9, new Product(productIdGenerator.generateProductId(),"Bass Guitar", "Guitars", 399.99, "4-string electric bass guitar", 8));
    }

    // serialization
    public void serializeData() {
        try {
            FileOutputStream fileOut = new FileOutputStream("database.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in database.ser");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deserializeData(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Database loadedDatabase = (Database) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Serialized data is loaded from " + filename);

            // Copy the loaded data into the current instance
            this.users = loadedDatabase.users;
            this.productsInventory = loadedDatabase.productsInventory;
            this.orderHistory = loadedDatabase.orderHistory;
            this.orderIdGenerator = loadedDatabase.orderIdGenerator;
            this.productIdGenerator = loadedDatabase.productIdGenerator;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}