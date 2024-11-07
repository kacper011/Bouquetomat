package com.example.bouquetomat.service;

import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.model.Notification;
import com.example.bouquetomat.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(BouquetOrder order) {
        String subject = "Bukiet sprzedany!";
        String body = "Bukiet o numerze " + order.getBouquet().getSlotNumber() + " został sprzedany!";
        String notificationType = "EMAIL";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("bbouquetomat@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        Notification notification = new Notification(order, body, notificationType);
        notificationRepository.save(notification);
    }
}
