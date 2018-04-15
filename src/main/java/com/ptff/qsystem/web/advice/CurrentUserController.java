package com.ptff.qsystem.web.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ptff.qsystem.data.NotificationStatus;
import com.ptff.qsystem.data.User;
import com.ptff.qsystem.data.UserRepository;
import com.ptff.qsystem.service.impl.NotificationService;

@ControllerAdvice
public class CurrentUserController {
	@Autowired
	private NotificationService notificationService;
	
	@ModelAttribute("unreadNotificationCount")
	public Long getCurrentUser(@AuthenticationPrincipal User user) {
    	if (user != null) {
    		return notificationService.countUnreadNotification(user);
    	}
    	
    	return 0L;
	}
}
