package com.example.advancedjava.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.advancedjava.model.EmployeeShadow;

public interface EmployeeShadowDao extends JpaRepository<EmployeeShadow, Long>{
    
}
