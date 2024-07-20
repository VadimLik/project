package com.bank.account.repositories;

import com.bank.account.models.AccountDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDetailsRepository extends CrudRepository<AccountDetails, Long> {

    Optional<AccountDetails> findByAccountNumber(Long accountNumber);

    Optional<AccountDetails> findByBankDetailsId(Long bankDetailsId);
}
