package com.projects.OTP.service;

import com.projects.OTP.dao.OTPDao;
import com.projects.OTP.dto.OTPRequestDto;
import com.projects.OTP.dto.OTPSendResponseDto;
import com.projects.OTP.dto.OTPValidateResponseDto;
import com.projects.OTP.model.OTP;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {

    private final JavaMailSender mailSender;
    private final OTPDao otpDao;

    public OTPService(JavaMailSender mailSender, OTPDao otpDao) {
        this.mailSender = mailSender;
        this.otpDao = otpDao;
    }

    public Integer generateOtp() {
        Integer otp = null;
        Random random = new Random();
        otp = random.nextInt(8888) + 1000;
        return otp;
    }

    public ResponseEntity sendOTP(OTPRequestDto requestDto) {
        OTPSendResponseDto response = new OTPSendResponseDto();
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        if (requestDto.getEmailAddress() == null || requestDto.getEmailAddress().equals("")) {
            return ResponseEntity.badRequest().body("EMAIL IS EMPTY");
        }
        mailMessage.setFrom("tgotpservice@gmail.com");
        mailMessage.setTo(requestDto.getEmailAddress());
        mailMessage.setSubject("TG OTP SERVICE");
        mailMessage.setSentDate(new Date());

        //Create OTP
        Integer otp = generateOtp();
        String msg = "Your One Time Password is " + otp + ".";
        mailMessage.setText(msg);
        try {
            mailSender.send(mailMessage);
            response.setOtp(otp);
            response.setStatusCode(200);
            response.setMessage(msg);

            //Save OTP
            saveOTPtoDB(requestDto.getEmailAddress(), otp);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            response.setOtp(null);
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    //Save OTP
    public void saveOTPtoDB(String email, Integer otp) {
        OTP otpModel = new OTP();

        otpModel.setEmail(email);
        otpModel.setIsUsed(false);
        otpModel.setOtp(otp);
        otpModel.setTime(LocalTime.now());

        OTP savedOtp = otpDao.save(otpModel);
        System.out.println("OTP SAVED : " + savedOtp.getEmail());
    }

    public ResponseEntity validateOTP(String email, Integer otp) {
        OTPValidateResponseDto response = new OTPValidateResponseDto();

        if (email == null || otp == null || email.equals("")) {
            return ResponseEntity.badRequest().body("REQUEST FAILED");
        }

        Optional<OTP> otpOptional = otpDao.findByEmailAndOtpAndIsUsed(email, otp, false);
        if (otpOptional.isPresent()) {
            OTP otpModel = otpOptional.get();

            LocalTime now = LocalTime.now();
            LocalTime savedTime = otpModel.getTime();

            Duration duration = Duration.between(savedTime,now);
            if (duration.toMinutes() >= 10){
                return ResponseEntity.badRequest().body("OTP VALIDATION FAILED");
            }

            response.setOtp(otp);
            response.setStatus("OTP VALIDATED");

            otpModel.setIsUsed(true);
            OTP updatedOtp = otpDao.save(otpModel);
            System.out.println("UPDATED : FALSE CHANGED TO TRUE " + updatedOtp.getIsUsed());

            return ResponseEntity.ok().body(response);
        } else {
            response.setOtp(otp);
            response.setStatus("OTP VALIDATION FAILED");
            return ResponseEntity.ok().body(response);
        }
    }
}