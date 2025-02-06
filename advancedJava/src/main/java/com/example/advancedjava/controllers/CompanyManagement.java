package com.example.advancedjava.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.advancedjava.dao.EmployeeDao;
import com.example.advancedjava.dto.EmployeeDTO;
import com.example.advancedjava.dto.EmployeeWrapper;
import com.example.advancedjava.dto.Request;
import com.example.advancedjava.dto.Response;
import com.example.advancedjava.model.Employee;
import com.example.advancedjava.service.CompanyManagementService;
import com.example.advancedjava.service.RedisService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/myhr/employee")
public class CompanyManagement {

        @Autowired
        EmployeeDao employeeRepository;

        @Autowired
        CompanyManagementService service;

        @Autowired
        RedisService redisService;

        private Response<String> createErrorResponse(String message, String reqid) {
                return new Response<String>().new Builder()
                                .setStatus("failed")
                                .setStatus_code(HttpStatus.BAD_REQUEST.value())
                                .setStatus_msg(message)
                                .set_reqid(reqid)
                                .build();
        }

        @PostMapping("/add")
        public ResponseEntity<Response<String>> getMethodName(@Valid @RequestBody Request<EmployeeDTO> request) {
                Response<String> response;

                // Check for duplicate employee ID

                if (employeeRepository.findByEmpid(request.getData().getEmpid()) != null) {
                        log.error("Duplicate employee ID, Add Employee failed");
                        return ResponseEntity.badRequest()
                                        .body(createErrorResponse("Duplicate employee ID", request.get_reqid()));
                }

                if (!service.addEmployee(request.getData())) {
                        log.error("System failure, Add Employee failed");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                                        .body(createErrorResponse("Unable to add data", request.get_reqid()));
                }

                response = new Response<String>().new Builder()
                                .setStatus("success")
                                .setStatus_code(HttpStatus.OK.value())
                                .setStatus_msg("Request completed successfully")
                                .set_reqid(request.get_reqid())
                                .setData("Employee added successfully")
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping(value = "/list", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
                        MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_PDF_VALUE })
        public ResponseEntity<?> getMethodName(@RequestParam("type") String type,
                        @RequestParam(value = "filter", required = false) String param,
                        @RequestParam(value = "reqid", required = false) String reqid) throws IOException {

                Response<List<EmployeeWrapper>> customResponse = new Response<List<EmployeeWrapper>>().new Builder()
                                .setStatus("success")
                                .setStatus_code(HttpStatus.OK.value())
                                .setStatus_msg("Request completed successfully")
                                .setData(service.getEmployeeWrapper(param.toUpperCase()))
                                .set_reqid(reqid)
                                .build();

                if ("xml".equalsIgnoreCase(type)) {
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML)
                                        .body(customResponse);
                } else if ("xlsx".equalsIgnoreCase(type)) {

                        byte[] excelContent = service.getEmployeeWrapperExcel(param.toUpperCase());

                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                                        "attachment; filename=\"employees.xlsx\"")
                                        .body(excelContent);
                } else if ("pdf".equalsIgnoreCase(type)) {
                        byte[] pdfContent = service.generatePDF(param.toUpperCase());
                        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                                        .body(pdfContent);
                }

                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(customResponse);
        }

        @GetMapping("/all")
        public ResponseEntity<Response<List<Employee>>> getMethodName() {

                Response<List<Employee>> response = new Response<List<Employee>>().new Builder()
                                .setStatus("success")
                                .setStatus_code(HttpStatus.OK.value())
                                .setStatus_msg("Request completed successfully")
                                .setData(employeeRepository.findAll())
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/get")
        public ResponseEntity<Response<Employee>> getOneEmployee(@RequestParam("id") String id) {

                // Check if the employee data is cached in Redis
                Employee cachedEmployee = redisService.getEmployeeValue(id);

                Employee employee;
                if (cachedEmployee != null) {
                        // If employee data is found in Redis, use it
                        employee = cachedEmployee;
                        log.info("Data read from redis");

                } else {
                        log.info("Data read from db");
                        // Otherwise, fetch from service and cache it
                        employee = service.getEmployee(id);
                        if (employee != null) {
                                log.info(employee.getFname());// Check if the employee exists before caching
                                redisService.setEmployeeKey(id, employee); // Cache the employee data
                        } else {
                                Response<Employee> response = new Response<Employee>().new Builder()
                                                .setStatus("failed")
                                                .setStatus_code(HttpStatus.BAD_REQUEST.value())
                                                .setStatus_msg("employee doesn't exists")
                                                .setData(employee)
                                                .build();

                                return ResponseEntity.ok(response);
                        }
                }

                Response<Employee> response = new Response<Employee>().new Builder()
                                .setStatus("success")
                                .setStatus_code(HttpStatus.OK.value())
                                .setStatus_msg("Request completed successfully")
                                .setData(employee)
                                .build();

                return ResponseEntity.ok(response);
        }

        @PutMapping("update/{id}")
        public ResponseEntity<Response<String>> putMethodName(@PathVariable String id,
                        @Valid @RequestBody Request<EmployeeDTO> request) {
                service.updateEmployee(id, request.getData(), request.getData().getClientReqId());
                Response<String> response = new Response<String>().new Builder()
                                .setStatus("success")
                                .setStatus_code(HttpStatus.OK.value())
                                .setStatus_msg("Request completed successfully")
                                .build();

                return ResponseEntity.ok(response);
        }

        @DeleteMapping("delete/{id}")
        public ResponseEntity<Response<String>> deleteMethodName(@PathVariable String id,
                        @RequestParam("reqid") String reqid) {
                service.deleteEmployee(id, reqid);
                Response<String> response = new Response<String>().new Builder()
                                .setStatus("success")
                                .setStatus_code(HttpStatus.OK.value())
                                .setStatus_msg("Request completed successfully")
                                .build();

                return ResponseEntity.ok(response);
        }

        @PostMapping("/updateEmployeeContribution")
        public ResponseEntity<Response<Map<String, Object>>> postMethodName(
                        @RequestBody Request<Map<String, Object>> data) {

                Response<Map<String, Object>> response = new Response<Map<String, Object>>().new Builder()
                                .setStatus("success")
                                .setStatus_code(HttpStatus.OK.value())
                                .setStatus_msg("Request completed successfully")
                                .setData(service.updateCount(data))
                                .build();

                return ResponseEntity.ok(response);

        }

        @GetMapping("/getContribution")
        public ResponseEntity<Response<Integer>> getMethodName(@RequestParam String dept, @RequestParam String empid) {

                Response<Integer> response = new Response<Integer>().new Builder()
                                .setStatus("success")
                                .setStatus_code(HttpStatus.OK.value())
                                .setStatus_msg("Request completed successfully")
                                .setData(service.getCount(dept, empid))
                                .build();

                return ResponseEntity.ok(response);
        }

}
