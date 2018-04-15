package com.ptff.qsystem.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ptff.qsystem.data.Notification;
import com.ptff.qsystem.data.NotificationCategory;
import com.ptff.qsystem.data.NotificationRepository;
import com.ptff.qsystem.data.NotificationStatus;
import com.ptff.qsystem.data.User;
import com.ptff.qsystem.data.UserGroupRepository;
import com.ptff.qsystem.data.UserRepository;

@Service
public class NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private SimpMessagingTemplate simpleMessagingTemplate;


	public void broadcast(String message) {
		List<User> users = userRepository.findAll();
		for (User user : users) {
			Notification notification = new Notification();
			notification.setUser(user);
			notification.setMessage(message);
			notification.setCategory(NotificationCategory.BROADCAST);
			notification.setStatus(NotificationStatus.UNREAD);
			notificationRepository.save(notification);
			
			simpleMessagingTemplate.convertAndSendToUser(
					user.getUsername(),
					"/queue/notifications/unread",
					countUnreadNotification(user));
		}
	}


	public Long countUnreadNotification(User user) {
		return notificationRepository.countByUserAndStatus(user, NotificationStatus.UNREAD);
	}
}
