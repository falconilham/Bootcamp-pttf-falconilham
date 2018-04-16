package com.ptff.qsystem.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user_group")
@Getter
@Setter
public class UserGroup implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="name", length=100)
	private String name;

	@Column(name="code", length=32)
	private String code;

	@Column(name="description", length=255)
	private String description;

	@Override
	public String getAuthority() {
		return name;
	}
}
