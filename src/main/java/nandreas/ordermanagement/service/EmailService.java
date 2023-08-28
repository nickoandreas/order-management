package nandreas.ordermanagement.service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nandreas.ordermanagement.dto.EmailAttribute;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@AllArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    private final ValidationService validationService;

    public void sendEmail(String template, Context context, EmailAttribute emailAttribute) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            this.validationService.validate(emailAttribute);

            String htmlContent = this.templateEngine.process(template, context);

            helper.setFrom(emailAttribute.getFrom());
            helper.setTo(emailAttribute.getTo());
            helper.setSubject(emailAttribute.getSubject());
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
