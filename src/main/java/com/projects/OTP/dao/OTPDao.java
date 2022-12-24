package com.projects.OTP.dao;

import com.projects.OTP.model.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPDao extends MongoRepository<OTP,String> {

    Optional<OTP> findByEmailAndOtpAndIsUsed(String email, Integer otp,Boolean isUsed);

}