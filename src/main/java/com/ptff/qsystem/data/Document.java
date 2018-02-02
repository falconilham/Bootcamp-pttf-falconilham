package com.ptff.qsystem.data;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Data;

@Entity
@Table(name="documents")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Document {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	@NotNull
	@NotEmpty
	private String name;
	
	@Column(name="description", length=255)
	private String description;
	
	@Column(name="file_path")
	@NotNull
	@NotEmpty
	private String filePath;

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
