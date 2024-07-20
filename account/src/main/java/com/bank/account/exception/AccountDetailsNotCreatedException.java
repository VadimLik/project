package com.bank.account.exception;

public class AccountDetailsNotCreatedException extends RuntimeException{

    public AccountDetailsNotCreatedException(String message) {
        super(message);
    }
}
