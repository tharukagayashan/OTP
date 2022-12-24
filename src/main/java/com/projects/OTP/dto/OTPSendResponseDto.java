package com.projects.OTP.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OTPSendResponseDto {

    private Integer otp;
    private Integer statusCode;
    private String message;

}