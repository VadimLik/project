package com.bank.account.repositories;

import com.bank.account.models.Audit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditRepository extends CrudRepository<Audit, Long> {
}
