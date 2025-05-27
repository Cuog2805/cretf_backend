package com.cretf.backend.users.service;

import com.cretf.backend.users.dto.AppointmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentService {
    AppointmentDTO create(AppointmentDTO appointmentDTO) throws Exception;
    AppointmentDTO update(AppointmentDTO appointmentDTO) throws Exception;
    boolean delete(String id) throws Exception;
    Page<AppointmentDTO> getAppointmentBySearch(AppointmentDTO appointmentDTO, Pageable pageable) throws Exception;
    public boolean approve(AppointmentDTO appointmentDTO) throws Exception;
    //public boolean reject(AppointmentDTO appointmentDTO) throws Exception;
}
