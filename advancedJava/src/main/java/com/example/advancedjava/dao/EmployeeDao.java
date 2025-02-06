package com.example.advancedjava.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.advancedjava.model.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long>{
    Employee findByEmpid(String empid);

    @Query(value = "SELECT * FROM employee WHERE fname LIKE CONCAT('%',:filter,'%')", nativeQuery = true)
    List<Employee> findEmployeeByRegex(@Param("filter") String filter);
    
  
    
}
