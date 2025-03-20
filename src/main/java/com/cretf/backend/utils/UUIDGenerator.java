package com.cretf.backend.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class UUIDGenerator implements IdentifierGenerator {
    public static final String GENERATOR_NAME = "timeBasedGenerator";
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {
        Serializable id = (Serializable) sharedSessionContractImplementor.getEntityPersister(object.getClass().getName(), object)
                .getClassMetadata()
                .getIdentifier(object, sharedSessionContractImplementor);
        return  (id == null) ? generate() : id;
    }
    public String generate(){
        return UUIDUtils.newTimeUUID().toString();
    }
}
