package com.example.advancedjava.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.advancedjava.Validators.Validator;
import com.example.advancedjava.dto.Request;
import com.example.advancedjava.dto.Response;
import com.example.advancedjava.exceptions.IncorrectWordException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/")
public class Greetings {

  

    @Autowired
    private Validator validator;

    @GetMapping("hello")
    public Response<Map<String,String>> getGreetings( @RequestParam("reqID") String id) {
        Map<String,String> mp=new HashMap<>();
        mp.put("result","Hello World");
        Response<Map<String,String>> response = new Response<Map<String,String>>().new Builder()
                .setStatus("success")
                .setStatus_code(HttpStatus.OK.value())
                .setStatus_msg("Request completed successfully")
                .set_reqid(id)
                    .setData(mp)
                .build();
        
        return response;
    }

    @PostMapping("hello")
    public ResponseEntity<Response<Map<String,String>>> getGreetings(@RequestBody Request<String> request) {
        Response<Map<String,String>> response;
        if(!validator.validateWord(request.getData())){
            log.error("Input name is incorrect");
            throw new IncorrectWordException("Name incorrect Exception");

        } else {
            Map<String,String> mp=new HashMap<>();
            mp.put("result",String.format("Hello %s", request.getData()));
            response = new Response<Map<String,String>>().new Builder()
            .setStatus("success")
            .setStatus_code(HttpStatus.OK.value())
            .setStatus_msg("Request completed successfully")
            .set_reqid(request.get_reqid())
            .setData(mp)
            .build();
        }
        
       

        return ResponseEntity.ok(response);
    }
    
    
    
}
