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
import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemPermitRepository;
import com.ptff.qsystem.data.Quotation;
import com.ptff.qsystem.data.QuotationLineItem;
import com.ptff.qsystem.data.QuotationLineItemRepository;
import com.ptff.qsystem.data.QuotationProductType;
import com.ptff.qsystem.data.QuotationRepository;
import com.ptff.qsystem.data.QuotationStatus;
import com.ptff.qsystem.data.Seaport;
import com.ptff.qsystem.form.ItemSearchForm;

@Controller
public class QuoteController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private QuotationRepository quotationRepository;
	
	@Autowired
	private QuotationLineItemRepository quotationLineItemRepository;
	
	@Autowired
	private ItemPermitRepository itemPermitRepository;
	
	
	
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
	
	@RequestMapping(value="/quotations/{id}/permit", method=RequestMethod.GET)
	public String searchQuotation(@PathVariable("id") Long id,  Model model, Pageable pageable) {
		LOGGER.info("Showing Permit Search Form: {}", id);
		
		Quotation quotation = quotationRepository.findOne(id);
		model.addAttribute("quotation", quotation);
		
		ItemSearchForm itemSearchForm = (ItemSearchForm) model.asMap().get("searchForm");
		if (itemSearchForm == null)
			itemSearchForm = new ItemSearchForm();
		
		model.addAttribute("searchForm", new ItemSearchForm());
		
		
		return "sale/quotation/search_permit";
	}
	
	@RequestMapping(value="/quotations/{id}/permit", method=RequestMethod.POST)
	public String searchQuotation(@PathVariable("id") Long id, @ModelAttribute("searchForm")ItemSearchForm itemSearchForm, Model model, Pageable pageable) {
		LOGGER.info("Showing Permit Search Form: {}", id);
		
		Quotation quotation = quotationRepository.findOne(id);
		model.addAttribute("quotation", quotation);
		
		Page<ItemPermit> items = itemPermitRepository.findByNameLike("%"+itemSearchForm.getName()+"%", pageable);
		model.addAttribute("items", items);
		
		LOGGER.info("Found {} pages with {} items matching the criteria {}", items.getTotalPages(), items.getTotalElements(), itemSearchForm);
		
		return "sale/quotation/search_permit";
	}
	
	@RequestMapping("/quotations/{id}/permit/{permitId}/add")
	public String addPermitToQuotation(@PathVariable("id") Long id, @PathVariable("permitId") Long permitId, Model model) {
		LOGGER.info("Showing Permit Search Form: {}", id);
		
		Quotation quotation = quotationRepository.findOne(id);
		ItemPermit itemPermit = itemPermitRepository.findOne(permitId);
		
		QuotationLineItem lineItem = new QuotationLineItem();
		lineItem.setType(QuotationProductType.PERMIT);
		lineItem.setPermit(itemPermit);
		lineItem.setQuotation(quotation);
		
		quotation.getQuotationLineItems().add(lineItem);
		quotationRepository.save(quotation);
		
		return "redirect:/quotations/{id}";
	}
	
	@RequestMapping("/quotations/{id}/line/{lineItemId}/remove")
	public String removeLineItemFromQuotation(@PathVariable("id") Long id, @PathVariable("lineItemId") Long lineItemId, Model model) {
		LOGGER.info("Removing Line Item {} from Quotation: {}",  lineItemId, id);
		
		Quotation quotation = quotationRepository.findOne(id);
		LOGGER.info("Quotation has {} Line Items", quotation.getQuotationLineItems().size());
		
		QuotationLineItem lineItem = quotationLineItemRepository.findOne(lineItemId);
		quotation.getQuotationLineItems().remove(lineItem);
		quotationLineItemRepository.delete(lineItem);

		return "redirect:/quotations/{id}";
	}
}
