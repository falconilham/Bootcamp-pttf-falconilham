package com.ptff.qsystem.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptff.qsystem.data.Customer;
import com.ptff.qsystem.data.CustomerContactPerson;
import com.ptff.qsystem.data.CustomerContactPersonRepository;
import com.ptff.qsystem.data.CustomerContactPersonStatus;
import com.ptff.qsystem.data.CustomerContactType;
import com.ptff.qsystem.data.CustomerDocumentRepository;
import com.ptff.qsystem.data.CustomerHistory;
import com.ptff.qsystem.data.CustomerHistoryRepository;
import com.ptff.qsystem.data.CustomerRepository;
import com.ptff.qsystem.data.CustomerStatus;
import com.ptff.qsystem.data.Quotation;
import com.ptff.qsystem.data.QuotationRepository;
import com.ptff.qsystem.data.User;
import com.ptff.qsystem.data.UserRepository;
import com.ptff.qsystem.web.form.CustomerSearchForm;
import com.ptff.qsystem.web.form.CustomerSearchForm.CustomerSearchType;



@Controller
public class CustomerController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private QuotationRepository quotationRepository;
	
	@Autowired
	private CustomerDocumentRepository customerDocumentRepository;
	
	@Autowired
	private CustomerContactPersonRepository customerContactPersonRepository;

	@Autowired
	private CustomerHistoryRepository customerHistoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute("salespersons")
	List<User> allSalesPersons() {
		return userRepository.findByUserGroupsNameIn(new String[] {"ROLE_SALESPERSON", "ROLE_SALESDIRECTOR"});
	}
	
	@RequestMapping("/customers")
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_DIRECTOR', 'ROLE_FINANCE', 'ROLE_SU')")
	public String listCustomers(
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		Page<Customer> customers = customerRepository.findAll(pageable);
		
		model.addAttribute("customers", customers);
		model.addAttribute("searchForm", new CustomerSearchForm());
		
		return "sale/customer/index";
	}
	
	@RequestMapping(value="/customers", method=RequestMethod.GET, params="search")
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_DIRECTOR', 'ROLE_FINANCE', 'ROLE_SU')")
	public String filterCustomers(
			@ModelAttribute("searchForm") CustomerSearchForm searchForm,
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		CustomerSearchForm processedSearchForm = searchForm.getSearchFilter();
		LOGGER.info("Processing {}", processedSearchForm);
		if (processedSearchForm.getType() == CustomerSearchType.BY_ALL) {
			Page<Customer> customers = customerRepository.findBySearchCriteria(processedSearchForm.getName(), processedSearchForm.getMinDate(), 
				processedSearchForm.getMaxDate(), processedSearchForm.getSalesperson(), pageable);
			model.addAttribute("customers", customers);
		} else {
			Page<Customer> customers = customerRepository.findBySearchCriteria(processedSearchForm.getName(), processedSearchForm.getMinDate(), 
					processedSearchForm.getMaxDate(), processedSearchForm.getSalesperson(), CustomerStatus.valueOf(processedSearchForm.getStatus()), pageable);
			model.addAttribute("customers", customers);
		}
		
		return "sale/customer/index";
	}
	
	@RequestMapping("/customers/new")
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String newCustomer(Model model) {
		Customer customer = new Customer();
		

		customer.setStatus(CustomerStatus.DRAFT);
		
		model.addAttribute("customer", customer);
		
		return "sale/customer/new";
	}
	
	@RequestMapping("/customers/save")
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String saveCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Customer " + customer.getName());
		
		if (bindingResult.hasErrors()) {
			return "sale/customer/new";
		}
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user != null)
			customer.addSalesperson(userRepository.findOne(user.getUsername()));
		customer.setRejectReason("");
		customer = customerRepository.save(customer);		
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage(String.format("Add New Customer: %s", customer.getName()));
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	@RequestMapping(value="/customers/{customerId}", method=RequestMethod.POST)
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String updateCustomer(@PathVariable("customerId") Long customerId, @Valid Customer customerForm, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating customer: " + customerForm.getName());

		if (bindingResult.hasErrors()) {
			return "sale/customer/edit";
		}
		Customer customer = customerRepository.findOne(customerId);
		
		// Updated Customer always goes to draft status
		customerForm.setStatus(CustomerStatus.DRAFT);
		customerForm.setRejectReason("");
		customerForm.setSalespersons(customer.getSalespersons());
		customerForm = customerRepository.save(customer);
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage(String.format("Customer %s is edited", customer.getName()));
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/{customerId}";
	}

	@RequestMapping("/customers/{id}")
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_DIRECTOR', 'ROLE_FINANCE', 'ROLE_SU')")
	public String showCustomer(@PathVariable("id") Long id, Model model) {
		Set<CustomerContactPerson> contactPersons = customerContactPersonRepository.findAllByCustomerId(id);
		
		Customer customer = customerRepository.findOne(id);
		
		model.addAttribute("customer", customer);
		model.addAttribute("operationPics", contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == CustomerContactType.OPERATION))
				.collect(Collectors.toSet()));
		model.addAttribute("financePics", contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == CustomerContactType.FINANCE))
				.collect(Collectors.toSet()));
		model.addAttribute("customerdocuments", customerDocumentRepository.findByCustomer(customer));
		model.addAttribute("customerhistories", customerHistoryRepository.findByCustomerOrderByCreateTimeDesc(customer));
		
		List<Quotation> quotations = quotationRepository.findAllByCustomer(customer);
		model.addAttribute("quotations", quotations);
		
		return "sale/customer/show";
	}
	
	@RequestMapping("/customers/{id}/edit")
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String editRole(@PathVariable("id") Long id, Model model) {
		model.addAttribute("customer", customerRepository.findOne(id));

		
		return "sale/customer/edit";
	}
	
	@RequestMapping("/customers/{id}/contact")
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String newCustomerContact(@PathVariable("id") Long id, Model model) {
		
		
		Customer customer = customerRepository.findOne(id);
		
		CustomerContactPerson customerContactPerson = new CustomerContactPerson();
		customerContactPerson.setCustomer(customer);
		
		model.addAttribute("customer", customer);
		model.addAttribute("customercontact", customerContactPerson);
		
		return "sale/customer/new_contact";
	}
	
	@RequestMapping("/customers/{id}/contact/save")
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String saveCustomerContact(@PathVariable("id") Long id, @Valid @ModelAttribute("customercontact") CustomerContactPerson customerContactPerson, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Customer Contact Person " + customerContactPerson.getFullName());
		
		Customer customer = customerRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("customer", customer);
			model.addAttribute("customercontact", customerContactPerson);
			
			return "sale/customer/new_contact";
		}
		
		String message = "";
		if (customerContactPerson.getId() == null)
			message = String.format("Add %s as %s contact person", customerContactPerson.getFullName(), customerContactPerson.getType());
		else
			message = String.format("%s Contact person %s is edited", customerContactPerson.getType(), customerContactPerson.getFullName());
		
		customer.setStatus(CustomerStatus.DRAFT);
		customer.setRejectReason("");
		customer = customerRepository.save(customer);
		customerContactPerson = customerContactPersonRepository.save(customerContactPerson);
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage(message);
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	@RequestMapping("/customers/{id}/contact/{contactId}")
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String editCustomerContact(@PathVariable("id") Long id, @PathVariable("contactId") Long contactId, Model model) {
		
		Customer customer = customerRepository.findOne(id);		
		CustomerContactPerson customerContactPerson = customerContactPersonRepository.findOne(contactId);
		
		model.addAttribute("customer", customer);
		model.addAttribute("customercontact", customerContactPerson);
		
		return "sale/customer/edit_contact";
	}
	
	@RequestMapping(value="/customers/{id}/salespersons", method=RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String assignSalePerson(@PathVariable("id") Long id, @RequestParam("salesperson") String salesperson, Model model) {
		LOGGER.info("Assigning Salesperson {} to customer {}", salesperson, id);
		
		Customer customer = customerRepository.findOne(id);		
		customer.addSalesperson(userRepository.findOne(salesperson));
		customerRepository.save(customer);
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage("Assigned " + salesperson + " to this customer" );
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	@RequestMapping(value="/customers/{id}/salespersons/{username}/remove", method=RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String removeSalePerson(@PathVariable("id") Long id, @PathVariable("username") String username, Model model) {
		LOGGER.info("Removing Salesperson {} from customer {}", username, id);
		
		Customer customer = customerRepository.findOne(id);		
		User user = userRepository.findOne(username);
		customer.removeSalesperson(user);
		customerRepository.save(customer);
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage("Removed " + username + " to this customer" );
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}
	
		
	@RequestMapping("/customers/{id}/submit")
	@PreAuthorize("hasAnyRole('ROLE_SALESPERSON', 'ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String submitCustomerForApproval(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		// Validate Customers
		Customer customer = customerRepository.findOne(id);
		List<String> errors = validateCustomerForSubmission(customer);
		
		if (!errors.isEmpty()) {
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:/customers/"+customer.getId();
		}
		
		customer.setStatus(CustomerStatus.AWAITING_APPROVAL);
		customer = customerRepository.save(customer);
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage(String.format("%s Submitted for Approval", customer.getName()));
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	@RequestMapping("/customers/{id}/deactivate")
	public String deactivateCustomer(@PathVariable("id") Long id, Model model) {
		Customer customer = customerRepository.findOne(id);
		customer.setStatus(CustomerStatus.INACTIVE);
		customer = customerRepository.save(customer);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	@RequestMapping("/customers/{id}/approve")
	@PreAuthorize("hasAnyRole('ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String approveCustomer(@PathVariable("id") Long id, Model model) {
		Customer customer = customerRepository.findOne(id);
		customer.setStatus(CustomerStatus.ACTIVE);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		customer.setApprovalBy(auth.getName());
		customer.setApprovalDate(LocalDate.now());
		customer.setRejectReason("");
		customer = customerRepository.save(customer);
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage(String.format("%s is Approved and Ready to Transact", customer.getName()));
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	@RequestMapping(value="/customers/{id}/reject", method=RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_SALESDIRECTOR', 'ROLE_SU')")
	public String rejectCustomer(@PathVariable("id") Long id, @ModelAttribute("rejectReason")String rejectReason, Model model) {
		LOGGER.info("Rejecting Customer {} due to {}", id, rejectReason);
		
		Customer customer = customerRepository.findOne(id);
		customer.setStatus(CustomerStatus.DRAFT);
		customer.setRejectReason(rejectReason);
		customer = customerRepository.save(customer);
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage(String.format("%s is rejected due to: %s", customer.getName(), customer.getRejectReason()));
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	private List<String> validateCustomerForSubmission(Customer customer) {
		List<String> errors = new ArrayList<String>();
		// Validate Customer Credit Limit
		if (customer.getCreditLimit() == null) {
			errors.add("Customer Credit Limit must be set");
		}
		
		// 1 Contact Person for each
		Set<CustomerContactPerson> contactPersons = customerContactPersonRepository.findAllByCustomerId(customer.getId());
		if (contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == CustomerContactType.OPERATION 
											&& contactPerson.getStatus() == CustomerContactPersonStatus.ACTIVE))
				.count()  == 0) {
			errors.add("Customer need to have at least 1 Active Operation contact person");
		}
		if (contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == CustomerContactType.FINANCE 
											&& contactPerson.getStatus() == CustomerContactPersonStatus.ACTIVE))
				.count()  == 0) {
			errors.add("Customer need to have at least 1 Active Finance contact person");
		}
		
		return errors;
	}
}
