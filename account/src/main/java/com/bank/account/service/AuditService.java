package com.bank.account.service;

import com.bank.account.models.Audit;

import java.util.List;

public interface AuditService {

    public List<Audit> getAllAudits();

    public Audit getAuditById(long id);

    public void save(Audit audit);
}
