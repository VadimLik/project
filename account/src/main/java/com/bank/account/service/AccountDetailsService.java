package com.bank.account.service;

import com.bank.account.models.AccountDetails;

import java.util.List;

public interface AccountDetailsService {

    public List<AccountDetails> getAllAccountDetails();

    public AccountDetails getAccountDetails(long id);

    public AccountDetails createAccountDetails(AccountDetails accountDetails);

    public AccountDetails updateAccountDetails(AccountDetails accountDetails, long id);

    public void deleteAccountDetails(long id);

}
