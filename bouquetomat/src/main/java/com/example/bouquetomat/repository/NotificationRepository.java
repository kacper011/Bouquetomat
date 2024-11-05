package com.example.bouquetomat.repository;

import com.example.bouquetomat.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
