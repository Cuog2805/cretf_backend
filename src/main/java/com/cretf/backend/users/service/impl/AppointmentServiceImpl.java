package com.cretf.backend.users.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.cretf.backend.common.jdbc_service.BaseJdbcServiceImpl;
import com.cretf.backend.product.dto.AmenityDTO;
import com.cretf.backend.product.dto.ApprovalHistoryDTO;
import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.dto.PropertyFilesDTO;
import com.cretf.backend.product.entity.ApprovalHistory;
import com.cretf.backend.product.repository.ApprovalHistoryRepository;
import com.cretf.backend.product.repository.StatusRepository;
import com.cretf.backend.product.service.ApprovalHistoryService;
import com.cretf.backend.security.SecurityUtils;
import com.cretf.backend.users.dto.AppointmentDTO;
import com.cretf.backend.users.dto.DepositContractDTO;
import com.cretf.backend.users.entity.Appointment;
import com.cretf.backend.users.entity.DepositContract;
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
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl extends BaseJdbcServiceImpl<AppointmentDTO, String> implements AppointmentService {
    private final String FILE_EXTENSION = "sql";
    private final String FILE_PATH_NAME = "sqlScripts/Appointment";

    private final AppointmentRepository appointmentRepository;
    private final StatusRepository statusRepository;
    private final ModelMapper modelMapper;
    private final ApprovalHistoryRepository approvalHistoryRepository;
    private final ApprovalHistoryService approvalHistoryService;

    public AppointmentServiceImpl(
            EntityManager entityManager,
            AppointmentRepository appointmentRepository,
            StatusRepository statusRepository,
            ModelMapper modelMapper, ApprovalHistoryRepository approvalHistoryRepository, ApprovalHistoryService approvalHistoryService
    ) {
        super(entityManager, AppointmentDTO.class);
        this.appointmentRepository = appointmentRepository;
        this.statusRepository = statusRepository;
        this.modelMapper = modelMapper;
        this.approvalHistoryRepository = approvalHistoryRepository;
        this.approvalHistoryService = approvalHistoryService;
    }


    @Override
    public AppointmentDTO create(AppointmentDTO appointmentDTO) throws Exception {
        Appointment appointment;

        if (appointmentDTO.getAppointmentId() != null) {
            // update
            Optional<Appointment> existingAppointment = appointmentRepository.findById(appointmentDTO.getAppointmentId());
            if (existingAppointment.isEmpty()) {
                throw new Exception("Appointment not found with ID: " + appointmentDTO.getAppointmentId());
            }

            appointment = existingAppointment.get();
            modelMapper.map(appointmentDTO, appointment);
            appointment.setDateModified(new Date());
            appointment.setModifier(SecurityUtils.getCurrentUserLogin().orElse(null));
        } else {
            // create
            appointment = modelMapper.map(appointmentDTO, Appointment.class);
            appointment.setDateCreated(new Date());
            appointment.setCreator(SecurityUtils.getCurrentUserLogin().orElse(null));
        }

        appointmentRepository.save(appointment);
        BeanUtils.copyProperties(appointment, appointmentDTO);
        return appointmentDTO;
    }


    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) throws Exception {
        if (appointmentDTO.getAppointmentId() == null) {
            throw new Exception("Appointment ID must not be null for update.");
        }

        Optional<Appointment> existingAppointment = appointmentRepository.findById(appointmentDTO.getAppointmentId());
        if (existingAppointment.isEmpty()) {
            throw new Exception("Appointment not found with ID: " + appointmentDTO.getAppointmentId());
        }

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
            //appointmentDelete.setIsDeleted(1);
            appointmentRepository.delete(appointmentDelete);
        }
        return appointment.isPresent();
    }

    @Override
    public boolean approve(AppointmentDTO appointmentDTO) throws Exception {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(appointmentDTO.getAppointmentId());
        if(existingAppointment.isPresent()){
            Appointment appointment = existingAppointment.get();
            appointment.setStatusId(appointmentDTO.getApprovalHistoryDTO().getStatusId());
            appointmentRepository.save(appointment);

            //update ApprovalHistory
            ApprovalHistory approvalHistory =  modelMapper.map(appointmentDTO.getApprovalHistoryDTO(), ApprovalHistory.class);
            approvalHistory.setEntityTableId(appointmentDTO.getAppointmentId());
            approvalHistory.setTableName("APPOINTMENT");
            approvalHistory.setApprovalDate(new Date());
            approvalHistoryRepository.save(approvalHistory);

            return true;
        }
        return existingAppointment.isPresent();
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

        List<String> appointmentIds = result.stream().map(AppointmentDTO::getAppointmentId).toList();
        //gan approval history
        List<ApprovalHistoryDTO> approvalHistoryDTOs = approvalHistoryService.findByEntityTableIds(appointmentIds);
        Map<String, List<ApprovalHistoryDTO>> appointmentApprovalGrouped = approvalHistoryDTOs.stream()
                .filter(a -> a.getEntityTableId() != null)
                .collect(Collectors.groupingBy(ApprovalHistoryDTO::getEntityTableId));
        for (AppointmentDTO appointment : result) {
            String appointmentId = appointment.getAppointmentId();
            List<ApprovalHistoryDTO> approvalHistories = appointmentApprovalGrouped.getOrDefault(appointmentId, new ArrayList<>());
            appointment.setApprovalHistoryDTOs(approvalHistories);
        }

        return new PageImpl<>(result, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), total);
    }
}
