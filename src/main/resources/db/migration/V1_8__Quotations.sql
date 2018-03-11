
DROP TABLE IF EXISTS `quotation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reference` varchar(255) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `status` int(11) DEFAULT 1,
  `quote_date` date DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `quotation_line_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quotation_line_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `quotation_id` bigint(20) NOT NULL,
  `itempermit_id` bigint(20) NULL,
  `itemairfreight_id` bigint(20) NULL,
  `itemseafreight_id` bigint(20) NULL,
  `itemtrucking_id` bigint(20) NULL,
  `type` int(11) DEFAULT 1,
  `quoted_price` DECIMAL(19,4) NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

