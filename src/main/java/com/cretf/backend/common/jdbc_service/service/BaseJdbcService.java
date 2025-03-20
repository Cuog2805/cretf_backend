package com.cretf.backend.common.jdbc_service.service;

import java.util.List;
import java.util.Map;

public interface BaseJdbcService<T, ID> {
    List<?> findAndAliasToBeanResultTransformerList(String sql, final Map<String, Object> params, Class<?> clazz) throws Exception;
}
