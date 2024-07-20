package com.bank.account.validators;

import com.bank.account.models.AccountDetails;
import com.bank.account.repositories.AccountDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class AccountDetailsValidator implements Validator {

    private final AccountDetailsRepository accountDetailsRepository;

    @Autowired
    public AccountDetailsValidator(AccountDetailsRepository accountDetailsRepository) {

        this.accountDetailsRepository = accountDetailsRepository;

    }

    @Override
    public boolean supports(Class<?> clazz) {

        return false;

    }

    @Override
    public void validate(Object object, Errors errors) {

        AccountDetails accountDetails = (AccountDetails) object;
        Optional<AccountDetails> optionalAccountDetails = accountDetailsRepository.findByAccountNumber(accountDetails.getAccountNumber());
        if(optionalAccountDetails.isPresent() && optionalAccountDetails.get().getId() != accountDetails.getId()) {
            errors.rejectValue("accountNumber", "", "Этот номер счета уже существует ");

        }
    }
}
