DROP TABLE IF EXISTS `item_minimum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_minimum` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) NOT NULL,
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


DROP TABLE IF EXISTS `item_minimum_pricing_tiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_minimum_pricing_tiers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_minimum_id` bigint(20) NOT NULL,
  `index_order` bigint(20) NULL,
  `lower_limit` bigint(20) NULL,
  `upper_limit` bigint(20) NULL,
  `is_single_price` TINYINT NULL,
  `price` decimal(19, 4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DELETE from `setting` WHERE `name`='MINIMUM_DEFAULT_REVIEW_DATE';

INSERT INTO `setting` VALUES 
('MINIMUM_DEFAULT_REVIEW_DATE', '3', 'Default Number Of days until next review of Minimum Price', '2', '60');
