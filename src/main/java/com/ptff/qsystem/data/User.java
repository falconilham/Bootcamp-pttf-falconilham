package com.ptff.qsystem.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ptff.qsystem.data.converter.LocalDateTimePersistenceConverter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="username")
	@NotNull
	@NotEmpty
	private String username;
	
	@Column(name="password")
	@NotNull
	@NotEmpty
	private String password;
	
	@Column(name="email")
	@NotNull
	@Email
	private String email;
	
	@Column(name="full_name")
	@NotNull
	@NotEmpty
	private String fullName;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(name="users_user_groups",
        joinColumns = {@JoinColumn(name="username", referencedColumnName="username")},
        inverseJoinColumns = {@JoinColumn(name="usergroup_id", referencedColumnName="id")}
    )
	private List<UserGroup> userGroups = new ArrayList<UserGroup>();
	
	@Transient
	private UserGroup selectedUserGroup;
	
	@Column(name="enabled", columnDefinition="TINYINT")
	private Boolean enabled;
	
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
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userGroups;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled==null?Boolean.TRUE:enabled;
	}
	
	public void addUserGroup(UserGroup userGroup) {
		userGroups.add(userGroup);
	}
	
	public void removeUserGroup(UserGroup userGroup) {
		userGroups.remove(userGroup);
	}
}
