package com.vanrin05.app.kafka.consumer;

import com.vanrin05.app.service.impl.EmailService;
import com.vanrin05.event.SentEmailEvent;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Service
@RequiredArgsConstructor
public class EmailConsumer {
    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @KafkaListener(topics = "sent_otp_to_login_signup", groupId = "main")
    public void sentOtpLoginSignup(SentEmailEvent sentLoginSignupEvent, Acknowledgment ack) throws MessagingException {
        ack.acknowledge();
        Context context = new Context();
        context.setVariables(sentLoginSignupEvent.getVariables());
        emailService.sendEmail(sentLoginSignupEvent.getEmail(),
                sentLoginSignupEvent.getSubject(), templateEngine.process("sent_otp_to_login_signup", context));
    }

    @KafkaListener(topics = "sent_confirm_seller_order", groupId = "main")
    public void sentConfirmSellerOrder(SentEmailEvent sentEmailEvent, Acknowledgment ack) throws MessagingException {
        ack.acknowledge();
        Context context = new Context();
        context.setVariables(sentEmailEvent.getVariables());
        emailService.sendEmail(sentEmailEvent.getEmail(),
                sentEmailEvent.getSubject(), templateEngine.process("sent_confirm_seller_order", context));
    }
}
