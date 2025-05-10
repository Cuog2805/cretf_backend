package com.cretf.backend.users.controller;

import com.cretf.backend.product.entity.Property;
import com.cretf.backend.users.dto.DepositDTO;
import com.cretf.backend.users.service.DepositService;
import com.cretf.backend.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposit")
public class DepositController {
    private final Logger log = LoggerFactory.getLogger(Property.class);
    private final DepositService depositsService;

    public DepositController(DepositService depositsService) {
        this.depositsService = depositsService;
    }

    @PostMapping("/getAllDeposits")
    public Response<List<DepositDTO>> getAllDeposits(@RequestBody DepositDTO depositsDTO, @ParameterObject Pageable pageable) throws Exception {
        log.debug("Rest request to getAllDeposits");
        Page<DepositDTO> result = depositsService.getDepositsBySearch(depositsDTO, pageable);
        return Response.ok(result);
    }

    @GetMapping("/getDepositsById/{id}")
    public Response<DepositDTO> getDepositsById(@PathVariable String id) throws Exception {
        log.debug("Rest request to getDepositsById");
        DepositDTO result = depositsService.getDepositsById(id);
        return Response.ok(result);
    }

}
