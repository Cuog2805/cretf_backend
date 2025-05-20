package com.cretf.backend.product.service;

import com.cretf.backend.product.dto.PublicFacilityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PublicFacilityService {
    public Page<PublicFacilityDTO> getPublicFacilityBySearch(PublicFacilityDTO publicFacilityDTO, Pageable pageable) throws Exception;
    public  PublicFacilityDTO create(PublicFacilityDTO publicFacilityDTO) throws Exception;
    public boolean lock(PublicFacilityDTO publicFacilityDTO) throws Exception;
    public boolean unlock(PublicFacilityDTO publicFacilityDTO) throws Exception;
    public List<PublicFacilityDTO> findByPropertyId(String propertyId) throws Exception;
}
