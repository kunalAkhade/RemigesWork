package com.example.advancedjava.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Rank {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String rankdesc;

    @JsonIgnore
    @OneToOne
    private Rank parentrankid;

    @JsonIgnore
    @OneToMany(mappedBy="rank")
    private Set<Employee> employees;

}
