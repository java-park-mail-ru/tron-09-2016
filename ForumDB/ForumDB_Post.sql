-- MySQL dump 10.13  Distrib 5.7.15, for Linux (x86_64)
--
-- Host: localhost    Database: ForumDB
-- ------------------------------------------------------
-- Server version	5.7.15-0ubuntu0.16.04.1

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
-- Table structure for table `Post`
--

DROP TABLE IF EXISTS `Post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Post` (
  `date` datetime NOT NULL,
  `dislikes` int(11) NOT NULL DEFAULT '0',
  `forum` varchar(255) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isApproved` tinyint(1) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL,
  `isEdited` tinyint(1) NOT NULL,
  `isHighlighted` tinyint(1) NOT NULL,
  `isSpam` tinyint(1) NOT NULL,
  `likes` int(11) NOT NULL DEFAULT '0',
  `message` text,
  `parent` int(11) DEFAULT NULL,
  `points` int(11) NOT NULL DEFAULT '0',
  `thread` int(11) NOT NULL,
  `user` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Post_id_uindex` (`id`),
  KEY `Post_User_email_fk` (`user`),
  KEY `Post_Thread_id_fk` (`thread`),
  KEY `Post_Forum_short_name_fk` (`forum`),
  CONSTRAINT `Post_Forum_short_name_fk` FOREIGN KEY (`forum`) REFERENCES `Forum` (`short_name`),
  CONSTRAINT `Post_Thread_id_fk` FOREIGN KEY (`thread`) REFERENCES `Thread` (`id`),
  CONSTRAINT `Post_User_email_fk` FOREIGN KEY (`user`) REFERENCES `User` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-24 10:28:22
