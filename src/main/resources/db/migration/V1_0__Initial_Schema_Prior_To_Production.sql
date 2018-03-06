-- MySQL dump 10.13  Distrib 5.6.26, for Win64 (x86_64)
--
-- Host: localhost    Database: qsystem
-- ------------------------------------------------------
-- Server version	5.6.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `airport`
--

DROP TABLE IF EXISTS `airport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `airport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(3) NOT NULL,
  `name` varchar(255) NOT NULL,
  `country_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb3pphrsecshbmqr9end680veg` (`country_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approval_by` varchar(255) DEFAULT NULL,
  `approval_date` date DEFAULT NULL,
  `corresponsence_address` varchar(255) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `credit_limit` decimal(19,2) DEFAULT NULL,
  `finance_notes` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `npwp` varchar(255) NOT NULL,
  `npwp_address` varchar(255) NOT NULL,
  `operation_notes` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `telephone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_contact_person`
--

DROP TABLE IF EXISTS `customer_contact_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_contact_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) NOT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `position` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1anigqmhbpuchmvv9h29h2ma5` (`customer_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_documents`
--

DROP TABLE IF EXISTS `customer_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_documents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoqdywuig7fbo40qnfhlp8iam8` (`customer_id`),
  KEY `FK4m640292lwq84dfyxbtjnbawn` (`document_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `depot`
--

DROP TABLE IF EXISTS `depot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `depot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `document_category`
--

DROP TABLE IF EXISTS `document_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documents`
--

DROP TABLE IF EXISTS `documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) NOT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_airfreight`
--

DROP TABLE IF EXISTS `item_airfreight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_airfreight` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `destination_airport_id` bigint(20) DEFAULT NULL,
  `origin_airport_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt2wf1k5v1kws75jvix44mdt0a` (`destination_airport_id`),
  KEY `FKqchi52c89yb47eal56j29e40v` (`origin_airport_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_airfreight_purchase`
--

DROP TABLE IF EXISTS `item_airfreight_purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_airfreight_purchase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) NOT NULL,
  `quote_date` date DEFAULT NULL,
  `review_date` date DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `item_airfreight_id` bigint(20) NOT NULL,
  `vendor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKno5sxd0w2djj0wjdccdxwknse` (`item_airfreight_id`),
  KEY `FKnvfvb8pqiu24xvf5ep2psde76` (`vendor_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_permit`
--

DROP TABLE IF EXISTS `item_permit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_permit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_permit_purchase`
--

DROP TABLE IF EXISTS `item_permit_purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_permit_purchase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) NOT NULL,
  `quote_date` date DEFAULT NULL,
  `review_date` date DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `item_permit_id` bigint(20) NOT NULL,
  `vendor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk9lv3j5swpihtcp6sjgebevjy` (`item_permit_id`),
  KEY `FK6iv7r38pdsfk53jbgycpi9adu` (`vendor_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_trucking`
--

DROP TABLE IF EXISTS `item_trucking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_trucking` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `destination_depot` bigint(20) DEFAULT NULL,
  `source_depot` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6yls9rag1g0wh3jqjhq73nstb` (`destination_depot`),
  KEY `FKa8fymionnpgyvi0tn5hngxh00` (`source_depot`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_trucking_purchase`
--

DROP TABLE IF EXISTS `item_trucking_purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_trucking_purchase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) NOT NULL,
  `quote_date` date DEFAULT NULL,
  `review_date` date DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `item_trucking_id` bigint(20) NOT NULL,
  `vendor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1adq7dwj3358f014mndh7fe7r` (`item_trucking_id`),
  KEY `FKn00p4maqk6wqkx8sh74d94kj2` (`vendor_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seaport`
--

DROP TABLE IF EXISTS `seaport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seaport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(3) NOT NULL,
  `name` varchar(255) NOT NULL,
  `country_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKprdstj358gxjaekpianh461v6` (`country_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `setting` (
  `name` varchar(100) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `username` varchar(255) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  `full_name` varchar(255) NOT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `user_group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`username`),
  KEY `FKd5uhmsqhax1l70pck9lmgphjr` (`user_group_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vendor`
--

DROP TABLE IF EXISTS `vendor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approval_by` varchar(255) DEFAULT NULL,
  `approval_date` date DEFAULT NULL,
  `corresponsence_address` varchar(255) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `finance_notes` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `npwp` varchar(255) NOT NULL,
  `npwp_address` varchar(255) NOT NULL,
  `operation_notes` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `telephone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vendor_contact_person`
--

DROP TABLE IF EXISTS `vendor_contact_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor_contact_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) NOT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `position` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `vendor_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK17k10akivxond20qyu98nb4n` (`vendor_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vendor_documents`
--

DROP TABLE IF EXISTS `vendor_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor_documents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `lastupdate_date` datetime DEFAULT NULL,
  `lastupdate_user` varchar(255) DEFAULT NULL,
  `document_id` bigint(20) DEFAULT NULL,
  `vendor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtgs3n6uo323ehay1wpjg2eueu` (`document_id`),
  KEY `FKnt9k126gax7url1kd0au5fhb8` (`vendor_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'qsystem'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-05 10:20:12
