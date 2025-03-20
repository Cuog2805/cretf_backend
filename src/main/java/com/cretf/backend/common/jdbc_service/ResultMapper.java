package com.cretf.backend.common.jdbc_service;

//package com.fpt.common.data.mapper;

import java.io.BufferedReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

public abstract class ResultMapper {
    @SuppressWarnings(value = "unchecked")
    protected <T> T createInstance(Constructor<?> constructor, Object[] args) {
        try {
            return (T) constructor.newInstance(args);
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder("no constructor taking:\n");
            for (Object object : args) {
                if (object != null) {
                    sb.append("\t").append(object.getClass().getName()).append("\n");
                }
            }
            throw new RuntimeException(sb.toString(), e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @SuppressWarnings(value = "unchecked")
    protected <T> T createNewInstance(Constructor<?> constructor, Object[] args) {
        try {
            Class<?>[] clazzOfParams = constructor.getParameterTypes();

            List<Object> listObj = new ArrayList<>();
            for(int i=0; i<clazzOfParams.length; i++) {
                Class<?> clazz = clazzOfParams[i];
                if(args[i] instanceof BigDecimal) {
                    BigDecimal b = (BigDecimal) args[i];
                    if(clazz.isAssignableFrom(Integer.class)){
                        listObj.add(Integer.valueOf(b.intValue()));
                    }else if(clazz.isAssignableFrom(Long.class)) {
                        listObj.add(Long.valueOf(b.longValue()));
                    }else if(clazz.isAssignableFrom(Float.class)) {
                        listObj.add(Float.valueOf(b.floatValue()));
                    }else if(clazz.isAssignableFrom(Double.class)) {
                        listObj.add(Double.valueOf(b.doubleValue()));
                    }else if(clazz.isAssignableFrom(Boolean.class)) {
                        boolean data = (b.intValue()==1);
                        listObj.add(Boolean.valueOf(data));
                    }else {
                        listObj.add(args[i]);
                    }
                }
                else if(args[i] instanceof Clob) {
                    Clob data = (Clob)args[i];
                    listObj.add(getClobString(data));
                }else {
                    listObj.add(args[i]);
                }
            }
            args = listObj.toArray();

            return (T) constructor.newInstance(args);
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder("no constructor taking:\n");
            for (Object object : args) {
                if (object != null) {
                    sb.append("\t").append(object.getClass().getName()).append("\n");
                }
            }
            throw new RuntimeException(sb.toString(), e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private String getClobString(Clob clob) {
        String singleLine = null;
        StringBuffer sb = new StringBuffer();

        try {
            BufferedReader reader = new BufferedReader(clob.getCharacterStream());
            while ((singleLine = reader.readLine()) != null) {
                sb.append(singleLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}


