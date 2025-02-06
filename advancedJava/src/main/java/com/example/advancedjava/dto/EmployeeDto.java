package com.example.advancedjava.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class EmployeeDto {

    
    @NotBlank(message = "Employee ID is mandatory")
    @JsonProperty(required = false)
    private String empid;

    @NotBlank(message = "First name cannot be empty")
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only letters")
    @JsonProperty(required = false)
    private String fname;

    @JsonProperty(required = false)
    private String fullname;

    
    //@NotBlank(message = "Date of Birth is mandatory")
    @Past(message = "Date of Birth must be in the past")
    @JsonProperty(required = false)
    private LocalDate dob; // LocalDate is appropriate here

    
   // @NotBlank(message = "Date of Joining is mandatory")
    @Past(message = "Date of Joining must be in the past")
    @JsonProperty(required = false)
    private LocalDate doj; // LocalDate is appropriate here

    
   // @NotBlank(message = "Salary is mandatory")
    @Positive(message = "Salary must be greater than zero")
    @JsonProperty(required = false)
    private Long salary;

    @JsonProperty(required = false)
    private Long reportsto;
    @JsonProperty(required = false)
    private Long department;
    @JsonProperty(required = false)
    private Long rank;
    @JsonProperty(required = false)
    private String clientReqId;

    public EmployeeDto(@NotBlank(message = "Employee ID is mandatory") String empid,
            @NotBlank(message = "First name cannot be empty") @Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only letters") String fname,
            String fullname,
            @NotNull(message = "Date of Birth is mandatory") @Past(message = "Date of Birth must be in the past") LocalDate dob,
            @NotNull(message = "Date of Joining is mandatory") @Past(message = "Date of Joining must be in the past") LocalDate doj,
            @NotNull(message = "Salary is mandatory") @Positive(message = "Salary must be greater than zero") Long salary,
            Long reportsto, Long department, Long rank, String clientReqId) {
        this.empid = empid;
        this.fname = fname;
        this.fullname = fullname;
        this.dob = dob;
        this.doj = doj;
        this.salary = salary;
        this.reportsto = reportsto;
        this.department = department;
        this.rank = rank;
        this.clientReqId = clientReqId;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDate getDoj() {
        return doj;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getReportsto() {
        return reportsto;
    }

    public void setReportsto(Long reportsto) {
        this.reportsto = reportsto;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public String getClientReqId() {
        return clientReqId;
    }

    public void setClientReqId(String clientReqId) {
        this.clientReqId = clientReqId;
    }

}
