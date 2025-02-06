package com.example.advancedjava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.advancedjava.dto.Response;
import com.example.advancedjava.service.RedisService;


@RestController
@RequestMapping("/api/redis")
public class RedisController {

    private final RedisService redisService;

    @Autowired
    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/add/{empName}")
    public ResponseEntity<Response<String>> addEmployee(@PathVariable String empName) {
        redisService.setUserKey(empName);

        Response<String> response = new Response<String>().new Builder()
                .setStatus("success")
                .setStatus_code(HttpStatus.OK.value())
                .setStatus_msg("Request completed successfully")
                .setData("Added key user." + empName + " with default value 0")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add/{empName}/{num}")
    public ResponseEntity<Response<String>> addEmployee(@PathVariable String empName, @PathVariable Integer num) {
        redisService.setUserKey(empName, num);

        Response<String> response = new Response<String>().new Builder()
                .setStatus("success")
                .setStatus_code(HttpStatus.OK.value())
                .setStatus_msg("Request completed successfully")
                .setData("Added key user." + empName + " with default value "+num)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{empName}")
    public ResponseEntity<Response<?>> getEmployeeValue(@PathVariable String empName) {
        Response<Object> response = new Response<Object>().new Builder()
                .setStatus("success")
                .setStatus_code(HttpStatus.OK.value())
                .setStatus_msg("Request completed successfully")
                .setData(redisService.getUserValue(empName))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/increment/{empName}")
    public ResponseEntity<Response<String>> incrementEmployeeValue(@PathVariable String empName) {
        redisService.incrementUserValue(empName);

        Response<String> response = new Response<String>().new Builder()
                .setStatus("success")
                .setStatus_code(HttpStatus.OK.value())
                .setStatus_msg("Request completed successfully")
                .setData("Incremented value of user." + empName)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/decrement/{empName}")
    public ResponseEntity<Response<String>> decrementEmployeeValue(@PathVariable String empName) {
        redisService.decrementUserValue(empName);
        Response<String> response = new Response<String>().new Builder()
                .setStatus("success")
                .setStatus_code(HttpStatus.OK.value())
                .setStatus_msg("Request completed successfully")
                .setData("Decremented value of user." + empName)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/expire/{empName}")
    public ResponseEntity<Response<String>> setEmployeeTTL(@PathVariable String empName, @RequestParam long seconds) {
        redisService.setTTL(empName, seconds);
        Response<String> response = new Response<String>().new Builder()
                .setStatus("success")
                .setStatus_code(HttpStatus.OK.value())
                .setStatus_msg("Request completed successfully")
                .setData("Set TTL for user." + empName + " to " + seconds + " seconds")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ttl/{key}")
    public ResponseEntity<Long> getTTL(@PathVariable String key) {
        Long ttl = redisService.getRemainingTTL(key);
        return ResponseEntity.ok(ttl);
    }

    
    
}
