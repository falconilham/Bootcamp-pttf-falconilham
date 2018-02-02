package com.ptff.qsystem.web;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.User;
import com.ptff.qsystem.data.UserGroupRepository;
import com.ptff.qsystem.data.UserRepository;

@Controller
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;
	private static final int[] PAGE_SIZES = { 5, 10, 20 };
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;

	@RequestMapping("/users")
	public String listUsers(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<User> users = userRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(users.getTotalPages(), users.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("users", users);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "security/user/index";
	}
	
	@RequestMapping("/users/new")
	public String newUser(Model model) {
		
		model.addAttribute("user", new User());
		model.addAttribute("roles", userGroupRepository.findAll());
		
		return "security/user/new";
	}
	
	@RequestMapping("/users/save")
	public String saveUser(@Valid User user, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving User " + user.getUsername());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", userGroupRepository.findAll());
			return "security/user/new";
		}
		
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@RequestMapping("/users/update")
	public String updateUser(@Valid User user, BindingResult bindingResult) {
		LOGGER.info("Updating user: " + user.getUsername());
		
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@RequestMapping("/users/{id}")
	public String newUser(@PathVariable("id") String username, Model model) {
		
		model.addAttribute("user", userRepository.findOne(username));
		model.addAttribute("roles", userGroupRepository.findAll());
		
		return "security/user/edit";
	}
	
}
