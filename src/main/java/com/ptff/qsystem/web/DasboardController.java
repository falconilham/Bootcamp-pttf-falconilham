package com.ptff.qsystem.web;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DasboardController {
	@RequestMapping("/")
	public String showDashoard(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if(isRolePresent(auth.getAuthorities(), "ROLE_ADMIN")) {
				return "dashboard/admin_index";
			} else if(isRolePresent(auth.getAuthorities(), "ROLE_HR")) {
				return "dashboard/hr_index";
			} else if(isRolePresent(auth.getAuthorities(), "ROLE_PAYROLL")) {
				return "dashboard/payroll_index";
			} else if(isRolePresent(auth.getAuthorities(), "ROLE_BOSS")) {
				return "dashboard/boss_index";
			} else if(isRolePresent(auth.getAuthorities(), "ROLE_IT")) {
				return "dashboard/it_index";
			} else {
				return "dashboard/it_index";
			}
		}

		return "redirect:/login.html";
	}

	private boolean isRolePresent(Collection<? extends GrantedAuthority> collection, String role) {
		boolean isRolePresent = false;
		for (GrantedAuthority grantedAuthority : collection) {
			isRolePresent = grantedAuthority.getAuthority().equals(role);
			if (isRolePresent)
				break;
		}
		return isRolePresent;
	}
}
