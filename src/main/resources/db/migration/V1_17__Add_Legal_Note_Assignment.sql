
DROP TABLE IF EXISTS `item_permit_legal_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_permit_legal_notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_permit_id` bigint(20) NOT NULL,
  `legal_notes_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `item_trucking_legal_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_trucking_legal_notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_trucking_id` bigint(20) NOT NULL,
  `legal_notes_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `item_airfreight_legal_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_airfreight_legal_notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_airfreight_id` bigint(20) NOT NULL,
  `legal_notes_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `item_seafreight_legal_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_seafreight_legal_notes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_seafreight_id` bigint(20) NOT NULL,
  `legal_notes_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;