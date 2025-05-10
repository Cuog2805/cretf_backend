package com.cretf.backend.users.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.AmenityDTO;
import com.cretf.backend.security.SecurityUtils;
import com.cretf.backend.users.dto.AppointmentDTO;
import com.cretf.backend.users.entity.Appointment;
import com.cretf.backend.users.repository.AppointmentRepository;
import com.cretf.backend.users.service.AppointmentService;
import com.cretf.backend.utils.NativeSqlBuilder;
import jakarta.persistence.EntityManager;
import org.apache.catalina.security.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppointmentServiceImpl extends BaseJdbcServiceImpl<AppointmentDTO, String> implements AppointmentService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Appointment";

    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    public AppointmentServiceImpl(
            EntityManager entityManager,
            AppointmentRepository appointmentRepository, ModelMapper modelMapper
    ) {
        super(entityManager, AppointmentDTO.class);
        this.appointmentRepository = appointmentRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public AppointmentDTO create(AppointmentDTO appointmentDTO) throws Exception {
        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
        appointment.setDateCreated(new Date());
        appointment.setCreator(SecurityUtils.getCurrentUserLogin().orElse(null));
        appointmentRepository.save(appointment);
        BeanUtils.copyProperties(appointment, appointmentDTO);
        return appointmentDTO;
    }

    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) throws Exception {
        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
        appointmentRepository.save(appointment);
        BeanUtils.copyProperties(appointment, appointmentDTO);
        return appointmentDTO;
    }

    @Override
    public boolean delete(String id) throws Exception {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if(appointment.isPresent()){
            Appointment appointmentDelete = appointment.get();
            appointmentDelete.setIsDeleted(1);
        }
        return appointment.isPresent();
    }

    @Override
    public Page<AppointmentDTO> getAppointmentBySearch(AppointmentDTO appointmentDTO, Pageable pageable) throws Exception {
        String sqlSelect = this.getSqlByFileName("getAllAppointment", FILE_EXTENSION, FILE_PATH_NAME);
        String sqlCount = this.getSqlByFileName("countAllAppointment", FILE_EXTENSION, FILE_PATH_NAME);
        Map<String, String> aliasMap = new HashMap<>();
        aliasMap.put("appointmentId", "ap");
        aliasMap.put("propertyId", "ap");
        aliasMap.put("type", "ap");
        aliasMap.put("statusId", "ap");

        NativeSqlBuilder.NativeSqlAfterBuilded sqlSelectBuilded = NativeSqlBuilder.buildSqlWithParams(sqlSelect, appointmentDTO, aliasMap);
        NativeSqlBuilder.NativeSqlAfterBuilded sqlCountBuilded = NativeSqlBuilder.buildSqlWithParams(sqlCount, appointmentDTO, aliasMap);

        List<AppointmentDTO> result = (List<AppointmentDTO>) this.findAndAliasToBeanResultTransformer(sqlSelectBuilded.sql, sqlSelectBuilded.params, pageable, AppointmentDTO.class);
        Long total = this.countByNativeQuery(sqlCountBuilded.sql, sqlCountBuilded.params);
        return new PageImpl<>(result, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }
}
