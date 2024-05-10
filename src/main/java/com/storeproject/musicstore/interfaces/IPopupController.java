package com.storeproject.musicstore.interfaces;

public interface IPopupController {
    void confirmAction();

    void cancelAction();

    void setParentController(IController parentController);

    void showError(String errorMessage);
}