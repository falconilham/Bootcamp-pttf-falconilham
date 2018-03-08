package com.ptff.qsystem.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@Table(name="customer_documents")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CustomerDocument {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="document_id")
	private Document document;
	
	@ManyToOne
	@JoinColumn(name="document_type_id")
	private DocumentType documentType;

	@Column(name="document_number")
	private String number;
	
	@Column(name="start_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate startDate = LocalDate.now();
	
	@Column(name="expiry_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull
	private LocalDate expiryDate;

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
	
	public String getStatus() {
		if (LocalDate.now().isAfter(expiryDate))
			return "Expired";
		else if (LocalDate.now().isAfter(expiryDate.minusDays(30))) {
			return "Expiring";
		} else {
			return "Active";
		}
	}
}
