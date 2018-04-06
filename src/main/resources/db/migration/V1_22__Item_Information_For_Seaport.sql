ALTER TABLE `items` 
ADD COLUMN `destination_seaport_id` BIGINT NULL DEFAULT NULL AFTER `destination_airport_id`,
ADD COLUMN `origin_seaport_id` BIGINT NULL DEFAULT NULL AFTER `destination_airport_id`;
