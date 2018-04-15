package com.ptff.qsystem.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllByStatus(NotificationStatus status);
	List<Notification> findAllByUsergroupNameAndStatus(String authority, NotificationStatus unread);
	List<Notification> findAllByUserAndStatus(User user, NotificationStatus unread);
	
	List<Notification> findFirst10ByUsergroupNameOrderByCreateDate(String userGroupName);
	List<Notification> findFirst10ByUserOrderByCreateDate(User user);
	
	
	Long countByUserAndStatus(User user, NotificationStatus unread);
}