package com.storeproject.musicstore.interfaces;

import com.storeproject.musicstore.data.Database;

public interface IController {
    void displayView();

    void setDatabase(Database database);

    void showError(String errorMessage);
}