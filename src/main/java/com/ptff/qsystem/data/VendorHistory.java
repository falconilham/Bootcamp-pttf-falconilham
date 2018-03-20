package com.ptff.qsystem.data;

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

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Data;

@Entity
@Table(name="vendor_history")
@Data
@EntityListeners(AuditingEntityListener.class)
public class VendorHistory {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	private Vendor vendor;
	
	@Column(name="message")
	private String message;

	@CreatedDate
	@Convert(converter = LocalDateTimePersistenceConverter.class)
	@Column(name="create_date")
	private LocalDateTime createTime;
	
	@Column(name="create_user")
	@CreatedBy
	private String createUser;
}
