package com.bank.account.service;

import com.bank.account.models.AccountDetails;
import com.bank.account.repositories.AccountDetailsRepository;
import com.bank.account.exception.AccountDetailsNotCreatedException;
import com.bank.account.exception.AccountDetailsNotFoundException;
import com.bank.account.exception.NotEnoughMoneyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountDetailsServiceImp implements AccountDetailsService{

    private final AccountDetailsRepository accountDetailsRepository;

    @Autowired
    public AccountDetailsServiceImp(AccountDetailsRepository accountDetailsRepository) {

        this.accountDetailsRepository = accountDetailsRepository;

    }

    @Override
    public List<AccountDetails> getAllAccountDetails() {

        return (List<AccountDetails>) accountDetailsRepository.findAll();

    }

    @Override
    public AccountDetails getAccountDetails(long id) {

        return accountDetailsRepository.findById(id).orElseThrow(AccountDetailsNotFoundException::new);

    }

    @Transactional(readOnly = false)
    @Override
    public AccountDetails createAccountDetails(AccountDetails accountDetails) {

        BigDecimal minMoney = BigDecimal.valueOf(10.00);
        if (accountDetails.getMoney().compareTo(minMoney) == -1) {
            throw new AccountDetailsNotCreatedException("Не достаточно средств для открытия счета, минимальная сумма - 10,00 рублей");
        }
        AccountDetails result = accountDetailsRepository.save(accountDetails);
        return result;

    }

    @Transactional(readOnly = false)
    @Override
    public AccountDetails updateAccountDetails(AccountDetails accountDetails, long id) {

        AccountDetails updateAccountDetails = accountDetailsRepository.findById(id).orElseThrow(AccountDetailsNotFoundException::new);
        if (updateAccountDetails != null) {
            updateAccountDetails.setPassportId(accountDetails.getPassportId());
            updateAccountDetails.setAccountNumber(accountDetails.getAccountNumber());
            updateAccountDetails.setBankDetailsId(accountDetails.getBankDetailsId());
            updateAccountDetails.setProfileId(accountDetails.getProfileId());
            if (accountDetails.isNegativeBalance()) {
                if (updateAccountDetails.getMoney().compareTo(accountDetails.getMoney()) == -1){
                    throw new NotEnoughMoneyException("Не достаточно средств на счете");
                }else
                    updateAccountDetails.setMoney(updateAccountDetails.getMoney().subtract(accountDetails.getMoney()));
            } else {
                updateAccountDetails.setMoney(updateAccountDetails.getMoney().add(accountDetails.getMoney()));
            }
            updateAccountDetails.setNegativeBalance(accountDetails.isNegativeBalance());
        }
        return updateAccountDetails;

    }

    @Transactional(readOnly = false)
    @Override
    public void deleteAccountDetails(long id) {

        accountDetailsRepository.deleteById(id);

    }
}
