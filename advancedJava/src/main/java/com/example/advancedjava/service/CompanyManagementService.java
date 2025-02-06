package com.example.advancedjava.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.advancedjava.dao.DepartmentDao;
import com.example.advancedjava.dao.EmployeeDao;
import com.example.advancedjava.dao.EmployeeShadowDao;
import com.example.advancedjava.dao.RankDao;
import com.example.advancedjava.dto.EmployeeDTO;
import com.example.advancedjava.dto.EmployeeWrapper;
import com.example.advancedjava.dto.Request;
import com.example.advancedjava.exceptions.DeleteRuntimeException;
import com.example.advancedjava.exceptions.UpdateRuntimeException;
import com.example.advancedjava.model.Department;
import com.example.advancedjava.model.Employee;
import com.example.advancedjava.model.EmployeeShadow;
import com.example.advancedjava.model.Rank;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompanyManagementService {

    @Autowired
    private EmployeeDao employeeRepository;

    @Autowired
    private DepartmentDao departmentRepository;

    @Autowired
    private RankDao rankRepository;

    @Autowired
    private EmployeeShadowDao employeeShadowDao;

    @Autowired
    private RedisService service;

    public boolean addEmployee(EmployeeDTO employeeDTO) {
        try {
            Employee employee = new Employee();

            employee.setEmpid(employeeDTO.getEmpid().trim().toUpperCase());
            employee.setFname(employeeDTO.getFname().trim().toUpperCase());
            employee.setFullname(employeeDTO.getFullname().trim().toUpperCase());
            employee.setDob(employeeDTO.getDob());
            employee.setDoj(employeeDTO.getDoj());
            employee.setSalary(employeeDTO.getSalary());

            // Fetch related entities by ID and set them
            if (employeeDTO.getReportsto() != null) {
                employee.setReportsto(employeeRepository.findById(employeeDTO.getReportsto()).orElse(null));
            }
            if (employeeDTO.getDepartment() != null) {
                employee.setDepartment(departmentRepository.findById(employeeDTO.getDepartment()).orElse(null));
            }
            if (employeeDTO.getRank() != null) {
                employee.setRank(rankRepository.findById(employeeDTO.getRank()).orElse(null));
            }

            employee.setCreatedAt(LocalDateTime.now());
            employee.setUpdatedAt(LocalDateTime.now());
            employee.setClientReqId(employeeDTO.getClientReqId().trim().toUpperCase());

            employeeRepository.save(employee);
            log.info(employee.getEmpid() + " employee added successfully");
            return true;

        } catch (Exception e) {
            // Log the exception (consider using a logging framework)
            log.error(e.getMessage()); // For debugging
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<EmployeeWrapper> getEmployeeWrapper(String regex) {

        List<Employee> list = null;
        Object object = service.getEmployeeListValue(regex);
        if (object != null) {
            if (object instanceof List<?> emplist) {
                List<?> elist = (List<?>) emplist;
                list = (List<Employee>) elist;
                log.info("Data read from redis");
            }
        } else {
            log.info("Data read from DB");
            list = employeeRepository.findEmployeeByRegex(regex);
            service.setEmployeeListKey(regex, list);
        }

        if (list == null) {
            return new ArrayList<>(); // Return an empty list if no employees are found
        }

        ArrayList<EmployeeWrapper> arrayList = new ArrayList<>();
        for (Employee employee : list) {
            arrayList.add(new EmployeeWrapper(employee.getId(),
                    employee.getEmpid(),
                    employee.getFname()));
        }
        return arrayList;
    }

    public Employee getEmployee(String id) {
        try {
            Employee emp;
            emp = employeeRepository.findByEmpid(id);
            return emp;
        } catch (Exception e) {
            log.error("failed to find employee by id");
            throw new RuntimeException("failed to find employee by id");
        }

    }

    public void updateEmployee(String id, EmployeeDTO employeeDto, String reqid) {
        try {
            backup(id);
            Employee employee = employeeRepository.findByEmpid(id);

            // Update fields if they are provided in the request
            if (employeeDto.getEmpid() != null) {
                employee.setEmpid(employeeDto.getEmpid().toUpperCase());
            }
            if (employeeDto.getFname() != null) {
                employee.setFname(employeeDto.getFname().toUpperCase());
            }
            if (employeeDto.getFullname() != null) {
                employee.setFullname(employeeDto.getFullname().toUpperCase());
            }
            if (employeeDto.getDob() != null) {
                employee.setDob(employeeDto.getDob());
            }
            if (employeeDto.getDoj() != null) {
                employee.setDoj(employeeDto.getDoj());
            }
            if (employeeDto.getSalary() != null) {
                employee.setSalary(employeeDto.getSalary());
            }
            if (employeeDto.getReportsto() != null) {
                Optional<Employee> reportsTo = employeeRepository.findById(employeeDto.getReportsto());
                reportsTo.ifPresent(employee::setReportsto);
            }
            if (employeeDto.getDepartment() != null) {
                Optional<Department> department = departmentRepository.findById(employeeDto.getDepartment());
                department.ifPresent(employee::setDepartment);
            }
            if (employeeDto.getRank() != null) {
                Optional<Rank> rank = rankRepository.findById(employeeDto.getRank());
                rank.ifPresent(employee::setRank);
            }
            if (employeeDto.getClientReqId() != null) {
                employee.setClientReqId(employeeDto.getClientReqId());
            }

            // Update the updatedAt timestamp

            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
            log.info(employee.getEmpid() + "employee updated successfully");
            service.removeEmployee(id);
            service.setEmployeeKey(employee.getEmpid()  , employee);
            log.info(employee.getEmpid()+" is updated in redis");

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UpdateRuntimeException(e.getMessage(), reqid);
        }

    }
    
    public void deleteEmployee(String id, String reqid) {

        try {
            backup(id);
            Employee employee = employeeRepository.findByEmpid(id);
            if (employee == null) {
                throw new Exception("No employee found with the id");
            }

            for (Employee e: employee.getSubordinates()) {
                e.setReportsto(null);
            }  
            
            for (EmployeeShadow e: employee.getEmployeesShadow()) {
                e.setReportsto(null);
            }
            employeeRepository.deleteById(employee.getId());

            service.removeEmployee(id);
            log.info(id + " employee deleted successfully");


        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DeleteRuntimeException(e.getMessage(), reqid);
        }

    }

    private void backup(String empid) {
        Employee employee = employeeRepository.findByEmpid(empid);

        EmployeeShadow employeeShadow = new EmployeeShadow();
        employeeShadow.setId(employee.getId());
        employeeShadow.setEmpid(employee.getEmpid());
        employeeShadow.setFname(employee.getFname());
        employeeShadow.setFullname(employee.getFullname());
        employeeShadow.setSalary(employee.getSalary());
        employeeShadow.setDoj(employee.getDob());
        employeeShadow.setDob(employee.getDob());
        employeeShadow.setClientReqId(employee.getClientReqId());
        employeeShadow.setDepartment(employee.getDepartment());
        employeeShadow.setRank(employee.getRank());
        employeeShadow.setReportsto(employee.getReportsto());
        employeeShadow.setCreatedAt(LocalDateTime.now());
        employeeShadow.setUpdatedAt(employeeShadow.getCreatedAt());
        employeeShadowDao.save(employeeShadow);

        log.info("Backup before updation is performed");

    }

    @SuppressWarnings("unchecked")
    public byte[] getEmployeeWrapperExcel(String regex) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Employee Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Employee ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Department");

            // Add some sample data
            List<Employee> list = null;
            Object object = service.getEmployeeListValue(regex);
            if (object != null) {
                if (object instanceof List<?> emplist) {
                    List<?> elist = (List<?>) emplist;
                    list = (List<Employee>) elist;
                    log.info("Data read from redis");
                }
            } else {
                log.info("Data read from DB");
                list = employeeRepository.findEmployeeByRegex(regex);
                service.setEmployeeListKey(regex, list);
            }

            if (list == null) {
                return new byte[0]; // Return an empty list if no employees are found
            }
            int rowCount = 1;
            for (Employee employee : list) {
                Row dataRow = sheet.createRow(rowCount++);
                dataRow.createCell(0).setCellValue(employee.getEmpid());
                dataRow.createCell(1).setCellValue(employee.getFname());
                dataRow.createCell(2).setCellValue(
                        departmentRepository.findById(employee.getDepartment().getId()).get().getDeptname());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    public byte[] generatePDF(String regex) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        try (PdfDocument pdfDocument = new PdfDocument(writer); Document document = new Document(pdfDocument)) {
            
            float[] pointColumnWidths = { 150F, 150F, 150F };
            Table table = new Table(pointColumnWidths);
            table.addCell(new Cell().add("Employee ID"));
            table.addCell(new Cell().add("Employee Name"));
            table.addCell(new Cell().add("Department"));
            
            List<Employee> list = null;
            Object object = service.getEmployeeListValue(regex);
            if (object != null) {
                if (object instanceof List<?> emplist) {
                    List<?> elist = (List<?>) emplist;
                    list = (List<Employee>) elist;
                    log.info("Data read from redis");
                }
            } else {
                log.info("Data read from DB");
                list = employeeRepository.findEmployeeByRegex(regex);
                service.setEmployeeListKey(regex, list);
            }
            
            if (list == null) {
                document.close();
                return new byte[0];// Return an empty list if no employees are found
            }
            
            for (Employee employee : list) {
                table.addCell(new Cell().add(employee.getEmpid()));
                table.addCell(new Cell().add(employee.getFname()));
                table.addCell(new Cell().add(employee.getDepartment().getDeptname()));
            }
            
            document.add(table);
            
        }

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        return pdfBytes;

    }

    public Map<String, Object> updateCount(Request<Map<String, Object>> data) {
        String key = data.getData().get("dept") + "." + data.getData().get("empID");
        if (service.doesUserKeyExist(key)) {
            if (data.getData().get("count") != null) {
                service.incrementUserValue(key, (Integer) data.getData().get("count"));
            } else {
                service.incrementUserValue(key);
            }

        } else {
            service.setUserKey(key);

        }
        Map<String, Object> map = new HashMap<>();
        map.put("dept", data.getData().get("dept"));
        map.put("empID", data.getData().get("empID"));
        map.put("count", service.getUserValue(key));
        return map;

    }

    public Integer getCount(String dept, String empid) {
        return service.getUserValue(dept, empid);
    }

}
