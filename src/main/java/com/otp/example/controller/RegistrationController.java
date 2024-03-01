package com.otp.example.controller;

import com.otp.example.entity.User;
import com.otp.example.service.EmailService;
import com.otp.example.service.EmailVerificationService;
import com.otp.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public Map<String,String> registerUser(@RequestBody User user){

        //Register the user without email verification
        User registeredUser=userService.registerUser(user);

        //Send otp email for email verification
       emailService.sendOtpEmail(user.getEmail());

       Map<String,String> response=new HashMap<>();
        response.put("status","sucess");
        response.put("message","User Registered sucessfully. Check your email for verification");

        return response;

    }

    @PostMapping("/verify-otp")
    public Map<String,String> verifyOtp(@RequestParam String email, @RequestParam String otp){
        return emailVerificationService.verifyOtp(email,otp);
    }


}
