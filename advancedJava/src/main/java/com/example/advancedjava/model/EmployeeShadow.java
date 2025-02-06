package com.example.advancedjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="employee_shadow")
public class EmployeeShadow {

    @Id
    @SequenceGenerator(name="seqGen", sequenceName = "employee_id_seq",initialValue=1, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator="seqGen")
    private Long id;


    @Column(nullable=false)
    private String empid;

    @Column(nullable=false)
    private String fname;

    private String fullname;

    @Column(nullable=false)
    private LocalDate dob;

    @Column(nullable=false)
    private LocalDate doj;

    @Column(nullable=false)
    private Long salary;

    @ManyToOne
    @JoinColumn(name="reportsto")
    private Employee reportsto;

    @ManyToOne 
    @JoinColumn(name="department")
    private Department department;

    @ManyToOne
    @JoinColumn(name="rank")
    private Rank rank;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt; 

    @Column(name = "updatedat")
    private LocalDateTime updatedAt; 

    @Column(name = "client_reqid", nullable = false)
    private String clientReqId; 
    
}
