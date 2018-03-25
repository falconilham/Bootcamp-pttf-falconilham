ALTER TABLE `item_permit_purchase` 
DROP COLUMN `price`,
CHANGE COLUMN `item_permit_id` `item_permit_id` BIGINT(20) NOT NULL COMMENT '' AFTER `id`,
CHANGE COLUMN `vendor_id` `vendor_id` BIGINT(20) NOT NULL COMMENT '' AFTER `item_permit_id`,
CHANGE COLUMN `create_user` `create_user` VARCHAR(255) NULL DEFAULT NULL COMMENT '' AFTER `status`,
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL COMMENT '' AFTER `create_user`,
CHANGE COLUMN `lastupdate_user` `lastupdate_user` VARCHAR(255) NULL COMMENT '' AFTER `create_date`,
CHANGE COLUMN `lastupdate_date` `lastupdate_date` DATETIME NULL DEFAULT NULL COMMENT '' AFTER `lastupdate_user`;

DROP TABLE IF EXISTS `item_permit_purchase_pricing_tiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_permit_purchase_pricing_tiers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_permit_purchase_id` bigint(20) NOT NULL,
  `lower_limit` bigint(20) NULL,
  `upper_limit` bigint(20) NULL,
  `price` decimal(19, 4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
