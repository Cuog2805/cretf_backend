package com.cretf.backend.users.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.users.dto.DashBoardDTO;
import com.cretf.backend.users.service.DashBoardService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class DashBoardServiceImpl extends BaseJdbcServiceImpl<DashBoardDTO, String> implements DashBoardService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/DashBoard";

    public DashBoardServiceImpl(EntityManager entityManager) {
        super(entityManager, DashBoardDTO.class);
    }
    @Override
    public List<DashBoardDTO> getSummaryPriceRange(DashBoardDTO dashBoardDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getSummaryPriceRange", FILE_EXTENSION, FILE_PATH_NAME);

        Map<String, Object> params = new HashMap<>();
        params.put("propertyType", dashBoardDTO.getType());

        List<DashBoardDTO> dashBoardDTOs = (List<DashBoardDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, DashBoardDTO.class);
        return dashBoardDTOs;
    }
    @Override
    public List<DashBoardDTO> getSummaryTransaction(DashBoardDTO dashBoardDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getSummaryTransaction", FILE_EXTENSION, FILE_PATH_NAME);

        Map<String, Object> params = new HashMap<>();
        if(StringUtil.isNullOrEmpty(dashBoardDTO.getType())){
            params.put("propertyType", "SOLD");
        }
        else{
            params.put("propertyType", dashBoardDTO.getType());
        }

        List<DashBoardDTO> dashBoardDTOs = (List<DashBoardDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, DashBoardDTO.class);
        return dashBoardDTOs;
    }
    @Override
    public List<DashBoardDTO> getSummaryPriceAvarageLocation(DashBoardDTO dashBoardDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getSummaryPriceAvarageLocation", FILE_EXTENSION, FILE_PATH_NAME);

        Map<String, Object> params = new HashMap<>();
        if(StringUtil.isNullOrEmpty(dashBoardDTO.getType())){
            params.put("propertyType", "SOLD");
        }
        else{
            params.put("propertyType", dashBoardDTO.getType());
        }

        List<DashBoardDTO> dashBoardDTOs = (List<DashBoardDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, DashBoardDTO.class);
        return dashBoardDTOs;
    }
    @Override
    public List<DashBoardDTO> getPropertyTypeStatic(DashBoardDTO dashBoardDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getPropertyTypeStatic", FILE_EXTENSION, FILE_PATH_NAME);

        Map<String, Object> params = new HashMap<>();
        if(StringUtil.isNullOrEmpty(dashBoardDTO.getType())){
            params.put("propertyType", "SOLD");
        }
        else{
            params.put("propertyType", dashBoardDTO.getType());
        }

        List<DashBoardDTO> dashBoardDTOs = (List<DashBoardDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, DashBoardDTO.class);
        return dashBoardDTOs;
    }
    @Override
    public List<DashBoardDTO> getPriceTrendOverTime(DashBoardDTO dashBoardDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getPriceTrendOverTime", FILE_EXTENSION, FILE_PATH_NAME);

        Map<String, Object> params = new HashMap<>();
        if(StringUtil.isNullOrEmpty(dashBoardDTO.getType())){
            params.put("propertyType", "SOLD");
        }
        else{
            params.put("propertyType", dashBoardDTO.getType());
        }

        List<DashBoardDTO> dashBoardDTOs = (List<DashBoardDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, DashBoardDTO.class);
        return dashBoardDTOs;
    }
    @Override
    public List<DashBoardDTO> getTopTransactionRegions(DashBoardDTO dashBoardDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getTopTransactionRegions", FILE_EXTENSION, FILE_PATH_NAME);

        Map<String, Object> params = new HashMap<>();
        if(StringUtil.isNullOrEmpty(dashBoardDTO.getType())){
            params.put("propertyType", "SOLD");
        }
        else{
            params.put("propertyType", dashBoardDTO.getType());
        }

        List<DashBoardDTO> dashBoardDTOs = (List<DashBoardDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, DashBoardDTO.class);
        return dashBoardDTOs;
    }
    @Override
    public List<DashBoardDTO> getSummaryTotalStat(DashBoardDTO dashBoardDTO) throws Exception {
        String sqlSelect = this.getSqlByFileName("getSummaryTotalStat", FILE_EXTENSION, FILE_PATH_NAME);

        Map<String, Object> params = new HashMap<>();
        if(StringUtil.isNullOrEmpty(dashBoardDTO.getType())){
            params.put("propertyType", "SOLD");
        }
        else{
            params.put("propertyType", dashBoardDTO.getType());
        }

        List<DashBoardDTO> dashBoardDTOs = (List<DashBoardDTO>) this.findAndAliasToBeanResultTransformerList(sqlSelect, params, DashBoardDTO.class);
        return dashBoardDTOs;
    }
}
