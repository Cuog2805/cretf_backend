package com.cretf.backend.common.jdbc_service;

import com.cretf.backend.utils.FileUtils;
import com.cretf.backend.common.jdbc_service.service.BaseJdbcService;
import jakarta.persistence.EntityManager;
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
            throw new Exception();
        }
    }
}
