package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.ScaleDTO;
import com.cretf.backend.product.dto.StatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScaleService {
    public Page<ScaleDTO> getScaleBySearch(ScaleDTO scaleDTO, Pageable pageable) throws Exception;
}
