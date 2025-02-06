package com.example.advancedjava.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.advancedjava.model.Department;

@Repository
public interface DepartmentDao extends JpaRepository<Department,Long>{

}

