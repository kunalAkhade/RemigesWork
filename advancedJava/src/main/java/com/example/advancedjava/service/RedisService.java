package com.example.advancedjava.service;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.advancedjava.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    // Set default value for a new key
    public void setUserKey(String empName) {
        redisTemplate.opsForValue().set("user." + empName, 0);
        redisTemplate.expire("user." + empName, Duration.ofSeconds(180));
    }

    public void setUserKey(String empName, Integer count) {
        redisTemplate.opsForValue().set("user." + empName, count);
        redisTemplate.expire("user." + empName, Duration.ofSeconds(180));
    }

    // Get value of a key
    public Object getUserValue(String empName) {
        if(!redisTemplate.hasKey("user."+empName)){
            return "employee Doesn't exists";
        }
        return (Integer)redisTemplate.opsForValue().get("user." + empName);
    }

    public Employee getEmployeeValue(String id) {
        // return (Employee) redisTemplate.opsForValue().get("employee."+id);
        Object value = redisTemplate.opsForValue().get("employee." + id);

        if (value instanceof LinkedHashMap) {
            // Convert LinkedHashMap to Employee
            return objectMapper.convertValue(value, Employee.class);
        }
        return (Employee) value;
    }

    public void setEmployeeKey(String empName, Employee employee) {
        redisTemplate.opsForValue().set("employee." + empName, employee);
        redisTemplate.expire("employee." + empName, Duration.ofSeconds(3600));
    }

    public void setEmployeeListKey(String filter, List<Employee> list) {
        redisTemplate.opsForValue().set("employees." + filter, list);
        redisTemplate.expire("employees." + filter, Duration.ofSeconds(3600));
    }

    public Object getEmployeeListValue(String filter) {

        Object value = redisTemplate.opsForValue().get("employees." + filter);

        if (value instanceof List<?>) {
            // Safely cast to List<LinkedHashMap>
            @SuppressWarnings("unchecked")
            List<LinkedHashMap<String, Object>> mapList = (List<LinkedHashMap<String, Object>>) value;
            // Convert each LinkedHashMap to Employee
            return mapList.stream()
                    .map(map -> objectMapper.convertValue(map, Employee.class))
                    .collect(Collectors.toList());
        }
        
        return null;
    }

    public Integer getUserValue(String dept, String empid) {
        return (Integer) redisTemplate.opsForValue().get("user." + dept + "." + empid);
    }

    // Increment value of a key
    public void incrementUserValue(String empName) {
        redisTemplate.opsForValue().increment("user." + empName);
    }

    public void incrementUserValue(String empName, Object count) {
        redisTemplate.opsForValue().increment("user." + empName, (Integer) count);
    }

    // Decrement value of a key
    public void decrementUserValue(String empName) {
        redisTemplate.opsForValue().decrement("user." + empName);
    }

    // Set TTL for a key
    public void setTTL(String empName, long seconds) {
        redisTemplate.expire("user." + empName, Duration.ofSeconds(seconds));
    }

    public boolean doesUserKeyExist(String empName) {
        String key = "user." + empName;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public Long getRemainingTTL(String key) {
        return redisTemplate.getExpire("user."+key);
    }

    public void removeEmployee(String employeeId) {
        String cacheKey = "employee." + employeeId;
        redisTemplate.delete(cacheKey);
        log.info("Employee data with key: " + cacheKey + " has been deleted from cache.");
    }


}
