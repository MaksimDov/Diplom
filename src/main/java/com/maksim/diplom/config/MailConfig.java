package com.maksim.diplom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
/**
 * Класс-конфигурация Mail config.
 */
@Configuration
public class MailConfig {
    /**
     * Стандартный метод, который позволяет создавать простые автоматизированные контроллеры,
     * предварительно настроенные с кодом состояния и/или видом.
     */
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("callboarddiplom@gmail.com");
        mailSender.setPassword("Maksim777");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
