package com.example.demo.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailModel {
    private String to;
    private String subject;
    private String text;
}
