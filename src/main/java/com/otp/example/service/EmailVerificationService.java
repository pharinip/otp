package com.otp.example.service;

import com.otp.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailVerificationService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    static final Map<String,String> emailOtpMapping= new HashMap<>();

    public Map<String,String> verifyOtp(String email,String otp){
        String storedOtp=emailOtpMapping.get(email);
        Map<String,String> response = new HashMap<>();
        if(storedOtp != null && storedOtp.equals(otp)){

            //Fetch user by email and mark email as verified
            //logger.info("OTP is valid. Proceeding with verification");
            User user=userService.getUserByEmail(email);

            if(user != null){

                //once the otp is verified before we update the user the otp is removed from HashMap
                emailOtpMapping.remove(email);


                userService.verifyEmail(user);
                response.put("status","sucess");
                response.put("message","Email verified Sucessfully");
            }else{
                //logger.error("Invalid OTP receiveeed for email: {}",email);
                response.put("status","error");
                response.put("message","user not found");
            }
        }else{
            response.put("status","error");
            response.put("message","Invalid OTP");
        }
        return response;
    }

    public Map<String,String> sendOtpForLogin(String email){
        if(userService.isEmailVerified(email)){
            String otp=emailService.generateOtp();
            emailOtpMapping.put(email,otp);

            //Send otp to the usrs email
            emailService.sendOtpEmail(email);

            Map<String,String>   response =new HashMap<>();
            response.put("status","sucess");
            response.put("message","otp sent sucessfully");
            return response;

        }else{
            Map<String,String> response=new HashMap<>();
            response.put("status","error");
            response.put("message", "Email is not verified");
            return  response;
        }
    }

    public Map<String, String> verifyOtpForLogin(String email, String otp) {

        String storedOtp=emailOtpMapping.get(email);

        Map<String,String> response=new HashMap<>();

        if(storedOtp !=null && storedOtp.equals(otp)){
            emailOtpMapping.remove(email);
            //OTP is valid
            response.put("status","sucess");
            response.put("message","otp is verified sucessfully");
        }else{
            //Invalid OTP
            response.put("status","error");
            response.put("message","Invalid Otp");
        }

        return response;
    }
}
