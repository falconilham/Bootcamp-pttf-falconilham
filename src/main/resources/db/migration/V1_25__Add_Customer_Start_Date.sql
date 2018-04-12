ALTER TABLE `customer` 
ADD COLUMN `active_date` DATE NULL AFTER `credit_limit`;

CREATE TABLE `customers_salesperson` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT NULL,
  `username` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));
