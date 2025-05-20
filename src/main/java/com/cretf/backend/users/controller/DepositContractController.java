package com.cretf.backend.users.controller;

import com.cretf.backend.product.dto.PropertyDTO;
import com.cretf.backend.product.entity.Property;
import com.cretf.backend.users.dto.DepositContractDTO;
import com.cretf.backend.users.dto.DepositDTO;
import com.cretf.backend.users.service.DepositContractService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depositContract")
public class DepositContractController {
    private final Logger log = LoggerFactory.getLogger(DepositContractDTO.class);
    private final DepositContractService depositContractService;

    @Autowired
    public DepositContractController(DepositContractService depositContractService) {
        this.depositContractService = depositContractService;
    }

    @PostMapping("/createDepositContract")
    public Response<DepositContractDTO> createDepositContract(@RequestBody DepositContractDTO request) {
        try {
            DepositContractDTO createdTemplate = depositContractService.create(request);
            return Response.ok(createdTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("Fail to createTemplate");
        }
    }

//    @GetMapping("/getAllDepositContract")
//    public Response<List<DepositContractDTO>> getAllDepositContract() {
//        try {
//            List<DepositContractDTO> templates = depositContractService.getAll();
//            return Response.ok(templates);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.error("Fail to createTemplate");
//        }
//    }

    @GetMapping("/getDepositContractById/{id}")
    public Response<DepositContractDTO> getDepositContractById(@PathVariable String id) {
        DepositContractDTO result = depositContractService.getById(id);
        return Response.ok(result);
    }

    @GetMapping("/searchDepositContract")
    public Response<List<DepositContractDTO>> searchDepositContract(@RequestParam String keyword) {
        List<DepositContractDTO> templates = depositContractService.search(keyword);
        return Response.ok(templates);
    }

    @DeleteMapping("/deleteDepositContract/{templateId}")
    public Response<String> deleteDepositContract(@PathVariable String templateId) throws Exception {
        boolean result = depositContractService.delete(templateId);
        if (result) {
            return Response.ok("Delete succeed!");
        }
        throw new Exception("Delete fail!");
    }

    @PostMapping("/getAllDepositContract")
    public Response<List<DepositContractDTO>> getAllDepositContract(@RequestBody DepositContractDTO depositContractDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAllDepositContract");
        Page<DepositContractDTO> result = depositContractService.getDepoistContractBySearch(depositContractDTO, pageable);
        return Response.ok(result);
    }

    @PostMapping("/confirmDepositContract")
    public Response<String> confirmDepositContract(@RequestBody DepositContractDTO depositContractDTO) throws Exception {
        log.debug("REST request to confirmProperty : {}", depositContractDTO.getDepositContractId());
        boolean result = depositContractService.comfirm(depositContractDTO);
        if (result) {
            return Response.ok("confirm succeed!");
        }
        throw new Exception("confirm fail!");
    }

    @PostMapping("/rejectDepositContract")
    public Response<String> rejectDepositContract(@RequestBody DepositContractDTO depositContractDTO) throws Exception {
        log.debug("REST request to rejectDepositContract : {}", depositContractDTO.getPropertyId());
        boolean result = depositContractService.reject(depositContractDTO);
        if (result) {
            return Response.ok("reject succeed!");
        }
        throw new Exception("reject fail!");
    }
}