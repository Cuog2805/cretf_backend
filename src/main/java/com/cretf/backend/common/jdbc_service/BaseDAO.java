package com.cretf.backend.common.jdbc_service;

import com.cretf.backend.converter.CustomResultTransformer;
import com.cretf.backend.utils.FileUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        nativeQuery.setResultTransformer(new CustomResultTransformer(clazz));

        return query.getResultList();
    }

    public List <?> findAndAliasToBeanResultTransformer(String sql, final Map<String, Object> params, Pageable pageable
            , Class<?> clazz) throws Exception {

        sql = this.setOrder(sql, pageable);

        Query query = entityManager.createNativeQuery(sql);
        this.setParams(query, params);
        this.paging(query, pageable);

        NativeQueryImpl<?> nativeQuery = (NativeQueryImpl<?>) query;
        nativeQuery.setResultTransformer(new CustomResultTransformer(clazz));

        return query.getResultList();
    }

    public Long countByNativeQuery(String sql, final Map<String, Object> params) throws Exception {
        Query query = entityManager.createNativeQuery(sql);
        this.setParams(query, params);

        Object total = query.getSingleResult();
        return this.getTotal(total);
    }

    private void setParams(Query query, final Map<String, Object> params) {
        if(params != null) {
            for(Map.Entry<String,Object> entry : params.entrySet()){
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    private String setOrder(String sql, Pageable pageable) {
        List<Sort.Order> orders = pageable!=null ? pageable.getSort().toList() : new ArrayList<Sort.Order>();
        if(orders!=null && orders.size()>0) {
            sql += " order by ";
            for(int i=0; i<orders.size(); i++) {
                if(i > 0) {
                    sql += ",";
                }
                Sort.Order order = orders.get(i);
                sql += order.getProperty() + " " + order.getDirection().name();
            }
        }
        return sql;
    }

    private void paging(Query query, Pageable pageable) {
        if (pageable!=null && pageable.getPageSize() > 1) {
            query.setFirstResult(BigDecimal.valueOf(pageable.getOffset()).intValue());
            query.setMaxResults(pageable.getPageSize());
        }
    }

    private Long getTotal(Object total) {
        Long count = Long.valueOf(0);
        if(!Objects.isNull(total)) {
            if(total instanceof Integer) {
                Integer value = (Integer)total;
                count = Long.valueOf(value.longValue());
            }else if(total instanceof Long){
                count = (Long)total;
            }else if(total instanceof BigDecimal){
                BigDecimal value = (BigDecimal)total;
                count = Long.valueOf(value.longValue());
            }
        }
        return count;
    }
}