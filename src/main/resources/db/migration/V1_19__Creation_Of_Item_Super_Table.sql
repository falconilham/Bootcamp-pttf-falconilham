DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_type_id` INT(11) NOT NULL,
  `pricing_unit_id` BIGINT(20) NULL,
  `description` varchar(255) NULL,
  `name` varchar(255) NULL,
  `origin_airport_id` BIGINT(20) NULL,
  `destination_airport_id` BIGINT(20) NULL,
  `status` INT(11) NOT NULL,
  `create_user` VARCHAR(255) NULL DEFAULT NULL,
  `create_date` DATETIME NULL,
  `lastupdate_user` VARCHAR(255) NULL,
  `lastupdate_date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `item_purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_purchase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) NOT NULL,
  `vendor_id` BIGINT(20) NOT NULL,
  `quote_date` DATE NULL,
  `review_date` DATE NULL,
  `status` INT(11) NOT NULL,
  `create_user` VARCHAR(255) NULL DEFAULT NULL,
  `create_date` DATETIME NULL,
  `lastupdate_user` VARCHAR(255) NULL,
  `lastupdate_date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `item_purchase_pricing_tiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_purchase_pricing_tiers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_purchase_id` bigint(20) NOT NULL,
  `index_order` bigint(20) NULL,
  `lower_limit` bigint(20) NULL,
  `upper_limit` bigint(20) NULL,
  `is_single_price` TINYINT NULL,
  `price` decimal(19, 4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `product_feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_feature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` VARCHAR(512) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `product_feature` VALUES
	(NULL, 'G', 'General Goods'),
	(NULL, 'DG', 'Dangerous Goods');
	
	
DROP TABLE IF EXISTS `items_legal_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `items_legal_notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) NOT NULL,
  `legal_notes_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP TABLE IF EXISTS `items_product_features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `items_product_features` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) NOT NULL,
  `product_features_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DELETE from `setting` WHERE `name`='VENDOR_FILE_LOCATION';
DELETE from `setting` WHERE `name`='CUSTOMER_FILE_LOCATION';
DELETE from `setting` WHERE `name`='PURCHASE_DEFAULT_REVIEW_DATE';

INSERT INTO `setting` VALUES 
('CUSTOMER_FILE_LOCATION', '1', 'Location to save customer files in', '1', 'c:/temp/tf/customers/'),
('VENDOR_FILE_LOCATION', '2', 'Location to save vendor files in', '1', 'c:/temp/tf/vendors/'),
('PURCHASE_DEFAULT_REVIEW_DATE', '3', 'Default Number Of days until next review of Purchase Price', '2', '30');
