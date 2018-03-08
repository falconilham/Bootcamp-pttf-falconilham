ALTER TABLE `setting` 
ADD COLUMN `type` BIGINT NOT NULL COMMENT '' AFTER `description`,
ADD COLUMN `category` BIGINT NOT NULL COMMENT '' AFTER `name`;

INSERT INTO `setting` VALUES('CUSTOMER_FILE_LOCATION', 1, 'Location to save customer files in', 1, 'file://./customers/');
INSERT INTO `setting` VALUES('VENDOR_FILE_LOCATION', 2, 'Location to save vendor files in', 1, 'file://./vendors/');

ALTER TABLE `customer_documents` 
ADD COLUMN `document_type_id` BIGINT NOT NULL COMMENT '' AFTER `id`,
ADD COLUMN `number` BIGINT NULL COMMENT '' AFTER `id`,
ADD COLUMN `start_date` DATE NULL COMMENT '' AFTER `number`,
ADD COLUMN `expiry_date` DATE NULL COMMENT '' AFTER `start_date`;

ALTER TABLE `vendor_documents` 
ADD COLUMN `document_type_id` BIGINT NOT NULL COMMENT '' AFTER `id`,
ADD COLUMN `number` BIGINT NULL COMMENT '' AFTER `id`,
ADD COLUMN `start_date` DATE NULL COMMENT '' AFTER `number`,
ADD COLUMN `expiry_date` DATE NULL COMMENT '' AFTER `start_date`;

INSERT INTO document_category VALUES(NULL, 'NIK');
INSERT INTO document_category VALUES(NULL, 'SIUP');
INSERT INTO document_category VALUES(NULL, 'TDP');
INSERT INTO document_category VALUES(NULL, 'Akta Perusahaan');
INSERT INTO document_category VALUES(NULL, 'NPWP');