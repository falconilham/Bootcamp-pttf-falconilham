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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Data;

@Entity
@Table(name="vendor_contact_person")
@Data
@EntityListeners(AuditingEntityListener.class)
public class VendorContactPerson {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	@NotNull
	private Vendor vendor;
	
	@Column(name="type")
	@NotNull
	private VendorContactType type;
	
	@Column(name="full_name", length=255)
	@NotEmpty
	private String fullName;
	
	@Column(name="position", length=255)
	@NotEmpty
	private String position;
	
	@Column(name="telephone")
	private String telephone;
	
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="email")
	@Email
	private String email;
	
	@Column(name="status")
	@NotNull
	private VendorContactPersonStatus status;
	
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
