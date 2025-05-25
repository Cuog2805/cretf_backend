package com.cretf.backend.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NativeSqlBuilder {

    public enum ComparisonType {
        EQUAL("="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL("<="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUAL(">="),
        IN("IN"),
        NOT_IN("NOT IN"),
        LIKE("LIKE"),
        NOT_EQUAL("<>");

        private final String operator;

        ComparisonType(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }
    }

    public static class ColumnInfo {
        private String fullColumnName; // Tên đầy đủ của cột, ví dụ: "p.StatusId"
        private ComparisonType comparisonType;

        public ColumnInfo(String fullColumnName) {
            this(fullColumnName, ComparisonType.EQUAL);
        }

        public ColumnInfo(String fullColumnName, ComparisonType comparisonType) {
            this.fullColumnName = fullColumnName;
            this.comparisonType = comparisonType;
        }

        public String getFullColumnName() {
            return fullColumnName;
        }

        public ComparisonType getComparisonType() {
            return comparisonType;
        }
    }

    public static class NativeSqlAfterBuilded {
        public String sql;
        public Map<String, Object> params;

        public NativeSqlAfterBuilded(String sql, Map<String, Object> params) {
            this.sql = sql;
            this.params = params;
        }
    }

    public static NativeSqlAfterBuilded buildSqlWithParams(String originalSql, Object dto, Map<String, String> aliasMap) {
        Map<String, ColumnInfo> columnInfoMap = new HashMap<>();
        for (Map.Entry<String, String> entry : aliasMap.entrySet()) {
            String fieldName = entry.getKey();
            String alias = entry.getValue();
            columnInfoMap.put(fieldName, new ColumnInfo(alias + "." + fieldName));
        }
        return buildSqlWithColumnInfo(originalSql, dto, columnInfoMap);
    }

    public static NativeSqlAfterBuilded buildSqlWithParams(String originalSql, Object dto,
                                                           Map<String, String> aliasMap,
                                                           Map<String, ComparisonType> comparisonMap) {
        Map<String, ColumnInfo> columnInfoMap = new HashMap<>();
        for (Map.Entry<String, String> entry : aliasMap.entrySet()) {
            String fieldName = entry.getKey();
            String alias = entry.getValue();
            ComparisonType type = comparisonMap != null && comparisonMap.containsKey(fieldName) ?
                    comparisonMap.get(fieldName) : ComparisonType.EQUAL;
            columnInfoMap.put(fieldName, new ColumnInfo(alias + "." + fieldName, type));
        }
        return buildSqlWithColumnInfo(originalSql, dto, columnInfoMap);
    }

    // Phương thức chính - sử dụng ColumnInfo
    public static NativeSqlAfterBuilded buildSqlWithColumnInfo(String originalSql, Object dto, Map<String, ColumnInfo> columnInfoMap) {
        StringBuilder whereClause = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        // Nếu columnInfoMap rỗng thì trả lại nguyên SQL
        if (columnInfoMap == null || columnInfoMap.isEmpty()) {
            return new NativeSqlAfterBuilded(originalSql, params);
        }

        Field[] fields = dto.getClass().getDeclaredFields();
        boolean hasExistingWhere = originalSql.toLowerCase().contains("where");
        boolean hasCondition = false;

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(dto);
                if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
                    String fieldName = field.getName();

                    // Nếu columnInfoMap không chứa thông tin cho field này thì bỏ qua
                    if (!columnInfoMap.containsKey(fieldName)) {
                        continue;
                    }

                    ColumnInfo columnInfo = columnInfoMap.get(fieldName);
                    String fullColumnName = columnInfo.getFullColumnName();
                    ComparisonType comparisonType = columnInfo.getComparisonType();

                    if (!hasCondition) {
                        whereClause.append(hasExistingWhere ? " and " : " where ");
                        hasCondition = true;
                    } else {
                        whereClause.append(" and ");
                    }

                    whereClause.append(fullColumnName);

                    // Xử lý các loại so sánh khác nhau
                    if (comparisonType == ComparisonType.IN) {
                        // Xử lý trường hợp IN
                        if (value instanceof Collection) {
                            whereClause.append(" IN (:").append(fieldName).append(")");
                        } else {
                            // Nếu không phải Collection thì chuyển về so sánh EQUAL
                            whereClause.append(" = :").append(fieldName);
                        }
                    } else if (comparisonType == ComparisonType.LIKE) {
                        whereClause.append(" LIKE :").append(fieldName);
                        // Đảm bảo giá trị có dấu % nếu là LIKE
                        if (value instanceof String && !((String) value).contains("%")) {
                            value = "%" + value + "%";
                        }
                    } else {
                        // Các loại so sánh khác
                        whereClause.append(" ").append(comparisonType.getOperator()).append(" :").append(fieldName);
                    }

                    params.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // hoặc throw runtime nếu muốn
            }
        }

        String finalSql = originalSql + whereClause;
        return new NativeSqlAfterBuilded(finalSql, params);
    }

    // Helper để tạo ColumnInfo Map với nhiều thông tin
    public static Map<String, ColumnInfo> createColumnInfoMap() {
        return new HashMap<>();
    }

    // Dùng cho việc thêm một cặp columnName & columnInfo vào map
    public static void addColumnInfo(Map<String, ColumnInfo> map, String fieldName, String fullColumnName) {
        map.put(fieldName, new ColumnInfo(fullColumnName));
    }

    public static void addColumnInfo(Map<String, ColumnInfo> map, String fieldName, String fullColumnName,
                                     ComparisonType comparisonType) {
        map.put(fieldName, new ColumnInfo(fullColumnName, comparisonType));
    }
}