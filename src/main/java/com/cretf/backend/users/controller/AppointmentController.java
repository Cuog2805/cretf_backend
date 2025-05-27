package com.cretf.backend.users.controller;

import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.product.service.PropertyService;
import com.cretf.backend.users.dto.AppointmentDTO;
import com.cretf.backend.users.dto.DepositContractDTO;
import com.cretf.backend.users.service.AppointmentService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/getAppointmentBySearch")
    public Response<List<AppointmentDTO>> getAppointmentBySearch(@RequestBody AppointmentDTO appointmentDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAppointmentBySearch");
        Page<AppointmentDTO> result = appointmentService.getAppointmentBySearch(appointmentDTO, pageable);
        return Response.ok(result);
    }

    @PostMapping("/createAppointment")
    public Response<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) throws Exception {
        log.debug("Rest request to createAppointment: {}", appointmentDTO);
        AppointmentDTO result = appointmentService.create(appointmentDTO);
        return Response.ok(result);
    }

//    @PostMapping("/updateAppointment")
//    public Response<AppointmentDTO> updateAppointment(@RequestBody AppointmentDTO appointmentDTO) throws Exception {
//        log.debug("Rest request to createAppointment: {}", appointmentDTO);
//        AppointmentDTO result = appointmentService.create(appointmentDTO);
//        return Response.ok(result);
//    }

    @DeleteMapping("/deleteAppointment/{id}")
    public Response<String> deleteAppointment(@PathVariable String id) throws Exception {
        log.debug("REST request to delete Appointment : {}", id);
        boolean result = appointmentService.delete(id);
        if (result) {
            return Response.ok("Delete succeed!");
        }
        throw new Exception("Delete fail!");
    }

    @PostMapping("/approveAppointment")
    public Response<String> approveAppointment(@RequestBody AppointmentDTO appointmentDTO) throws Exception {
        log.debug("REST request to approveAppointment : {}", appointmentDTO.getAppointmentId());
        boolean result = appointmentService.approve(appointmentDTO);
        if (result) {
            return Response.ok("approve succeed!");
        }
        throw new Exception("approve fail!");
    }

//    @PostMapping("/rejectAppointment")
//    public Response<String> rejectAppointment(@RequestBody AppointmentDTO appointmentDTO) throws Exception {
//        log.debug("REST request to rejectAppointment : {}", appointmentDTO.getAppointmentId());
//        boolean result = appointmentService.reject(appointmentDTO);
//        if (result) {
//            return Response.ok("reject succeed!");
//        }
//        throw new Exception("reject fail!");
//    }
}
