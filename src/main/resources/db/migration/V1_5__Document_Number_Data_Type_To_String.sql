ALTER TABLE `customer_documents` 
CHANGE COLUMN `number` `document_number` VARCHAR(255) NULL COMMENT '' AFTER `id`;

ALTER TABLE `vendor_documents` 
CHANGE COLUMN `number` `document_number` VARCHAR(255) NULL COMMENT '' AFTER `id`;
