package com.ptff.qsystem.web;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ptff.qsystem.data.CustomerRepository;
import com.ptff.qsystem.data.CustomerStatus;
import com.ptff.qsystem.data.User;


@Controller
public class DashboardController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping("")
	public String showDashoard(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		LOGGER.info("Authentication object: {}", auth.getPrincipal());
		
		if (auth != null) {
			if(isRolePresent(auth.getAuthorities(), "ROLE_ADMIN")) {
				return "dashboard/admin_index";
			} else if(isRolePresent(auth.getAuthorities(), "ROLE_SALESPERSON")) {
				model.addAttribute("draftCustomers", customerRepository.findByStatusAndSalespersons(CustomerStatus.DRAFT, (User)auth.getPrincipal()));
				model.addAttribute("awatingApprovalCustomers", customerRepository.findByStatusAndSalespersons(CustomerStatus.AWAITING_APPROVAL, (User)auth.getPrincipal()));
				model.addAttribute("rejectedCustomers", customerRepository.findByStatusAndSalespersons(CustomerStatus.DRAFT, (User)auth.getPrincipal()));
				return "dashboard/sp_index";
			} else if(isRolePresent(auth.getAuthorities(), "ROLE_SALESDIRECTOR")) {
				model.addAttribute("awatingApprovalCustomers", customerRepository.findByStatus(CustomerStatus.AWAITING_APPROVAL));
				return "dashboard/sd_index";
			} else if(isRolePresent(auth.getAuthorities(), "ROLE_BOSS")) {
				return "dashboard/boss_index";
			} else if(isRolePresent(auth.getAuthorities(), "ROLE_IT")) {
				return "dashboard/it_index";
			} else if (isRolePresent(auth.getAuthorities(), "ROLE_ANONYMOUS")) {
				return "redirect:/login";
			} else { // Made for SU
				return "dashboard/it_index";
			}
		} else {
			return "redirect:/login";
		}

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
