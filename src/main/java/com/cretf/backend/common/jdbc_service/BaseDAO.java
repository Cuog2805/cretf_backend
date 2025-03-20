package com.cretf.backend.common.jdbc_service;

import com.cretf.backend.utils.FileUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;

import java.util.List;
import java.util.Map;

public class BaseDAO<T> {
    private Class<T> entityClass;
    private EntityManager entityManager;

    public BaseDAO(EntityManager entityManager, Class<T> entityClass){
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    protected String getSqlByFileName(String fileName, String fileExtension, String filePathName) throws Exception{
        String sql = FileUtils.loadContentFromResourceAsStream(fileName, fileExtension, filePathName);

        return sql;
    }

    public List <?> findAndAliasToBeanResultTransformerList(String sql, final Map<String, Object> params, Class<?> clazz) throws Exception {

        Query query = entityManager.createNativeQuery(sql);
        this.setParams(query, params);

        NativeQueryImpl<?> nativeQuery = (NativeQueryImpl<?>) query;
        nativeQuery.setResultTransformer(new AliasToBeanResultTransformer(clazz));

        return query.getResultList();
    }

    private void setParams(Query query, final Map<String, Object> params) {
        if(params != null) {
            for(Map.Entry<String,Object> entry : params.entrySet()){
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }
}