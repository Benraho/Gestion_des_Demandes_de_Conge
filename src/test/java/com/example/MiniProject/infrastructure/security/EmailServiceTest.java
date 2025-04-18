package com.example.MiniProject.infrastructure.security;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testEnvoyerNotification() {
        String destinataire = "test@example.com";
        String sujet = "Test Subject";
        String message = "Test Message";

        emailService.envoyerNotification(destinataire, sujet, message);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
