package com.example.advancedjava.Validators;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Validator {

    
    public boolean validateFloatingPoint(String operand){
        return operand.matches("[-+]?[0-9]*[.]?[0-9]*");
    }

    public boolean validateWord(String operand){
        return operand.matches("[a-zA-Z]+");
    }

    public boolean validateFloatingList(List<String> list){

        for(String operand:list){
          if(!validateFloatingPoint(operand)){
            return false;
          }  
        }
        return true;
   }

  
    
}
