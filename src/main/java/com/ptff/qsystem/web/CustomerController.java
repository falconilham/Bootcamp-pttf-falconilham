package com.ptff.qsystem.web;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ptff.qsystem.data.Document;
import com.ptff.qsystem.data.DocumentRepository;
import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.Customer;
import com.ptff.qsystem.data.CustomerContactPerson;
import com.ptff.qsystem.data.CustomerContactPersonRepository;
import com.ptff.qsystem.data.CustomerContactType;
import com.ptff.qsystem.data.CustomerDocument;
import com.ptff.qsystem.data.CustomerDocumentRepository;
import com.ptff.qsystem.data.CustomerRepository;
import com.ptff.qsystem.data.CustomerStatus;
import com.ptff.qsystem.service.StorageService;



@Controller
public class CustomerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20 };
	
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

	@RequestMapping("/customers")
	public String listCustomers(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<Customer> customers = customerRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(customers.getTotalPages(), customers.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("customers", customers);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
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
	public String saveCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Customer " + customer.getName());
		
		if (bindingResult.hasErrors()) {
			return "sale/customer/new";
		}
		
		customer = customerRepository.save(customer);
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
		customer = customerRepository.save(customer);
		
		return "redirect:/customers/"+customer.getId();
	}

	@RequestMapping("/customers/{id}")
	public String showCustomer(@PathVariable("id") Long id, Model model) {
		Set<CustomerContactPerson> contactPersons = customerContactPersonRepository.findAllByCustomerId(id);
		
		model.addAttribute("customer", customerRepository.findOne(id));
		model.addAttribute("operationPics", contactPersons.stream()
												.filter(contactPerson -> (contactPerson.getType() == CustomerContactType.OPERATION))
												.collect(Collectors.toSet()));
		model.addAttribute("financePics", contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == CustomerContactType.FINANCE))
				.collect(Collectors.toSet()));
		model.addAttribute("customerdocuments", customerDocumentRepository.findByCustomer(customerRepository.findOne(id)));
		
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
	public String saveCustomerContact(@PathVariable("id") Long id, @Valid CustomerContactPerson customerContactPerson, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Customer Contact Person " + customerContactPerson.getFullName());
		
		Customer customer = customerRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("customer", customer);
			model.addAttribute("customercontact", customerContactPerson);
			
			return "sale/customer/new_contact";
		}
		
		customer.setStatus(CustomerStatus.DRAFT);
		customer = customerRepository.save(customer);
		customerContactPerson = customerContactPersonRepository.save(customerContactPerson);
		
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
	
	
	/////////////////////////////
	
	@RequestMapping("/customers/{id}/document")
	public String newCustomerDocument(@PathVariable("id") Long id, Model model) {
		Customer customer = customerRepository.findOne(id);
		Document document = new Document();
		
		CustomerDocument customerDocument = new CustomerDocument();
		customerDocument.setCustomer(customer);
		customerDocument.setDocument(document);
		
		model.addAttribute("customer", customer);
		model.addAttribute("customerdocument", customerDocument);
		
		return "sale/customer/new_document";
	}
	
	@RequestMapping("/customers/{id}/document/save")
	@Transactional
	public String saveCustomerDocument(@PathVariable("id") Long id, @Valid CustomerDocument customerDocument, BindingResult bindingResult, @RequestParam("file") MultipartFile file, Model model) throws IllegalStateException, IOException {
		LOGGER.info("Saving Customer Document " + customerDocument.getDocument().getName());
		
		Customer customer = customerRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("customer", customer);
			model.addAttribute("customerdocument", customerDocument);
			
			return "sale/customer/new_document";
		}
		
		
		// Save the File
		Document document = customerDocument.getDocument();
		
		File convFile = new File("C:/Projects/TerraForwarding/documents/"+customer.getId()+"-"+document.getName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')));
	    file.transferTo(convFile);
	    document.setFilePath(convFile.getPath());
		
		customer.setStatus(CustomerStatus.DRAFT);
		customer = customerRepository.save(customer);
		
		document = documentRepository.save(document);
		customerDocument.setDocument(document);
		
		customerDocument = customerDocumentRepository.save(customerDocument);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	
	@RequestMapping("/customers/{id}/submit")
	public String submitCustomerForApproval(@PathVariable("id") Long id, Model model) {
		Customer customer = customerRepository.findOne(id);
		customer.setStatus(CustomerStatus.AWAITING_APPROVAL);
		customer = customerRepository.save(customer);
		
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
		
		customer = customerRepository.save(customer);
		
		return "redirect:/customers/"+customer.getId();
	}
	
	@RequestMapping("/customers/{id}/reject")
	public String rejectCustomer(@PathVariable("id") Long id, Model model) {
		Customer customer = customerRepository.findOne(id);
		customer.setStatus(CustomerStatus.DRAFT);
		customer = customerRepository.save(customer);
		
		return "redirect:/customers/"+customer.getId();
	}
}
