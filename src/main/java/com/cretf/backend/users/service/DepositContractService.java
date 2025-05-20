package com.cretf.backend.users.service;

import com.cretf.backend.users.dto.DepositContractDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepositContractService {
    public DepositContractDTO create(DepositContractDTO request) throws Exception;
    public List<DepositContractDTO> getAll() throws Exception;
    public DepositContractDTO getById(String templateId);
    public List<DepositContractDTO> search(String keyword);
    public boolean delete(String templateId) throws Exception;
    public Page<DepositContractDTO> getDepoistContractBySearch(DepositContractDTO depositContractDTO, Pageable pageable) throws Exception;
    public boolean comfirm(DepositContractDTO depositContractDTO) throws Exception;
    public boolean reject(DepositContractDTO depositContractDTO) throws Exception;
}
