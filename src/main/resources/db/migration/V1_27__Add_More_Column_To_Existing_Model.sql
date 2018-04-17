ALTER TABLE `quotation` 
ADD COLUMN `linked_quotation` BIGINT(20) NULL AFTER `expiry_date`,
ADD COLUMN `salesperson_id` VARCHAR(100) NULL AFTER `expiry_date`;

ALTER TABLE `customer_contact_person` 
ADD COLUMN `is_correspondent` TINYINT NULL AFTER `type`;