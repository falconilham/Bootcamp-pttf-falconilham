package com.ptff.qsystem.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ptff.qsystem.data.Quotation;
import com.ptff.qsystem.data.QuotationRepository;


@Service
public class QuotationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuotationService.class);

	
	@Autowired
	private QuotationRepository quotationRepository;
	
	private void generateReference(Quotation quotation) {
		
		LocalDate today = LocalDate.now();
		Long quotationCount = quotationRepository.countByQuoteDateBetween(today.withDayOfMonth(1).withDayOfMonth(1), today.withDayOfYear(31).withMonth(12));
		
		String qRef = String.format("Q%s/%s/%d", today.format(DateTimeFormatter.ofPattern("yy")), quotation.getUser().getCode(), quotationCount);
		
		LOGGER.info("Generated Reference {}", qRef);
	}
}
