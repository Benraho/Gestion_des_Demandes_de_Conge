package com.example.MiniProject.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerNotification(String destinataire, String sujet, String message ){
        SimpleMailMessage email =new SimpleMailMessage();
        email.setTo(destinataire);
        email.setSubject(sujet);
        email.setText(message);
        mailSender.send(email);
     }

}
