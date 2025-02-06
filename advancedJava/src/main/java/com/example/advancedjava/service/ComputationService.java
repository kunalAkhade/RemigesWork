package com.example.advancedjava.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ComputationService {

    public Float calculate(Float first, Float second, String operation){

        Float result;
        switch (operation.trim()) {
            case "+" -> {
                result=first+second;
            }
            case "-" -> {
                result=first-second;
            }
            case "*" -> {
                result=first*second;
            }
            case "/" -> {
                
                result=first/second;
            }
            default ->{
                log.error("No valid operand found for the operation");
                throw new AssertionError();

            } 
        }
        return (float) (Math.round(result * 100) / 100.0);

    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
    public Float calculate(List<Float> list, String operaton){

        Float result;
        switch (operaton.trim().toUpperCase()) {
            case "MEAN" -> {
                Float sum=(float) list.stream()
                .mapToDouble(Float::floatValue)
                .sum();
                result=sum/list.size();
            }
            case "SUM" -> {
                result= (float) list.stream()
                .mapToDouble(Float::floatValue)
                .sum();
            }
            case "MIN" -> {
                result=list.stream()
                .min(Float::compareTo).get();
            }
            case "MAX" -> {
                result=list.stream()
                .max(Float::compareTo).get();
            }
            default -> {
                log.error("No valid operand found for the operation");
                throw new AssertionError();
            }
        }
        return (float) (Math.round(result * 100) / 100.0);

    }


   
    
}
