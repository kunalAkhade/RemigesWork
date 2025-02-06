package com.example.advancedjava.dto;


import jakarta.validation.constraints.NotNull;

public class Calculator {


    @NotNull(message="Required operand")
    private Float operandFirst;
    
    @NotNull(message="Required operand")
    private Float operandSecond;
  
    @NotNull(message="Required operand")
    private String operation;

    

    public Calculator() {
    }

    


    public Calculator(Float operandFirst, Float operandSecond, String operation) {
        this.operandFirst = operandFirst;
        this.operandSecond = operandSecond;
        this.operation = operation;
    }




    public Float getOperandFirst() {
        return operandFirst;
    }



    public void setOperandFirst(Float operandFirst) {
        this.operandFirst = operandFirst;
    }



    public Float getOperandSecond() {
        return operandSecond;
    }



    public void setOperandSecond(Float operandSecond) {
        this.operandSecond = operandSecond;
    }



    public String getOperation() {
        return operation;
    }



    public void setOperation(String operation) {
        this.operation = operation;
    }

   
    
   
    
    
}
