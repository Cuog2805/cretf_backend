package com.cretf.backend.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.transform.AliasToBeanResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Field;

public class CustomResultTransformer extends AliasToBeanResultTransformer {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Class<?>> jsonFieldTypes = new HashMap<>();

    // Danh sách các tên trường JSON cần xử lý theo camelCase
    private static final Set<String> JSON_FIELD_NAMES = new HashSet<>(
            Arrays.asList("statusIds", "fileIds")
    );

    public CustomResultTransformer(Class<?> resultClass) throws Exception {
        super(resultClass);

        try {
            for (Field field : resultClass.getDeclaredFields()) {
                if (field.getType() == List.class) {
                    String fieldName = field.getName();

                    // Kiểm tra nếu tên trường nằm trong danh sách cần xử lý hoặc theo quy tắc đặt tên
                    if (JSON_FIELD_NAMES.contains(fieldName) || fieldName.endsWith("Ids")) {
                        // Thêm vào map cả tên camelCase và tên lowercase để xử lý cả hai trường hợp
                        jsonFieldTypes.put(fieldName, field.getType());
                        jsonFieldTypes.put(fieldName.toLowerCase(), field.getType());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        for (int i = 0; i < aliases.length; i++) {
            String alias = aliases[i];
            String aliasLower = alias.toLowerCase();

            // Kiểm tra xem trường này có phải là trường JSON cần xử lý không
            if ((jsonFieldTypes.containsKey(alias) || jsonFieldTypes.containsKey(aliasLower))
                    && tuple[i] instanceof String) {
                String jsonValue = (String) tuple[i];
                try {
                    // Xử lý chuỗi JSON
                    if (jsonValue != null && !jsonValue.isEmpty()) {
                        if (jsonValue.startsWith("[[")) {
                            // Trường hợp double-nested JSON array
                            List<List<String>> nestedList = objectMapper.readValue(
                                    jsonValue, new TypeReference<List<List<String>>>() {});
                            if (!nestedList.isEmpty()) {
                                tuple[i] = nestedList.get(0);
                            } else {
                                tuple[i] = new ArrayList<>();
                            }
                        } else {
                            // Trường hợp JSON array bình thường
                            tuple[i] = objectMapper.readValue(
                                    jsonValue, new TypeReference<List<String>>() {});
                        }
                    } else {
                        tuple[i] = new ArrayList<>();
                    }
                } catch (JsonProcessingException e) {
                    tuple[i] = new ArrayList<>();
                }
            }
        }
        return super.transformTuple(tuple, aliases);
    }

//    // Phương thức để thêm trường JSON cần xử lý trong lúc chạy nếu cần
//    public static void addJsonFieldName(String fieldName) {
//        JSON_FIELD_NAMES.add(fieldName);
//        // Thêm cả phiên bản lowercase để đối phó với sự khác biệt giữa alias SQL và tên trường
//        JSON_FIELD_NAMES.add(fieldName.toLowerCase());
//    }
}