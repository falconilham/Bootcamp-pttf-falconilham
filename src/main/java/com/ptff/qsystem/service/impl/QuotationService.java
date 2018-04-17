package com.ptff.qsystem.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ptff.qsystem.data.Quotation;
import com.ptff.qsystem.data.QuotationRepository;
import com.ptff.qsystem.data.User;
import com.ptff.qsystem.data.UserRepository;


@Service
public class QuotationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuotationService.class);

	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private QuotationRepository quotationRepository;
	
	public String generateReference(Quotation quotation) {
		
		LocalDate today = LocalDate.now();
		Long quotationCount = quotationRepository.countByQuoteDateBetween(today.withDayOfMonth(1).withDayOfMonth(1), today.withDayOfYear(31).withMonth(12));
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)(auth.getPrincipal());
		
		String qRef = String.format("Q%s/%s/%04d", today.format(DateTimeFormatter.ofPattern("yy")), user.getCode(), quotationCount);
		
		LOGGER.info("Generated Reference {}", qRef);
		return qRef;
	}
}
