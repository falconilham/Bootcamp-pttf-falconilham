ALTER TABLE `documents` 
ADD COLUMN `content_type` VARCHAR(255) NOT NULL COMMENT '' AFTER `file_path`;
