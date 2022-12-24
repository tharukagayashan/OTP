package com.projects.OTP.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "OTP")
public class OTP {

    @Id
    private String id;

    @Field
    private Integer otp;

    @Field
    private String email;

    @Field
    private Boolean isUsed;

    @Field
    private LocalTime time;

}
