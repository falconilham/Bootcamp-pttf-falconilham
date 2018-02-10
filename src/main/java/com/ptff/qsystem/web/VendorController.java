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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ptff.qsystem.data.Document;
import com.ptff.qsystem.data.DocumentRepository;
import com.ptff.qsystem.data.Pager;
import com.ptff.qsystem.data.Vendor;
import com.ptff.qsystem.data.VendorContactPerson;
import com.ptff.qsystem.data.VendorContactPersonRepository;
import com.ptff.qsystem.data.VendorContactType;
import com.ptff.qsystem.data.VendorDocument;
import com.ptff.qsystem.data.VendorDocumentRepository;
import com.ptff.qsystem.data.VendorRepository;
import com.ptff.qsystem.data.VendorStatus;
import com.ptff.qsystem.service.StorageService;



@Controller
public class VendorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VendorController.class);
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 20;
	private static final int[] PAGE_SIZES = { 5, 10, 20 };
	
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

	@RequestMapping("/vendors")
	public String listVendors(
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page,
			Model model) {
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		
		Page<Vendor> vendors = vendorRepository.findAll(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(vendors.getTotalPages(), vendors.getNumber(), BUTTONS_TO_SHOW);
		
		model.addAttribute("vendors", vendors);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		
		return "purchasing/vendor/index";
	}
	
	@RequestMapping("/vendors/new")
	public String newRole(Model model) {
		Vendor vendor = new Vendor();
		vendor.setStatus(VendorStatus.DRAFT);
		
		model.addAttribute("vendor", vendor);
		
		return "purchasing/vendor/new";
	}
	
	@RequestMapping("/vendors/save")
	public String saveVendor(@Valid Vendor vendor, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Vendor " + vendor.getName());
		
		if (bindingResult.hasErrors()) {
			return "purchasing/vendor/new";
		}
		
		vendor = vendorRepository.save(vendor);
		return "redirect:/vendors/"+vendor.getId();
	}

	@RequestMapping("/vendors/{id}/edit")
	public String editRole(@PathVariable("id") Long id, Model model) {
		model.addAttribute("vendor", vendorRepository.findOne(id));

		
		return "purchasing/vendor/edit";
	}
	
	@RequestMapping("/vendors/update")
	public String updateVendor(@Valid Vendor vendor, BindingResult bindingResult, Model model) {
		LOGGER.info("Updating vendor: " + vendor.getName());

		if (bindingResult.hasErrors()) {
			return "purchasing/vendor/edit";
		}
		
		// Updated Vendor always goes to draft status
		vendor.setStatus(VendorStatus.DRAFT);
		vendor = vendorRepository.save(vendor);
		
		return "redirect:/vendors/"+vendor.getId();
	}

	@RequestMapping("/vendors/{id}")
	public String showVendor(@PathVariable("id") Long id, Model model) {
		Set<VendorContactPerson> contactPersons = vendorContactPersonRepository.findAllByVendorId(id);
		
		model.addAttribute("vendor", vendorRepository.findOne(id));
		model.addAttribute("operationPics", contactPersons.stream()
												.filter(contactPerson -> (contactPerson.getType() == VendorContactType.OPERATION))
												.collect(Collectors.toSet()));
		model.addAttribute("financePics", contactPersons.stream()
				.filter(contactPerson -> (contactPerson.getType() == VendorContactType.FINANCE))
				.collect(Collectors.toSet()));
		model.addAttribute("vendordocuments", vendorDocumentRepository.findByVendor(vendorRepository.findOne(id)));
		
		return "purchasing/vendor/show";
	}
	
	@RequestMapping("/vendors/{id}/contact")
	public String newVendorContact(@PathVariable("id") Long id, Model model) {
		
		
		Vendor vendor = vendorRepository.findOne(id);
		
		VendorContactPerson vendorContactPerson = new VendorContactPerson();
		vendorContactPerson.setVendor(vendor);
		
		model.addAttribute("vendor", vendor);
		model.addAttribute("vendorContactPerson", vendorContactPerson);
		
		return "purchasing/vendor/new_contact";
	}
	
	@RequestMapping(value="/vendors/{id}/contact", method=RequestMethod.POST)
	public String saveVendorContact(@PathVariable("id") Long id, @Valid VendorContactPerson vendorContactPerson, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Vendor Contact Person " + vendorContactPerson.getFullName());
		
		Vendor vendor = vendorRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("vendor", vendor);
			
			return "purchasing/vendor/new_contact";
		}
		
		vendor.setStatus(VendorStatus.DRAFT);
		vendor = vendorRepository.save(vendor);
		vendorContactPerson = vendorContactPersonRepository.save(vendorContactPerson);
		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	@RequestMapping("/vendors/{id}/contact/{contactId}")
	public String editVendorContact(@PathVariable("id") Long id, @PathVariable("contactId") Long contactId, Model model) {
		
		Vendor vendor = vendorRepository.findOne(id);		
		VendorContactPerson vendorContactPerson = vendorContactPersonRepository.findOne(contactId);
		
		model.addAttribute("vendor", vendor);
		model.addAttribute("vendorContactPerson", vendorContactPerson);
		
		return "purchasing/vendor/edit_contact";
	}
	
	@RequestMapping(value="/vendors/{id}/contact/{contactId}", method=RequestMethod.POST)
	public String updateVendorContact(@PathVariable("id") Long id, @Valid VendorContactPerson vendorContactPerson, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Vendor Contact Person " + vendorContactPerson.getFullName());
		
		Vendor vendor = vendorRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("vendor", vendor);
			
			return "purchasing/vendor/edit_contact";
		}
		
		vendor.setStatus(VendorStatus.DRAFT);
		vendor = vendorRepository.save(vendor);
		vendorContactPerson = vendorContactPersonRepository.save(vendorContactPerson);
		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	
	/////////////////////////////
	
	@RequestMapping("/vendors/{id}/document")
	public String newVendorDocument(@PathVariable("id") Long id, Model model) {
		Vendor vendor = vendorRepository.findOne(id);
		Document document = new Document();
		
		VendorDocument vendorDocument = new VendorDocument();
		vendorDocument.setVendor(vendor);
		vendorDocument.setDocument(document);
		
		model.addAttribute("vendor", vendor);
		model.addAttribute("vendordocument", vendorDocument);
		
		return "purchasing/vendor/new_document";
	}
	
	@RequestMapping("/vendors/{id}/document/save")
	@Transactional
	public String saveVendorDocument(@PathVariable("id") Long id, @Valid VendorDocument vendorDocument, BindingResult bindingResult, @RequestParam("file") MultipartFile file, Model model) throws IllegalStateException, IOException {
		LOGGER.info("Saving Vendor Document " + vendorDocument.getDocument().getName());
		
		Vendor vendor = vendorRepository.findOne(id);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("vendor", vendor);
			model.addAttribute("vendordocument", vendorDocument);
			
			return "purchasing/vendor/new_document";
		}
		
		
		// Save the File
		Document document = vendorDocument.getDocument();
		
		File convFile = new File("C:/Projects/TerraForwarding/documents/"+vendor.getId()+"-"+document.getName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')));
	    file.transferTo(convFile);
	    document.setFilePath(convFile.getPath());
		
		vendor.setStatus(VendorStatus.DRAFT);
		vendor = vendorRepository.save(vendor);
		
		document = documentRepository.save(document);
		vendorDocument.setDocument(document);
		
		vendorDocument = vendorDocumentRepository.save(vendorDocument);
		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	
	@RequestMapping("/vendors/{id}/submit")
	public String submitVendorForApproval(@PathVariable("id") Long id, Model model) {
		Vendor vendor = vendorRepository.findOne(id);
		vendor.setStatus(VendorStatus.AWAITING_APPROVAL);
		vendor = vendorRepository.save(vendor);
		
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
	public String approveVendor(@PathVariable("id") Long id, Model model) {
		Vendor vendor = vendorRepository.findOne(id);
		vendor.setStatus(VendorStatus.ACTIVE);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		vendor.setApprovalBy(auth.getName());
		vendor.setApprovalDate(LocalDate.now());
		
		vendor = vendorRepository.save(vendor);
		
		return "redirect:/vendors/"+vendor.getId();
	}
	
	@RequestMapping("/vendors/{id}/reject")
	public String rejectVendor(@PathVariable("id") Long id, Model model) {
		Vendor vendor = vendorRepository.findOne(id);
		vendor.setStatus(VendorStatus.DRAFT);
		vendor = vendorRepository.save(vendor);
		
		return "redirect:/vendors/"+vendor.getId();
	}
}
