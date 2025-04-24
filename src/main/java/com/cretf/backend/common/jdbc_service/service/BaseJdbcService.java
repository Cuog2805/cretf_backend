package com.cretf.backend.common.jdbc_service.service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BaseJdbcService<T, ID> {
    List<?> findAndAliasToBeanResultTransformerList(String sql, final Map<String, Object> params, Class<?> clazz) throws Exception;
    public List<?> findAndAliasToBeanResultTransformer(String sql, final Map<String, Object> params, Pageable pageable, Class<?> clazz) throws Exception;
    public Long countByNativeQuery(String sql, final Map<String,Object> params) throws Exception;
}
