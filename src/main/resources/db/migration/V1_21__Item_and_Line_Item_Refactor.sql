DROP TABLE IF EXISTS `quotation_line_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quotation_line_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `quotation_id` bigint(20) NOT NULL,
  `item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `item_quote_pricing_tiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_quote_pricing_tiers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `quotation_line_item_id` bigint(20) NOT NULL,
  `index_order` bigint(20) NULL,
  `lower_limit` bigint(20) NULL,
  `upper_limit` bigint(20) NULL,
  `is_single_price` TINYINT NULL,
  `price` decimal(19, 4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;