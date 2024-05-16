package com.cognizant.EventPlanner.services;

import com.cognizant.EventPlanner.dto.email.BaseEmailDetailsDto;
import com.cognizant.EventPlanner.dto.email.EmailType;
import com.cognizant.EventPlanner.strategy.EmailStrategy;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final List<EmailStrategy> strategies;
    private final Map<EmailType, EmailStrategy> emailStrategies = new HashMap<>();

    @PostConstruct
    private void init() {
        strategies.forEach(strategy -> emailStrategies.put(strategy.getEmailType(), strategy));
    }

    @Override
    public void sendEmail(BaseEmailDetailsDto emailDetailsDto) {
        EmailStrategy strategy = emailStrategies.get(emailDetailsDto.getEmailType());

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported email type");
        }

        try {
            MimeMessage message = prepareMessage(emailDetailsDto, strategy);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private MimeMessage prepareMessage(BaseEmailDetailsDto emailDetailsDto, EmailStrategy strategy) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailDetailsDto.getRecipientEmail());
        helper.setSubject(strategy.getSubject());
        helper.setText(prepareEmailContent(strategy, emailDetailsDto), true);
        return message;
    }

    private String prepareEmailContent(EmailStrategy strategy, BaseEmailDetailsDto emailDetailsDto) {
        Context context = new Context(Locale.ENGLISH, strategy.getProperties(emailDetailsDto));
        return templateEngine.process(strategy.getTemplateName(), context);
    }

}
