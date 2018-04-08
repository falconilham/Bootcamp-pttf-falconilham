update items set pricing_unit_id = 1;

ALTER TABLE `items` 
CHANGE COLUMN `pricing_unit_id` `pricing_unit_id` BIGINT(20) NOT NULL DEFAULT 1 AFTER `item_type_id`;