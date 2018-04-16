ALTER TABLE `user` 
ADD COLUMN `user_code` VARCHAR(32) NULL DEFAULT '' AFTER `username`;

ALTER TABLE `user_group` 
ADD COLUMN `code` VARCHAR(32) NULL DEFAULT '' AFTER `name` ,
ADD COLUMN `description` VARCHAR(255) NULL DEFAULT '' AFTER `name` ,
DROP COLUMN `create_user`,
DROP COLUMN `create_date`,
DROP COLUMN `lastupdate_user`,
DROP COLUMN `lastupdate_date`;


CREATE TABLE `users_user_groups` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NULL,
  `usergroup_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`));

UPDATE user_group set code='SP', description='Salesperson' where name='ROLE_SALESPERSON';
UPDATE user_group set code='AD', description='Administrator' where name='ROLE_ADMINISTRATOR';
UPDATE user_group set code='SD', description='Sales Director' where name='ROLE_SALESDIRECTOR';
UPDATE user_group set code='PR', description='Purchasing' where name='ROLE_PURCHASING';
UPDATE user_group set code='PM', description='Purchasing Manager' where name='ROLE_PURCHASINGMANAGER';
UPDATE user_group set code='FI', description='Finance' where name='ROLE_FINANCE';
UPDATE user_group set code='DD', description='Director' where name='ROLE_DIRECTOR';

insert into `users_user_groups` (username, usergroup_id) 
	select username, user_group_id from user;

ALTER TABLE `user` 
DROP COLUMN `user_group_id`;

