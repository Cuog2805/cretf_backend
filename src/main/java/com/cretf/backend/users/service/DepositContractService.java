package com.cretf.backend.users.service;

import com.cretf.backend.users.dto.DepositContractDTO;

import java.util.List;

public interface DepositContractService {
    public DepositContractDTO create(DepositContractDTO request) throws Exception;
    public List<DepositContractDTO> getAll() throws Exception;
    public DepositContractDTO getById(String templateId);
    public List<DepositContractDTO> search(String keyword);
    public boolean delete(String templateId) throws Exception;
}
