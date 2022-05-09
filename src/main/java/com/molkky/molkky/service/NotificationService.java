package com.molkky.molkky.service;

import com.molkky.molkky.domain.Notification;
import com.molkky.molkky.domain.UserTournamentRole;
import com.molkky.molkky.repository.NotificationRepository;
import com.molkky.molkky.repository.UserTournamentRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserTournamentRoleRepository userTournamentRoleRepository;

    public Notification sendNotification(String message, String link, UserTournamentRole userTournamentRole) {
        Notification notification = new Notification();
        notification.setLink(link);
        notification.setMessage(message);
        notification.setUserTournamentRole(userTournamentRole);
        userTournamentRole.getNotifications().add(notification);
        userTournamentRoleRepository.save(userTournamentRole);
        return notificationRepository.save(notification);
    }

    public Integer getUnreadNotificationCount(UserTournamentRole userTournamentRole) {
        return Math.toIntExact(userTournamentRole.getNotifications().stream().filter(notification -> !notification.isRead()).count());
    }

    public void markNotificationAsRead(Notification notification) {
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}