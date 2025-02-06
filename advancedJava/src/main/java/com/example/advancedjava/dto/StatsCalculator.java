package com.example.advancedjava.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StatsCalculator {

    @NotNull(message = "The number must be provided.")
    private List<@NotNull(message = "Each float value must not be null.")Float> list;
    @NotBlank(message="The operation is empty")
    @NotNull(message = "The operation is null")
    private String operation;

    public StatsCalculator(List<Float> list, String operation) {
        this.list = list;
        this.operation = operation;
    }
    public List<Float> getList() {
        return list;
    }
    public void setList(List<Float> list) {
        this.list = list;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }

   

    

    
    
}
