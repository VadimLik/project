package com.bank.account.exception;

public class AccountDetailsErrorResponce extends RuntimeException{

    public AccountDetailsErrorResponce (String message) {
        super(message);
    }
}
