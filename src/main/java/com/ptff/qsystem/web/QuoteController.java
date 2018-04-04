package com.ptff.qsystem.web;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ptff.qsystem.data.Customer;
import com.ptff.qsystem.data.CustomerRepository;
import com.ptff.qsystem.data.CustomerStatus;
import com.ptff.qsystem.data.Item;
import com.ptff.qsystem.data.ItemMinimum;
import com.ptff.qsystem.data.ItemMinimumPricingTier;
import com.ptff.qsystem.data.ItemPermit;
import com.ptff.qsystem.data.ItemPermitRepository;
import com.ptff.qsystem.data.ItemRepository;
import com.ptff.qsystem.data.ItemStatus;
import com.ptff.qsystem.data.Quotation;
import com.ptff.qsystem.data.QuotationLineItem;
import com.ptff.qsystem.data.QuotationLineItemRepository;
import com.ptff.qsystem.data.QuotationPricingTier;
import com.ptff.qsystem.data.QuotationProductType;
import com.ptff.qsystem.data.QuotationRepository;
import com.ptff.qsystem.data.QuotationStatus;
import com.ptff.qsystem.data.Seaport;
import com.ptff.qsystem.data.validator.QuotationValidator;
import com.ptff.qsystem.data.validator.ValidationMessage;
import com.ptff.qsystem.form.ItemSearchForm;

@Controller
public class QuoteController implements DefaultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteController.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private QuotationRepository quotationRepository;
	
	@Autowired
	private QuotationValidator quotationValidator;
	
	@Autowired
	private QuotationLineItemRepository quotationLineItemRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@InitBinder("quotation")
	public void setupBinder(WebDataBinder binder) {
	    binder.addValidators(quotationValidator);
	}
	
	
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
	
	@RequestMapping("/quotations/{id}/edit")
	public String editQuotation(@PathVariable("id") Long id, Model model) {
		model.addAttribute("quotation", quotationRepository.findOne(id));
		
		return "sale/quotation/edit";
	}
	
	@RequestMapping(value="/quotations/{quotationId}", method=RequestMethod.POST)
	public String saveQuotationDetail(
			@PathVariable("quotationId") Long id, 
			@ModelAttribute("quotation") @Valid Quotation quotation,
			BindingResult bindingResult,
			Model model) {
		LOGGER.info("Saving Quotation " + quotation.getReference());
		
		if (bindingResult.hasErrors()) {
			return "sale/quotation/edit";
		}
				
		quotation = quotationRepository.save(quotation);
		return "redirect:/quotations/{quotationId}";
	}
	
	@RequestMapping(value="/quotations/{id}", method=RequestMethod.GET)
	public String newQuotation(@PathVariable("id") Long id, Model model) {
		LOGGER.info("Showing Quotation with Id: {}", id);
		
		Quotation quotation = quotationRepository.findOne(id);
		ValidationMessage validationMessage = new ValidationMessage();
		quotationValidator.validate(quotation, validationMessage);
		
		model.addAttribute("quotation", quotation);
		model.addAttribute("message", validationMessage);
		
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
		
		Page<Item> items = itemRepository.findAll(pageable);
		model.addAttribute("items", items);
		
		LOGGER.info("Found {} pages with {} items matching the criteria {}", items.getTotalPages(), items.getTotalElements(), itemSearchForm);
		
		return "sale/quotation/search_permit";
	}
	
	@RequestMapping("/quotations/{id}/{itemId}/add")
	public String addItemToQuotation(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, Model model) {
		Quotation quotation = quotationRepository.findOne(id);
		Item item = itemRepository.findOne(itemId);
		
		LOGGER.info("Adding {} to quotation {}", item.getName(), quotation.getReference());
		
		QuotationLineItem lineItem = new QuotationLineItem();
		lineItem.setItem(item);
		lineItem.setQuotation(quotation);
		
		// Add the Pricing Tier
		ItemMinimum minimumPrice = item.getActiveMinimumPrice();
		if (minimumPrice != null) {
			for (ItemMinimumPricingTier minimumPriceTier : minimumPrice.getPricingTiers()) {
				QuotationPricingTier quotationPricingTier = new QuotationPricingTier();
				quotationPricingTier.setIsSinglePrice(minimumPriceTier.getIsSinglePrice());
				quotationPricingTier.setLowerLimit(minimumPriceTier.getLowerLimit());
				quotationPricingTier.setUpperLimit(minimumPriceTier.getUpperLimit());
				quotationPricingTier.setQuotationLineItem(lineItem);
				quotationPricingTier.setPrice(BigDecimal.ZERO);
				lineItem.addPriceTier(quotationPricingTier);
			}
		}
		
		quotation.getQuotationLineItems().add(lineItem);
		quotationRepository.save(quotation);
		
		return "redirect:/quotations/{id}";
	}
	
	@RequestMapping("/quotations/{id}/line/{lineItemId}/remove")
	public String removeLineItemFromQuotation(@PathVariable("id") Long id, @PathVariable("lineItemId") Long lineItemId, Model model) {
		LOGGER.info("Removing Line Item {} from Quotation: {}",  lineItemId, id);
		
		Quotation quotation = quotationRepository.findOne(id);
		
		QuotationLineItem lineItem = quotation.getLineItem(lineItemId);
		quotation.removeLineItem(lineItem);
		
		quotationRepository.save(quotation);

		return "redirect:/quotations/{id}";
	}
	
	@RequestMapping(value="/quotations/{quotationId}", method=RequestMethod.POST, params="finalise")
	public String finaliseQuotation(
			@PathVariable("quotationId") Long id, 
			@ModelAttribute("quotation") @Valid Quotation quotation,
			BindingResult bindingResult,
			Model model) {
		
		Quotation toSave = quotationRepository.findOne(id);

		for (int i=0; i<toSave.getQuotationLineItems().size(); i++) {
			QuotationLineItem qliToSave = toSave.getQuotationLineItems().get(i);
			QuotationLineItem qli = quotation.getLineItem(qliToSave.getId());
			
			// set the price tiers
			for (int j=0; j<qliToSave.getPricingTiers().size(); j++) {
				QuotationPricingTier qpt = qli.getPricingTiers().get(j);
				QuotationPricingTier qptToSave = qliToSave.getPriceTier(qpt.getId());
				qptToSave.setPrice(qpt.getPrice());
			}
		}
		
		// Only Progress the status when business object validation pass
		ValidationMessage validationMessage = new ValidationMessage();
		quotationValidator.validate(toSave, validationMessage);
		if (!validationMessage.hasErrors())
			toSave.setStatus(QuotationStatus.ACTIVE);
		
		quotationRepository.save(toSave);
		
		return "redirect:/quotations/{quotationId}";
	}
	
	@RequestMapping(value="/quotations/{quotationId}", method=RequestMethod.POST, params="save")
	public String saveQuotation(
			@PathVariable("quotationId") Long id, 
			@ModelAttribute("quotation") @Valid Quotation quotation,
			BindingResult bindingResult,
			Model model) {
		
		Quotation toSave = quotationRepository.findOne(id);

		for (int i=0; i<toSave.getQuotationLineItems().size(); i++) {
			QuotationLineItem qliToSave = toSave.getQuotationLineItems().get(i);
			QuotationLineItem qli = quotation.getLineItem(qliToSave.getId());
			
			// set the price tiers
			for (int j=0; j<qliToSave.getPricingTiers().size(); j++) {
				QuotationPricingTier qpt = qli.getPricingTiers().get(j);
				QuotationPricingTier qptToSave = qliToSave.getPriceTier(qpt.getId());
				qptToSave.setPrice(qpt.getPrice());
			}
		}
		
		quotationRepository.save(toSave);
		
		return "redirect:/quotations/{quotationId}";
	}
	
	
	@RequestMapping(value="/quotations/{quotationId}/requote")
	public String requoteQuotation(
			@PathVariable("quotationId") Long id,
			Model model) {
		
		// Deactivate the original
		Quotation original = quotationRepository.findOne(id);
		original.setStatus(QuotationStatus.EXPIRED);
		original.setExpiryDate(LocalDate.now());
		quotationRepository.save(original);
		
		Quotation newQuotation = new Quotation();
		newQuotation.setReference(original.getReference() + " (Copy)");
		newQuotation.setQuoteDate(LocalDate.now());
		newQuotation.setExpiryDate(LocalDate.now().plusDays(30));
		newQuotation.setCustomer(original.getCustomer());
		newQuotation.setStatus(QuotationStatus.DRAFT);
		for (QuotationLineItem qli: original.getQuotationLineItems()) {
			QuotationLineItem newQli = new QuotationLineItem();
			newQli.setItem(qli.getItem());
			
			// set the price tiers
			for (QuotationPricingTier qpt : qli.getPricingTiers()) {
				QuotationPricingTier newQpt = new QuotationPricingTier();
				newQpt.setIsSinglePrice(qpt.getIsSinglePrice());
				newQpt.setLowerLimit(qpt.getLowerLimit());
				newQpt.setUpperLimit(qpt.getUpperLimit());
				newQpt.setPrice(qpt.getPrice());
				newQli.addPriceTier(newQpt);
			}
			newQuotation.addLineItem(newQli);
		}
		
		
		newQuotation = quotationRepository.save(newQuotation);
		
		return "redirect:/quotations/"+newQuotation.getId();
	}
	
	@RequestMapping(value="/quotations/{quotationId}/deactivate")
	public String deactivateQuotation(
			@PathVariable("quotationId") Long id,
			Model model) {
		
		// Deactivate the original
		Quotation original = quotationRepository.findOne(id);
		original.setStatus(QuotationStatus.EXPIRED);
		original.setExpiryDate(LocalDate.now());
		quotationRepository.save(original);
		
		return "redirect:/quotations/{quotationId}";
	}
	
}
