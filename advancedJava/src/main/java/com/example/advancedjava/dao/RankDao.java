package com.example.advancedjava.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.advancedjava.model.Rank;

@Repository
public interface  RankDao extends JpaRepository<Rank, Long> {
    
}
