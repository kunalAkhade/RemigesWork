package com.example.advancedjava.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement
public class EmployeeWrapper {

 
    private Long id;

    private String empid;


    private String fname;

    

    public EmployeeWrapper() {
    }


    public EmployeeWrapper(Long id, String empid, String fname) {
        this.id = id;
        this.empid = empid;
        this.fname = fname;
    }

  
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    
    
}
