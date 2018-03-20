
DROP TABLE IF EXISTS `pricing_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pricing_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` VARCHAR(512) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `pricing_unit` VALUES
	(NULL, 'Doc', 'Per Document'),
	(NULL, 'BL', 'Per BL'),
	(NULL, '20''', 'Per 20 Feet'),
	(NULL, '40''', 'Per 40 Feet'),
	(NULL, 'Carry', 'Per Carry'),
	(NULL, 'Carry Box', 'Per Carry Box'),
	(NULL, 'Colt Single', 'Per Colt Single'),
	(NULL, 'Colt Double', 'Per Colt Double'),
	(NULL, 'Colt Single Box', 'Per Colt Single Box'),
	(NULL, 'Colt Double Box', 'Per Colt Double Box'),
	(NULL, 'Fuso', 'Per Fuso'),
	(NULL, 'Fuso Box', 'Per Fuso Box'),
	(NULL, 'Tronton', 'Per Tronton'),
	(NULL, 'Tronton Long', 'Per Tronton Long'),
	(NULL, 'Wingbox', 'Per Wing Box'),
	(NULL, 'CBM', 'Per Cubic Metre');
	