package com.example.bouquetomat.service;

import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.model.Notification;
import com.example.bouquetomat.repository.NotificationRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public void sendNotification(BouquetOrder order) {
        String subject = "Bouquet sold!";
        String body = "Bouquet number " + order.getBouquet().getSlotNumber() + " has been sold!";
        String notificationType = "EMAIL";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("bbouquetomat@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Email sent to: bbouquetomat@gmail.com");

        Notification notification = new Notification(order, body, notificationType);
        notificationRepository.save(notification);
        System.out.println("Notification saved in the database: " + notification);
    }
}
