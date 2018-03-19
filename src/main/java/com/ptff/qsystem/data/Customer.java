package com.ptff.qsystem.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Data;

@Data
@Entity
@Table(name="customer")
@EntityListeners(AuditingEntityListener.class)
public class Customer {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	@NotNull
	@NotEmpty
	private String name;
	
	@Column(name="npwp")
	@NotEmpty
	@Length(min=15, max=15, message="NPWP must be 15 characters Long")
	private String npwp;
	
	@Column(name="npwp_address", length=255)
	@NotEmpty
	private String npwpAddress;
	
	@Column(name="corresponsence_address", length=255)
	@NotEmpty
	private String correspondenceAddress;
	
	@Column(name="telephone", length=255)
	@NotEmpty
	private String telephone;
	
	@Column(name="credit_limit", length=255)
	private BigDecimal creditLimit;
	
	@Column(name="operation_notes", length=255)
	private String operationNotes;
	
	@Column(name="finance_notes", length=255)
	private String financeNotes;
	
	@Column(name="status")
	@NotNull
	private CustomerStatus status;
	
	@Column(name="reject_reason")
	private String rejectReason;
	
	@Column(name="approval_date")
	private LocalDate approvalDate;
	
	@Column(name="approval_by")
	private String approvalBy;
	
	@ManyToOne
	@JoinColumn(name="salesperson_id")
	private User salesperson;
	
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
	
}
