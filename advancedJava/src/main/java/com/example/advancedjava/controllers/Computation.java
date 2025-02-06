package com.example.advancedjava.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.advancedjava.dto.Calculator;
import com.example.advancedjava.dto.Request;
import com.example.advancedjava.dto.Response;
import com.example.advancedjava.dto.StatsCalculator;
import com.example.advancedjava.service.ComputationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/")
public class Computation {

    @Autowired
    ComputationService service;


    
    @PostMapping("/mysum")
    public ResponseEntity<Response<Map<String,String>>> simpleCalculator(@Valid @RequestBody Request<Calculator> request) {
        Map<String,String> mp = new HashMap<>();
        Float result = service.calculate(request.getData().getOperandFirst(),request.getData().getOperandSecond(),request.getData().getOperation());
        mp.put("result", String.valueOf(result));
        mp.put("operation", request.getData().getOperation());
        mp.put("firstOperand",String.valueOf(request.getData().getOperandFirst()));
        mp.put("secondOperand",String.valueOf(request.getData().getOperandSecond().toString()));

        Response<Map<String,String>> response = new Response<Map<String,String>>().new Builder()
            .setStatus("success")
            .setStatus_code(HttpStatus.OK.value())
            .setStatus_msg("Request completed successfully")
            .set_reqid(request.get_reqid())
            .setData(mp)
            .build();

        

        return ResponseEntity.ok(response);
    }

    @PostMapping("/mycalc") 
    public ResponseEntity<Response<Map<String,String>>> ComplexCalculator(@Valid @RequestBody Request<StatsCalculator> request) {
        Map<String,String> mp = new HashMap<>();
        Float result = service.calculate(request.getData().getList(),request.getData().getOperation());
        mp.put("result", String.valueOf(result));
        mp.put("operation", request.getData().getOperation());
        mp.put("List",request.getData().getList().toString());

        Response<Map<String,String>> response = new Response<Map<String,String>>().new Builder()
            .setStatus("success")
            .setStatus_code(HttpStatus.OK.value())
            .setStatus_msg("Request completed successfully")
            .set_reqid(request.get_reqid())
                .setData(mp)
            .build();    

        return ResponseEntity.ok(response);
    }

    
    
    

}
