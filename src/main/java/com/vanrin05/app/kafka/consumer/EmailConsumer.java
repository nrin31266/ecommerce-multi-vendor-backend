package com.vanrin05.app.kafka.consumer;

import com.vanrin05.app.service.impl.EmailService;
import com.vanrin05.event.SentLoginSignupEvent;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Service
@RequiredArgsConstructor
public class EmailConsumer {
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @KafkaListener(topics = "sent_otp_to_login_signup", groupId = "main")
    public void sentOtpLoginSignup(SentLoginSignupEvent sentLoginSignupEvent, Acknowledgment ack) throws MessagingException {
        ack.acknowledge();
        Context context = new Context();
        context.setVariables(sentLoginSignupEvent.getVariables());
        emailService.sendVerificationOtpEmail(sentLoginSignupEvent.getEmail(),
                sentLoginSignupEvent.getSubject(), templateEngine.process("sent_otp_to_login_signup", context));
    }
}
