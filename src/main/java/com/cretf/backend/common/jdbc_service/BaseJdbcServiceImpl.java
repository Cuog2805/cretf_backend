package com.cretf.backend.common.jdbc_service;

import com.cretf.backend.utils.FileUtils;
import com.cretf.backend.common.jdbc_service.service.BaseJdbcService;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public class BaseJdbcServiceImpl<T, ID> implements BaseJdbcService<T, ID> {
    private BaseDAO<T> baseDAO;

    public BaseJdbcServiceImpl(EntityManager entityManager, Class<T> entityClass){
        baseDAO = new BaseDAO<>(entityManager, entityClass);
    }

    protected String getSqlByFileName(String fileName, String fileExtension, String filePathName) throws Exception{
        String sql = FileUtils.loadContentFromResourceAsStream(fileName, fileExtension, filePathName);
        return sql;
    }

    @Override
    public List<?> findAndAliasToBeanResultTransformerList(String sql, final Map<String, Object> params, Class<?> clazz) throws Exception {
        try {
            return baseDAO.findAndAliasToBeanResultTransformerList(sql, params, clazz);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<?> findAndAliasToBeanResultTransformer(String sql, final Map<String, Object> params, Pageable pageable, Class<?> clazz) throws Exception {
        try {
            return baseDAO.findAndAliasToBeanResultTransformer(sql, params, pageable, clazz);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Long countByNativeQuery(String sql, final Map<String,Object> params) throws Exception {
        try {
            return baseDAO.countByNativeQuery(sql, params);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
