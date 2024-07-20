package com.bank.account.service;

import com.bank.account.models.Audit;
import com.bank.account.repositories.AuditRepository;
import com.bank.account.exception.AuditNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuditServiceImp implements AuditService{

    private final AuditRepository auditRepository;

    @Autowired
    public AuditServiceImp(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }


    @Override
    public List<Audit> getAllAudits() {
        return (List<Audit>) auditRepository.findAll();
    }

    @Override
    public Audit getAuditById(long id) {

        return auditRepository.findById(id).orElseThrow(AuditNotFoundException::new);

    }

    @Override
    public void save(Audit audit) {

        if (audit != null){
            auditRepository.save(audit);
        }

    }
}
