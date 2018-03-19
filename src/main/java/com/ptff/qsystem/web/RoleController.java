package com.ptff.qsystem.web;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.UserGroup;
import com.ptff.qsystem.data.UserGroupRepository;



@Controller
public class RoleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20 };
	
	@Autowired
	private UserGroupRepository userGroupRepository;

	@RequestMapping("/roles")
	public String listRoles(
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		
		Page<UserGroup> roles = userGroupRepository.findAll(pageable);
		
		model.addAttribute("roles", roles);
		
		return "security/role/index";
	}
	
	@RequestMapping("/roles/new")
	public String newRole(Model model) {
		
		model.addAttribute("role", new UserGroup());
		
		return "security/role/new";
	}
	
	@RequestMapping("/roles/save")
	public String saveRole(UserGroup role, BindingResult bindingResult) {
		LOGGER.info("Saving Role " + role.getName());
		
		userGroupRepository.save(role);
		return "redirect:/roles";
	}
	
	@RequestMapping("/roles/update")
	public String updateRole(UserGroup userGroup, BindingResult bindingResult) {
		LOGGER.info("Updating role: " + userGroup.getName());
		
		userGroupRepository.save(userGroup);
		return "redirect:/roles";
	}
	
	@RequestMapping("/roles/{id}")
	public String editRole(@PathVariable("id") Long id, Model model) {
		
		model.addAttribute("role", userGroupRepository.findOne(id));
		
		return "security/role/edit";
	}
	
}
