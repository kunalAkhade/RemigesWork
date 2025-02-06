package com.example.advancedjava.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.advancedjava.dto.Request;
import com.example.advancedjava.dto.Response;
import com.example.advancedjava.service.JwtService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtService service;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@RequestBody Request<Map<String, String>> request) {
        log.info("login worked");
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getData().get("empid"), null, null));
        if (authentication.isAuthenticated()) {
            String token = service.generateToken(request.getData().get("empid"));
            Response<String> customResponse = new Response<String>().new Builder()
                    .setStatus("success")
                    .setStatus_code(HttpStatus.OK.value())
                    .setStatus_msg("Request completed successfully")
                    .setData(token)
                    .set_reqid(request.get_reqid())
                    .build();
                    log.info("login 2 worked");
            return ResponseEntity.ok(customResponse);
        }

        Response<String> customResponse = new Response<String>().new Builder()
                    .setStatus("success")
                    .setStatus_code(HttpStatus.BAD_REQUEST.value())
                    .setStatus_msg("Request completed successfully")
                    .setData(null)
                    .set_reqid(request.get_reqid())
                    .build();
                    log.info("login 3 worked");
            return ResponseEntity.badRequest().body(customResponse);

    }

    @PostMapping("/sendMail")
    public String getMethodNames(@RequestBody Map<String,String> map) {

         SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kunalakhade35@gmail.com");  // Your email
        message.setTo(map.get("email"));  // Recipient's email address
        message.setSubject(map.get("subject"));  // Email subject
        message.setText(map.get("message"));  // Email body

        // Send the email
        javaMailSender.send(message);
        return "Success";
    }
    

    
}
