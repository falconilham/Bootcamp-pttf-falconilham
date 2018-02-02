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

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Data;

@Entity
@Table(name="user_group")
@Data
@EntityListeners(AuditingEntityListener.class)
public class UserGroup implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name", length=100)
	private String name;
	
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

	@Override
	public String getAuthority() {
		return name;
	}
}
