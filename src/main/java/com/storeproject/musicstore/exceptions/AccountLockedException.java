package com.storeproject.musicstore.exceptions;

public class AccountLockedException extends Exception{
    public AccountLockedException(String message) {
        super(message);
    }
}