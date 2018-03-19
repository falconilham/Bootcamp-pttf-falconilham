ALTER TABLE `customer` 
ADD COLUMN `salesperson_id` VARCHAR(255) NOT NULL DEFAULT 'su' AFTER `telephone`;
