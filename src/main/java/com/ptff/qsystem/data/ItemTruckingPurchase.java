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
import javax.validation.constraints.Min;
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

@Data
@Entity
@Table(name="item_trucking_purchase")
@EntityListeners(AuditingEntityListener.class)
public class ItemTruckingPurchase {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="item_trucking_id")
	@NotNull
	private ItemTrucking item;
	
	@ManyToOne
	@JoinColumn(name="vendor_id")
	@NotNull
	private Vendor vendor;
	
	@Column(name="price")
	@Min(0)
	@NotNull
	private BigDecimal price;
	
	@Column(name="quote_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate quoteDate;
	
	@Column(name="review_date")
	@Convert(converter = LocalDatePersistenceConverter.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate reviewDate;
	
	@Column(name="status")
	private ItemPurchaseStatus status;
	

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
