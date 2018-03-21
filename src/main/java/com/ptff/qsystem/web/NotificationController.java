package com.ptff.qsystem.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotificationController {
	@RequestMapping(value="/notifications", params="group")
	public String groupNotification(@RequestParam("group") String group, Model model) {
			    
		return "/notification/notifications :: notifications";
	}
	
	@RequestMapping(value="/notifications", params="user")
	public String userNotification(@RequestParam("user") String user, Model model) {
			    
		return "/notification/notifications :: notifications";
	}
	
	
}
