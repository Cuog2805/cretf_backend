package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.StatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StatusService {
    public Page<StatusDTO> getStatusBySearch(StatusDTO statusDTO, Pageable pageable) throws Exception;
}
