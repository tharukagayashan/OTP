package com.projects.OTP.rest;

import com.projects.OTP.dto.OTPRequestDto;
import com.projects.OTP.dto.OTPSendResponseDto;
import com.projects.OTP.dto.OTPValidateResponseDto;
import com.projects.OTP.service.OTPService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OTPController {

    private final OTPService otpService;

    public OTPController(OTPService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<OTPSendResponseDto> sendOTP(@RequestBody OTPRequestDto requestDto) {
        ResponseEntity response = otpService.sendOTP(requestDto);
        return response;
    }

    @GetMapping("/validate/email/{email}/otp/{otp}")
    public ResponseEntity<OTPValidateResponseDto> validateOTP(@PathVariable String email, @PathVariable Integer otp) {
        ResponseEntity response = otpService.validateOTP(email, otp);
        return response;
    }

}
