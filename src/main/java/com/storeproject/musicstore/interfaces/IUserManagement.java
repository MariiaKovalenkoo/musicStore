package com.storeproject.musicstore.interfaces;

public interface IUserManagement {
    void login(String username, String password);

    void logout();

    boolean validateUser(String username, String password);
}