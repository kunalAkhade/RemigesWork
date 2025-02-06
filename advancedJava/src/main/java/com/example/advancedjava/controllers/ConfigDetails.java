package com.example.advancedjava.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.advancedjava.dto.Request;
import com.example.advancedjava.dto.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class ConfigDetails {



    @Autowired
    Environment environment;

    @PostMapping("/myproperties")
    public ResponseEntity<Response<Map<String,String>>> getMyProperties(@RequestBody Request<List<String>> request) {
        
        List<String> list=request.getData();
        Map<String,String> map;
        map = new HashMap<>();  
        for(String element: list){
            if(environment.getProperty(element)!=null && !environment.getProperty(element).isEmpty()){
                map.put(element, environment.getProperty(element));
            }
            else{
                log.warn(element+" property is either null or empty");
            }
        }


        Response<Map<String, String>> response = new Response<Map<String,String>>().new Builder()
        .setStatus("success")
        .setStatus_code(HttpStatus.OK.value())
        .setStatus_msg("Request completed successfully")
        .set_reqid(request.get_reqid())
        .setData(map)
        .build();
        
        return ResponseEntity.ok(response);

    }
    
    
}
