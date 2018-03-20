package com.ptff.qsystem.web;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ptff.qsystem.data.CustomerHistory;
import com.ptff.qsystem.data.Document;
import com.ptff.qsystem.data.DocumentRepository;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorContactPerson;
import com.ptff.qsystem.data.VendorContactPersonRepository;
import com.ptff.qsystem.data.VendorContactPersonStatus;
import com.ptff.qsystem.data.VendorContactType;
import com.ptff.qsystem.data.VendorDocument;
import com.ptff.qsystem.data.VendorDocumentRepository;
import com.ptff.qsystem.data.VendorHistory;
import com.ptff.qsystem.data.VendorHistoryRepository;
import com.ptff.qsystem.data.VendorRepository;
import com.ptff.qsystem.data.VendorStatus;
import com.ptff.qsystem.service.StorageService;



@Controller
public class VendorController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VendorController.class);
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private VendorDocumentRepository vendorDocumentRepository;
	
	@Autowired
	private VendorContactPersonRepository vendorContactPersonRepository;

	@Autowired
	private VendorHistoryRepository vendorHistoryRepository;

	
	@RequestMapping("/vendors")
	public String listVendors(
			@PageableDefault(sort="name", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable,
			Model model) {
		
		
		Page<Vendor> vendors = vendorRepository.findAll(pageable);
		
		model.addAttribute("vendors", vendors);
		
		return "purchasing/vendor/index";
	}
	
	@RequestMapping("/vendors/new")
	public String newVendor(Model model) {
		Vendor vendor = new Vendor();
		vendor.setStatus(VendorStatus.DRAFT);
		
		model.addAttribute("vendor", vendor);
		
		return "purchasing/vendor/new";
	}
	
	@RequestMapping("/vendors/save")
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String saveVendor(@Valid Vendor vendor, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Vendor " + vendor.getName());
		
		if (bindingResult.hasErrors()) {
			return "purchasing/vendor/new";
		}
		
		vendor.setRejectReason("");
		vendor = vendorRepository.save(vendor);
		
		VendorHistory vendorHistory = new VendorHistory();
		vendorHistory.setVendor(vendor);
		vendorHistory.setMessage(String.format("Add New Vendor: %s", vendor.getName()));
		vendorHistoryRepository.save(vendorHistory);

		
		return "redirect:/vendors/"+vendor.getId();
	}


	
	@RequestMapping("/vendors/update")
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String updateVendor(@Valid Vendor vendor, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating vendor: " + vendor.getName());

		if (bindingResult.hasErrors()) {
			return "purchasing/vendor/edit";
		}
		
		// Updated Vendor always goes to draft status
		vendor.setStatus(VendorStatus.DRAFT);
		vendor.setRejectReason("");
		vendor = vendorRepository.save(vendor);
		
		VendorHistory vendorHistory = new VendorHistory();
		vendorHistory.setVendor(vendor);
		vendorHistory.setMessage(String.format("Vendor %s is edited", vendor.getName()));
		vendorHistoryRepository.save(vendorHistory);
		
		return "redirect:/vendors/"+vendor.getId();
	}

	@RequestMapping("/vendors/{id}")
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_DIRECTOR', 'ROLE_FINANCE', 'ROLE_SU')")
	public String showVendor(@PathVariable("id") Long id, Model model) {
		Set<VendorContactPerson> contactPersons = vendorContactPersonRepository.findAllByVendorId(id);
		
		Vendor vendor = vendorRepository.findOne(id);
		
		model.addAttribute("vendor", vendor);
		model.addAttribute("operationPics", contactPersons.stream()
												.filter(contactPerson -> (contactPerson.getType() == VendorContactType.OPERATION))
												.collect(Collectors.toSet()));
		model.addAttribute("financePics", contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == VendorContactType.FINANCE))
				.collect(Collectors.toSet()));
		model.addAttribute("vendordocuments", vendorDocumentRepository.findByVendor(vendorRepository.findOne(id)));
		model.addAttribute("vendorhistories", vendorHistoryRepository.findByVendorOrderByCreateTimeDesc(vendor));
		

		return "purchasing/vendor/show";
	}
	
	@RequestMapping("/vendors/{id}/edit")
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String editRole(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vendor", vendorRepository.findOne(id));

		
		return "purchasing/vendor/edit";
	}
	
	@RequestMapping("/vendors/{id}/contact")
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String newVendorContact(@PathVariable("id") Long id, Model model) {
		
		
		Vendor vendor = vendorRepository.findOne(id);
		
		VendorContactPerson vendorContactPerson = new VendorContactPerson();
		vendorContactPerson.setVendor(vendor);
		
		model.addAttribute("vendor", vendor);
		model.addAttribute("vendorcontact", vendorContactPerson);
		
		return "purchasing/vendor/new_contact";
	}
	
	@RequestMapping(value="/vendors/{id}/contact", method=RequestMethod.POST)
	@Transactional
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String saveVendorContact(@PathVariable("id") Long id, @Valid  @ModelAttribute("vendorcontact") VendorContactPerson vendorContactPerson, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Vendor Contact Person " + vendorContactPerson.getFullName());
		
		Vendor vendor = vendorRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("vendor", vendor);
			model.addAttribute("vendorcontact", vendorContactPerson);
			
			return "purchasing/vendor/new_contact";
		}
		
		String message = "";
		if (vendorContactPerson.getId() == null)
			message = String.format("Add %s as %s contact person", vendorContactPerson.getFullName(), vendorContactPerson.getType());
		else
			message = String.format("%s Contact person %s is edited", vendorContactPerson.getType(), vendorContactPerson.getFullName());

		
		vendor.setStatus(VendorStatus.DRAFT);
		vendor.setRejectReason("");
		vendor = vendorRepository.save(vendor);
		vendorContactPerson = vendorContactPersonRepository.save(vendorContactPerson);
		
		VendorHistory vendorHistory = new VendorHistory();
		vendorHistory.setVendor(vendor);
		vendorHistory.setMessage(message);
		vendorHistoryRepository.save(vendorHistory);
		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	@RequestMapping("/vendors/{id}/contact/{contactId}")
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String editVendorContact(@PathVariable("id") Long id, @PathVariable("contactId") Long contactId, Model model) {
		
		Vendor vendor = vendorRepository.findOne(id);		
		VendorContactPerson vendorContactPerson = vendorContactPersonRepository.findOne(contactId);
		
		model.addAttribute("vendor", vendor);
		model.addAttribute("vendorcontact", vendorContactPerson);
		
		return "purchasing/vendor/edit_contact";
	}
	

	
	
	@RequestMapping("/vendors/{id}/submit")
	@PreAuthorize("hasAnyRole('ROLE_PURCHASING', 'ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String submitVendorForApproval(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Vendor vendor = vendorRepository.findOne(id);
		List<String> errors = validateVendorForSubmission(vendor);
		
		if (!errors.isEmpty()) {
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:/vendors/"+vendor.getId();
		}
		
		vendor.setStatus(VendorStatus.AWAITING_APPROVAL);
		vendor = vendorRepository.save(vendor);
		
		
		VendorHistory vendorHistory = new VendorHistory();
		vendorHistory.setVendor(vendor);
		vendorHistory.setMessage(String.format("%s Submitted for Approval", vendor.getName()));
		vendorHistoryRepository.save(vendorHistory);

		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	@RequestMapping("/vendors/{id}/deactivate")
	public String deactivateVendor(@PathVariable("id") Long id, Model model) {
		Vendor vendor = vendorRepository.findOne(id);
		vendor.setStatus(VendorStatus.INACTIVE);
		vendor = vendorRepository.save(vendor);
		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	@RequestMapping("/vendors/{id}/approve")
	@PreAuthorize("hasAnyRole('ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String approveVendor(@PathVariable("id") Long id, Model model) {
		Vendor vendor = vendorRepository.findOne(id);
		vendor.setStatus(VendorStatus.ACTIVE);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		vendor.setApprovalBy(auth.getName());
		vendor.setApprovalDate(LocalDate.now());
		vendor.setRejectReason("");
		vendor = vendorRepository.save(vendor);
		
		VendorHistory vendorHistory = new VendorHistory();
		vendorHistory.setVendor(vendor);
		vendorHistory.setMessage(String.format("%s is Approved and Ready to Transact", vendor.getName()));
		vendorHistoryRepository.save(vendorHistory);

		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	@RequestMapping(value="/vendors/{id}/reject", method=RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_PURCHASINGMANAGER', 'ROLE_SU')")
	public String rejectVendor(@PathVariable("id") Long id, @ModelAttribute("rejectReason")String rejectReason, Model model) {
		Vendor vendor = vendorRepository.findOne(id);
		vendor.setStatus(VendorStatus.DRAFT);
		vendor.setRejectReason(rejectReason);
		vendor = vendorRepository.save(vendor);
		
		VendorHistory vendorHistory = new VendorHistory();
		vendorHistory.setVendor(vendor);
		vendorHistory.setMessage(String.format("%s is rejected due to: %s", vendor.getName(), vendor.getRejectReason()));
		vendorHistoryRepository.save(vendorHistory);
		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	private List<String> validateVendorForSubmission(Vendor vendor) {
		List<String> errors = new ArrayList<String>();
		
		
		// 1 Contact Person for each
		Set<VendorContactPerson> contactPersons = vendorContactPersonRepository.findAllByVendorId(vendor.getId());
		if (contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == VendorContactType.OPERATION 
											&& contactPerson.getStatus() == VendorContactPersonStatus.ACTIVE))
				.count()  == 0) {
			errors.add("Vendor need to have at least 1 Active Operation contact person");
		}
		if (contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == VendorContactType.FINANCE 
											&& contactPerson.getStatus() == VendorContactPersonStatus.ACTIVE))
				.count()  == 0) {
			errors.add("Vendor need to have at least 1 Active Finance contact person");
		}
		
		return errors;
	}

}
