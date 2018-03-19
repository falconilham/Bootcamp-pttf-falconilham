package com.ptff.qsystem.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptff.qsystem.data.Customer;
import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.User;
import com.ptff.qsystem.data.UserGroup;
import com.ptff.qsystem.data.UserGroupRepository;
import com.ptff.qsystem.data.UserRepository;

@Controller
public class UserController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;

	
	@ModelAttribute("roles")
    public List<UserGroup> messages() {
        return userGroupRepository.findAll();
    }
	
	
	@RequestMapping("/users")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_SU', 'ROLE_DIRECTOR')")
	public String listUsers(
			@PageableDefault(sort="username", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		Page<User> users = userRepository.findAll(pageable);
		
		model.addAttribute("users", users);
		
		return "security/user/index";
	}
	
	@RequestMapping("/users/new")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_SU', 'ROLE_DIRECTOR')")
	public String newUser(Model model) {
		
		model.addAttribute("user", new User());
		model.addAttribute("roles", userGroupRepository.findAll());
		
		return "security/user/new";
	}
	
	@RequestMapping("/users/save")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_SU', 'ROLE_DIRECTOR')")
	public String saveUser(@Valid User user, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving User " + user.getUsername());
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("roles", userGroupRepository.findAll());
			return "security/user/new";
		}
		
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@RequestMapping("/users/update")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_SU', 'ROLE_DIRECTOR')")
	public String updateUser(@Valid User user, BindingResult bindingResult) {
		LOGGER.info("Updating user: " + user.getUsername());
		
		if (user.getPassword().length() != 60) {
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		}
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@RequestMapping("/users/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMINISTRATOR', 'ROLE_SU', 'ROLE_DIRECTOR')")
	public String newUser(@PathVariable("id") String username, Model model) {
		
		model.addAttribute("user", userRepository.findOne(username));
		model.addAttribute("roles", userGroupRepository.findAll());
		
		return "security/user/edit";
	}
	
}
