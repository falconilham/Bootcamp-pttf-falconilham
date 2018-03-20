
DROP TABLE IF EXISTS `vendor_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vendor_id` bigint(20) NOT NULL,
  `message` VARCHAR(512) NOT NULL DEFAULT '',
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
