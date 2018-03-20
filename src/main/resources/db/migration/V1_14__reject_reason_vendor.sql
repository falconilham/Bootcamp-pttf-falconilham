ALTER TABLE `vendor` 
ADD COLUMN `reject_reason` VARCHAR(255) NOT NULL DEFAULT '' AFTER `telephone`;
