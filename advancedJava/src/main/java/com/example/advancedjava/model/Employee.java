package com.example.advancedjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class Employee {

    @Id
    @SequenceGenerator(name = "seqGen", sequenceName = "employee_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    private Long id;

    @Column(nullable = false, unique = true)
    private String empid;

    @Column(nullable = false)
    private String fname;

    private String fullname;

    // @JsonIgnore
    @Column(nullable = false)
    private LocalDate dob;

    // @JsonIgnore
    @Column(nullable = false)
    private LocalDate doj;

    @Column(nullable = false)
    private Long salary;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reportsto")
    private Employee reportsto;

    @OneToMany(mappedBy = "reportsto", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private Set<Employee> subordinates = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "department")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "rank")
    private Rank rank;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;

    @Column(name = "client_reqid", nullable = false)
    private String clientReqId;

    @JsonIgnore
    @OneToMany(mappedBy = "reportsto")
    private Set<EmployeeShadow> employeesShadow;


    @Override
    public int hashCode() {
        return Objects.hash(empid); // Use a unique identifier
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(empid, employee.empid); // Compare unique field
    }

}
