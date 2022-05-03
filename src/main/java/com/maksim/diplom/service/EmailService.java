package com.maksim.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки почты
 */
@Service
public class EmailService {

    @Autowired
    public JavaMailSender emailSender;

    /**
     * Метод отправки почты
     * @param subject тема
     * @param text сообщение
     * @param to получатель
     */
    public void sendSimpleMessage(String subject, String text, String to){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
