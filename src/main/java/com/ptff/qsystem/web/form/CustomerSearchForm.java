package com.ptff.qsystem.web.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.ptff.qsystem.data.CustomerStatus;

import lombok.Data;

@Data
public class CustomerSearchForm {
	public enum CustomerSearchType
    {
        BY_STATUS, BY_ALL
    }
	
	private String name;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate minDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate maxDate;
	
	private String salesperson;
	
	private String status;
	
	private CustomerSearchType type;
	
	public CustomerSearchForm getSearchFilter() {
		CustomerSearchForm searchForm = new CustomerSearchForm();
		searchForm.name = this.name;
		searchForm.salesperson = this.salesperson;
		searchForm.minDate = this.minDate;
		searchForm.maxDate = this.maxDate;
		searchForm.status = this.status;
		
		if (name.length() == 0)
			searchForm.name = "";
		
		if (salesperson.length() == 0)
			searchForm.salesperson = "";
		
		if (minDate == null)
			searchForm.minDate = LocalDate.of(1900, 1, 1);
		

		if (maxDate == null)
			searchForm.maxDate = LocalDate.of(9999, 12, 31);;
		
		if (status.equals("ALL")) {
			searchForm.type = CustomerSearchType.BY_ALL;
		} else {
			searchForm.type = CustomerSearchType.BY_STATUS;
		}
			
		
		return searchForm;
	}
	
}
