/*
SQLyog Community v13.0.1 (64 bit)
MySQL - 5.6.19-log : Database - jbpm
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jbpm` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jbpm`;

/*Table structure for table `kpi_role` */

DROP TABLE IF EXISTS `kpi_role`;

CREATE TABLE `kpi_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) NOT NULL,
  `staus` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `kpi_role` */

insert  into `kpi_role`(`id`,`rolename`,`status`) values 
(1,'Administrators',1,0),
(2,'PM',1),
(3,'SVP',1),
(4,'EVP',1),
(5,'Admin',1);

/*Table structure for table `kpi_user` */

DROP TABLE IF EXISTS `kpi_user`;

CREATE TABLE `kpi_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `age` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `kpi_user` */

insert  into `kpi_user`(`id`,`username`,`password`,`age`,`status`) values 
(1,'Nero','$2a$10$6NQ7ZWOGHT.W7JhlIa6hQOpABmGlBW7Essr5m78Pu.S3Gav7rha/K',23,1),
(2,'JOJO','$2a$10$6NQ7ZWOGHT.W7JhlIa6hQOpABmGlBW7Essr5m78Pu.S3Gav7rha/K',17,1),
(3,'Test','$2a$10$6NQ7ZWOGHT.W7JhlIa6hQOpABmGlBW7Essr5m78Pu.S3Gav7rha/K',99,1),
(4,'Administrator','$2a$10$6NQ7ZWOGHT.W7JhlIa6hQOpABmGlBW7Essr5m78Pu.S3Gav7rha/K',100,1);

/*Table structure for table `kpi_user_role` */

DROP TABLE IF EXISTS `kpi_user_role`;

CREATE TABLE `kpi_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `rid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `rid` (`rid`),
  CONSTRAINT `kpi_user_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `kpi_role` (`id`),
  CONSTRAINT `kpi_user_role_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `kpi_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Data for the table `kpi_user_role` */

insert  into `kpi_user_role`(`id`,`uid`,`rid`) values 
(1,1,1),
(2,2,3),
(3,3,4),
(4,1,2),
(5,1,3),
(6,2,4),
(7,2,1),
(8,1,5),
(9,2,5),
(10,3,5),
(11,4,1);
