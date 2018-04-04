package com.ptff.qsystem.data.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ptff.qsystem.data.Customer;
import com.ptff.qsystem.data.CustomerRepository;
import com.ptff.qsystem.data.CustomerStatus;
import com.ptff.qsystem.data.Item;
import com.ptff.qsystem.data.ItemRepository;
import com.ptff.qsystem.data.ItemStatus;
import com.ptff.qsystem.data.Quotation;
import com.ptff.qsystem.data.QuotationLineItem;
import com.ptff.qsystem.data.QuotationRepository;

@Component
public class QuotationValidator implements Validator, ValidationCondition {
	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
   
	@Override
	public boolean supports(Class<?> clazz) {
		return Quotation.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Quotation quotation = (Quotation)target;
		
		// Reference already Exist
		if (quotation.getId() == null) {
	        if (quotationRepository.countByReference(quotation.getReference()) != 0L) {
	            errors.rejectValue("reference", "quotation.reference.notunique", "Quotation Reference is not Unique");
	        }
		}
		
		// Expiry Date after Quote Date
		if (quotation.getExpiryDate() != null && quotation.getQuoteDate() != null) {
			if (quotation.getExpiryDate().isBefore(quotation.getQuoteDate()))
				errors.rejectValue("expiryDate", "quotation.expiryDate.afterquote",  "Expiry Date must be after Quote Date");
		}
		
	}

	@Override
	public void validate(Object object, ValidationMessage message) {
		Quotation quotation = (Quotation)object;
		
		// All Items are active
		for (QuotationLineItem lineItem: quotation.getQuotationLineItems()) {
		  	Item item = itemRepository.findOne(lineItem.getItem().getId());
		  	if (item.getStatus() != ItemStatus.ACTIVE) {
		  		message.addError("Item: " + item.getName() + " has not been activated");
		  	}
		}
      
		// Customers are active
		Customer customer = customerRepository.findOne(quotation.getCustomer().getId());
  		if (customer.getStatus() != CustomerStatus.ACTIVE) {
  			message.addError("Customer: " +customer.getName() + " has not been activated");
  		}
	}

}
