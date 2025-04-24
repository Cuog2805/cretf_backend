package com.cretf.backend.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class NativeSqlBuilder {
    public static class NativeSqlAfterBuilded {
        public String sql;
        public Map<String, Object> params;

        public NativeSqlAfterBuilded(String sql, Map<String, Object> params) {
            this.sql = sql;
            this.params = params;
        }
    }

    public static NativeSqlAfterBuilded buildSqlWithParams(String originalSql, Object dto, Map<String, String> aliasMap) {
        StringBuilder whereClause = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        // Nếu aliasMap rỗng thì trả lại nguyên SQL
        if (aliasMap == null || aliasMap.isEmpty()) {
            return new NativeSqlAfterBuilded(originalSql, params);
        }

        Field[] fields = dto.getClass().getDeclaredFields();
        boolean hasExistingWhere = originalSql.toLowerCase().contains(" where ");
        boolean hasCondition = false;

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(dto);
                if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
                    String columnName = field.getName();
                    String alias = aliasMap.getOrDefault(columnName, "");

                    // Nếu aliasMap không chứa alias cho field này thì bỏ qua
                    if (!aliasMap.containsKey(columnName)) {
                        continue;
                    }

                    if (!hasCondition) {
                        whereClause.append(hasExistingWhere ? " and " : " where ");
                        hasCondition = true;
                    } else {
                        whereClause.append(" and ");
                    }

                    whereClause.append(alias).append(alias.isEmpty() ? "" : ".")
                            .append(columnName)
                            .append(" = :").append(columnName);

                    params.put(columnName, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // hoặc throw runtime nếu muốn
            }
        }

        String finalSql = originalSql + whereClause;
        return new NativeSqlAfterBuilded(finalSql, params);
    }


}
