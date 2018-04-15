package com.ptff.qsystem.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.Notification;
import com.ptff.qsystem.data.NotificationRepository;
import com.ptff.qsystem.data.NotificationStatus;
import com.ptff.qsystem.data.User;
import com.ptff.qsystem.data.UserGroupRepository;
import com.ptff.qsystem.data.UserRepository;
import com.ptff.qsystem.service.impl.NotificationService;
import com.ptff.qsystem.web.form.BroadcastNotificationForm;


@Controller
public class NotificationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	
	@RequestMapping(value="/notifications", params="group")
	public String groupNotification(@RequestParam("group") String group, Model model) {
		List<Notification> notifications = new ArrayList<Notification>();
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		for (GrantedAuthority ga : user.getAuthorities()) {
			notifications = notificationRepository.findFirst10ByUsergroupNameOrderByCreateDate(ga.getAuthority());
		}
		
		model.addAttribute("notifications", notifications);
		
		if (notifications.isEmpty())
			return "notification/notifications :: emptyNotifications";
		else
			return "notification/notifications :: notifications";
	}
	
	@RequestMapping(value="/notifications", params="user")
	public String userNotification(@RequestParam("user") String user, Model model) {	    
		List<Notification> notifications = new ArrayList<Notification>();
		
		User userObj = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		notifications = notificationRepository.findFirst10ByUserOrderByCreateDate(userObj);
		
		model.addAttribute("notifications", notifications);
		
		if (notifications.isEmpty())
			return "notification/notifications :: emptyNotifications";
		else
			return "notification/notifications :: notifications";
	}
	
	@RequestMapping(value="/notifications/{notificationId}/read")
	public String acceptNotification(@PathVariable("notificationId") Long notificationId, Model model) {	    
		Notification notification = notificationRepository.findOne(notificationId);
		notification.setStatus(NotificationStatus.READ);
		notificationRepository.save(notification);
		
		return "";
	}
	
	@RequestMapping(value="/notifications/settings")
	public String notificationSettings(Model model) {
		
		model.addAttribute("notification", new BroadcastNotificationForm());
		model.addAttribute("users", userRepository.findAll());
		model.addAttribute("usergroups", userGroupRepository.findAll());
		return "notification/settings";
	}
	
	@RequestMapping(value="/notifications/settings", method=RequestMethod.POST)
	public String broadcast(@ModelAttribute("notification") BroadcastNotificationForm notificationForm, BindingResult bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("users", userRepository.findAll());
			model.addAttribute("usergroups", userGroupRepository.findAll());
			return "notification/settings";
		}
		
		notificationService.broadcast(notificationForm.getMessage());
		
		return "notification/settings";
	}
	
    @MessageMapping("/notifications/unread")
    @SendToUser("/queue/notifications/unread")
    public Long countNotification(Principal principal) {
    	
    	User user = userRepository.findOne(principal.getName());
    	
    	return notificationService.countUnreadNotification(user);
    }

}
