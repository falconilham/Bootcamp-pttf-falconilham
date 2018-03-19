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
import com.ptff.qsystem.data.DocumentRepository;
import com.ptff.qsystem.service.StorageService;



@Controller
public class CustomerController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private CustomerDocumentRepository customerDocumentRepository;
	
	@Autowired
	private CustomerContactPersonRepository customerContactPersonRepository;

	@Autowired
	private CustomerHistoryRepository customerHistoryRepository;
	
	@RequestMapping("/customers")
	public String listCustomers(
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		Page<Customer> customers = customerRepository.findAll(pageable);
		
		model.addAttribute("customers", customers);
		
		return "sale/customer/index";
	}
	
	@RequestMapping("/customers/new")
	public String newRole(Model model) {
		Customer customer = new Customer();
		customer.setStatus(CustomerStatus.DRAFT);
		
		model.addAttribute("customer", customer);
		
		return "sale/customer/new";
	}
	
	@RequestMapping("/customers/save")
	@Transactional
	public String saveCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Customer " + customer.getName());
		
		if (bindingResult.hasErrors()) {
			return "sale/customer/new";
		}
		
		customer.setRejectReason("");
		customer = customerRepository.save(customer);		
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage(String.format("Add New Customer: %s", customer.getName()));
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	@RequestMapping("/customers/update")
	public String updateCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating customer: " + customer.getName());

		if (bindingResult.hasErrors()) {
			return "sale/customer/edit";
		}
		
		// Updated Customer always goes to draft status
		customer.setStatus(CustomerStatus.DRAFT);
		customer.setRejectReason("");
		customer = customerRepository.save(customer);
		
		CustomerHistory customerHistory = new CustomerHistory();
		customerHistory.setCustomer(customer);
		customerHistory.setMessage(String.format("Customer %s is edited", customer.getName()));
		customerHistoryRepository.save(customerHistory);
		
		return "redirect:/customers/"+customer.getId();
	}

	@RequestMapping("/customers/{id}")
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
		
		return "sale/customer/show";
	}
	
	@RequestMapping("/customers/{id}/edit")
	public String editRole(@PathVariable("id") Long id, Model model) {
		model.addAttribute("customer", customerRepository.findOne(id));

		
		return "sale/customer/edit";
	}
	
	@RequestMapping("/customers/{id}/contact")
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
	public String editCustomerContact(@PathVariable("id") Long id, @PathVariable("contactId") Long contactId, Model model) {
		
		Customer customer = customerRepository.findOne(id);		
		CustomerContactPerson customerContactPerson = customerContactPersonRepository.findOne(contactId);
		
		model.addAttribute("customer", customer);
		model.addAttribute("customercontact", customerContactPerson);
		
		return "sale/customer/edit_contact";
	}
	
		
	@RequestMapping("/customers/{id}/submit")
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
