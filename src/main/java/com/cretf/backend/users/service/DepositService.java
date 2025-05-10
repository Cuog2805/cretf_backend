package com.cretf.backend.users.service;

import com.cretf.backend.users.dto.DepositDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DepositService {
    Page<DepositDTO> getDepositsBySearch(DepositDTO depositsDTO, Pageable pageable) throws Exception;
    DepositDTO getDepositsById(String id) throws Exception;
    DepositDTO getDepositsByPropertyId(String propertyId) throws Exception;
}
