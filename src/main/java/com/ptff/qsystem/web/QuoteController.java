package com.ptff.qsystem.web;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ptff.qsystem.data.Customer;
import com.ptff.qsystem.data.CustomerRepository;
import com.ptff.qsystem.data.Quotation;
import com.ptff.qsystem.data.QuotationRepository;
import com.ptff.qsystem.data.QuotationStatus;
import com.ptff.qsystem.data.Seaport;

@Controller
public class QuoteController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private QuotationRepository quotationRepository;
	
	@ModelAttribute("customers")
    public List<Customer> messages() {
        return customerRepository.findAll();
    }
	
	@RequestMapping("/quotations")
	public String list(Model model, 
			@PageableDefault(sort="reference", direction=Sort.Direction.ASC, page=INITIAL_PAGE, size=INITIAL_PAGE_SIZE) Pageable pageable) {
		
		Page<Quotation> quotations = quotationRepository.findAll(pageable);
		
		model.addAttribute("quotations", quotations);
		
		return "sale/quotation/index";
	}
	
	@RequestMapping("/quotations/new")
	public String newQuotation(Model model) {
		model.addAttribute("quotation", new Quotation());
		
		return "sale/quotation/new";
	}
	
	@RequestMapping(value="/quotations", method=RequestMethod.POST)
	public String saveNewQuotation(@Valid Quotation quotation, BindingResult bindingResult, Model model) {
		LOGGER.info("Saving Quotation " + quotation.getReference());
		
		if (bindingResult.hasErrors()) {
			return "sale/quotation/new";
		}
		
		// Set Qutation status to Draft
		quotation.setStatus(QuotationStatus.DRAFT);
		
		quotation = quotationRepository.save(quotation);
		return "redirect:/quotations/"+quotation.getId();
	}
	
	@RequestMapping("/quotations/{id}")
	public String newQuotation(@PathVariable("id") Long id, Model model) {
		LOGGER.info("Showing Quotation with Id: {}", id);
		
		Quotation quotation = quotationRepository.findOne(id);
		model.addAttribute("quotation", quotation);
		
		return "sale/quotation/show";
	}
	
	@RequestMapping(value="/quotations/{id}/permit", method=RequestMethod.POST)
	public String searchQuotation(@PathVariable("id") Long id, Model model) {
		LOGGER.info("Showing Permit Search Form: {}", id);
		
		Quotation quotation = quotationRepository.findOne(id);
		model.addAttribute("quotation", quotation);
		
		return "sale/quotation/search_permit";
	}
	
	@RequestMapping("/quotations/{id}/permit")
	public String addPermitToQuotation(@PathVariable("id") Long id, Model model) {
		LOGGER.info("Showing Permit Search Form: {}", id);
		
		Quotation quotation = quotationRepository.findOne(id);
		model.addAttribute("quotation", quotation);
		
		return "sale/quotation/search_permit";
	}
}
