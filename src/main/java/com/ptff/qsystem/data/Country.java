package com.ptff.qsystem.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name="country")
public class Country {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="code")
	@NotNull
	@NotEmpty
	@Length(max=2, min=2)
	private String code;
	
	@Column(name="name")
	@NotNull
	@NotEmpty
	private String name;
}
