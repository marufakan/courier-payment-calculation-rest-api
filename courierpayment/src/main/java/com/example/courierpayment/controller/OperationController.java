package com.example.courierpayment.controller;

import com.example.courierpayment.service.OperationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/operations")
public class OperationController {

    private final OperationService operationService;
    public OperationController(OperationService operationService) {this.operationService = operationService;}

}
