package com.projects.OTP.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OTPValidateResponseDto {

    private String status;
    private Integer otp;

}