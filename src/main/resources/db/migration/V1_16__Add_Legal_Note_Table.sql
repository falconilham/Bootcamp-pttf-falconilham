
DROP TABLE IF EXISTS `legal_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `legal_note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` VARCHAR(512) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `legal_note` VALUES
	(NULL, 'TKC1', 'Apabila pemakaian mobil lebih dari 24 jam, maka akan dikenaya biaya overnight charge.'),
	(NULL, 'TKC2', 'Harga FOT free on truck, dalam arti tidak termasuk biaya muat atau bongkar'),
	(NULL, 'TKC3', 'Kami tidak menanggung semua barang yang hilang/rusak dijalan.'),
	(NULL, 'TKC4', ''),
	(NULL, 'EDI', 'Harga hanya untuk 40 item. Apabila lebih dari 40 item akan dikenakan biaya extra');
	