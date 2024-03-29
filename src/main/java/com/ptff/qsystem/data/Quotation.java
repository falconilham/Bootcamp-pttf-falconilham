package com.ptff.qsystem.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.ptff.qsystem.data.converter.LocalDatePersistenceConverter;
import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Data;

@Entity
@Table(name="quotation")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Quotation {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="reference")
	@NotNull
	@NotEmpty
	private String reference;
	
	@ManyToOne
	@JoinColumn(name="customer_id", referencedColumnName="id")
	private Customer customer;
	
	@Column(name="status")
	private QuotationStatus status;
	
	@Column(name="quote_date")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Convert(converter = LocalDatePersistenceConverter.class)
	@NotNull
	private LocalDate quoteDate;
	
	@Column(name="expiry_date")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Convert(converter = LocalDatePersistenceConverter.class)
	@NotNull
	private LocalDate expiryDate;
	
	@OneToMany(mappedBy="quotation", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@OrderBy("id")
	private List<QuotationLineItem> quotationLineItems = new ArrayList<QuotationLineItem>();
	
	@ManyToOne
	@JoinColumn(name="salesperson_id", referencedColumnName="username")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="linked_quotation", referencedColumnName="id")
	private Quotation linkedQuotation;
	
	@Column(name="create_date")
	@CreatedDate
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime createTime;
	
	@Column(name="create_user")
	@CreatedBy
	private String createUser;
	
	@Column(name="lastupdate_date")
	@LastModifiedDate
    @Convert(converter = LocalDateTimePersistenceConverter.class)
	private LocalDateTime lastUpdateTime;
	
	@Column(name="lastupdate_user")
	@LastModifiedBy
	private String lastUpdateUser;

	public QuotationLineItem getLineItem(Long lineItemId) {
    	for (QuotationLineItem lineItem : quotationLineItems) {
    		if (lineItemId.equals(lineItem.getId()))
    			return lineItem;
    	}
    	
    	return null;
    }
	
	public void addLineItem(QuotationLineItem lineItem) {
    	quotationLineItems.add(lineItem);
    	lineItem.setQuotation(this);
    }
 
    public void removeLineItem(QuotationLineItem lineItem) {
    	quotationLineItems.remove(lineItem);
    	lineItem.setQuotation(null);
    }
}
