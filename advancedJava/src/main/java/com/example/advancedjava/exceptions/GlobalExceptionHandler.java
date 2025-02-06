package com.example.advancedjava.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.advancedjava.dto.Request;
import com.example.advancedjava.dto.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectWordException.class)
    public ResponseEntity<Response<String>> handleResourceNotFoundException(IncorrectWordException ex) {
        Response<String> response = new Response<String>().new Builder()
            .setStatus("failed")
            .setStatus_code(400)
            .setStatus_msg(ex.getMessage())
            .set_reqid("123")
            .build();
        return ResponseEntity.badRequest().body(response);
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getDefaultMessage()) // Get the custom message
        .findFirst()
        .orElse("Validation failed");
       
        String reqid = null;

        // Get the target object from BindingResult and check its type
        Object target = ex.getBindingResult().getTarget();
        if (target instanceof Request request) { // Replace MyRequest with your actual request class
            reqid = request.get_reqid();
        }

        Response<String> response = new Response<String>().new Builder()
        .setStatus("failed")
        .setStatus_code(400)
        .setStatus_msg(errorMessage)
        .set_reqid(reqid)
        .build();
      return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Response<String> response = new Response<String>().new Builder()
        .setStatus("failed")
        .setStatus_code(400)
        .setStatus_msg("Input format mismatched"+":httpMessageReadException")
        .build();
      return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UpdateRuntimeException.class)
    public ResponseEntity<Response<String>> handleConstraintViolationException(UpdateRuntimeException ex) {
        Response<String> response = new Response<String>().new Builder()
        .setStatus("failed")
        .setStatus_code(400)
        .setStatus_msg(ex.getMessage())
        .set_reqid(ex.getReqid())
        .build();
      return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DeleteRuntimeException.class)
    public ResponseEntity<Response<String>> handleConstraintViolationException(DeleteRuntimeException ex) {
        Response<String> response = new Response<String>().new Builder()
        .setStatus("failed")
        .setStatus_code(400)
        .setStatus_msg(ex.getMessage())
        .set_reqid(ex.getReqid())
        .build();
      return ResponseEntity.badRequest().body(response);
    }
}
