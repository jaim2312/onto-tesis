/*
 Navicat Premium Data Transfer

 Source Server         : Local MySQL
 Source Server Type    : MySQL
 Source Server Version : 100126
 Source Host           : localhost:3306
 Source Schema         : TESIS

 Target Server Type    : MySQL
 Target Server Version : 100126
 File Encoding         : 65001

 Date: 22/06/2018 00:01:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for CARGO
-- ----------------------------
DROP TABLE IF EXISTS `CARGO`;
CREATE TABLE `CARGO` (
  `nId` bigint(20) NOT NULL AUTO_INCREMENT,
  `sNombre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`nId`),
  KEY `nId` (`nId`),
  KEY `nId_2` (`nId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CARGO
-- ----------------------------
BEGIN;
INSERT INTO `CARGO` VALUES (7, 'Programador Junior Big Data');
INSERT INTO `CARGO` VALUES (8, 'Programador MVC');
INSERT INTO `CARGO` VALUES (9, 'Analista Junior .Net');
INSERT INTO `CARGO` VALUES (10, 'Java Developer');
INSERT INTO `CARGO` VALUES (11, 'Programador ABAP');
INSERT INTO `CARGO` VALUES (12, 'Senior Front Developer');
INSERT INTO `CARGO` VALUES (13, 'Arquitecto .Net');
COMMIT;

-- ----------------------------
-- Table structure for CONOCIMIENTO
-- ----------------------------
DROP TABLE IF EXISTS `CONOCIMIENTO`;
CREATE TABLE `CONOCIMIENTO` (
  `nId` bigint(20) NOT NULL AUTO_INCREMENT,
  `sNombre` varchar(100) DEFAULT NULL,
  `sURI` varchar(255) DEFAULT NULL,
  `cargo_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`nId`),
  KEY `PK_CARGO_CONOCIMIENTO` (`cargo_id`),
  CONSTRAINT `PK_CARGO_CONOCIMIENTO` FOREIGN KEY (`cargo_id`) REFERENCES `CARGO` (`nId`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CONOCIMIENTO
-- ----------------------------
BEGIN;
INSERT INTO `CONOCIMIENTO` VALUES (1, 'PHP', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#PHP', 7);
INSERT INTO `CONOCIMIENTO` VALUES (2, 'Java', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Java', 7);
INSERT INTO `CONOCIMIENTO` VALUES (3, 'Python', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Python', 7);
INSERT INTO `CONOCIMIENTO` VALUES (4, 'ASP_MVC', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#ASP_MVC', 8);
INSERT INTO `CONOCIMIENTO` VALUES (5, 'C_Sharp', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#C_Sharp', 8);
INSERT INTO `CONOCIMIENTO` VALUES (6, 'VBA', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#VBA', 8);
INSERT INTO `CONOCIMIENTO` VALUES (7, 'VB.NET', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#VB.NET', 8);
INSERT INTO `CONOCIMIENTO` VALUES (8, 'SQL_SERVER', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#SQL_SERVER', 8);
INSERT INTO `CONOCIMIENTO` VALUES (9, 'Bootstrap', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Bootstrap', 8);
INSERT INTO `CONOCIMIENTO` VALUES (10, 'Jquery', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Jquery', 8);
INSERT INTO `CONOCIMIENTO` VALUES (11, 'JavaScript', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#JavaScript', 8);
INSERT INTO `CONOCIMIENTO` VALUES (12, 'Android_Studio', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Android_Studio', 8);
INSERT INTO `CONOCIMIENTO` VALUES (13, 'ASP_MVC', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#ASP_MVC', 9);
INSERT INTO `CONOCIMIENTO` VALUES (14, 'Bootstrap', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Bootstrap', 9);
INSERT INTO `CONOCIMIENTO` VALUES (15, 'Jquery', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Jquery', 9);
INSERT INTO `CONOCIMIENTO` VALUES (16, 'JavaScript', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#JavaScript', 9);
INSERT INTO `CONOCIMIENTO` VALUES (17, 'SQL_SERVER', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#SQL_SERVER', 9);
INSERT INTO `CONOCIMIENTO` VALUES (18, 'Java', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Java', 10);
INSERT INTO `CONOCIMIENTO` VALUES (19, 'Spring', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Spring', 10);
INSERT INTO `CONOCIMIENTO` VALUES (20, 'Git', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Git', 10);
INSERT INTO `CONOCIMIENTO` VALUES (21, 'JUnit', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#JUnit', 10);
INSERT INTO `CONOCIMIENTO` VALUES (22, 'Docker', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Docker', 10);
INSERT INTO `CONOCIMIENTO` VALUES (23, 'ABAP', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#ABAP', 11);
INSERT INTO `CONOCIMIENTO` VALUES (24, 'OpenSQL', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#OpenSQL', 11);
INSERT INTO `CONOCIMIENTO` VALUES (25, 'SAP_HANA', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#SAP_HANA', 11);
INSERT INTO `CONOCIMIENTO` VALUES (26, 'HTML', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#HTML', 12);
INSERT INTO `CONOCIMIENTO` VALUES (27, 'CSS', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#CSS', 12);
INSERT INTO `CONOCIMIENTO` VALUES (28, 'JavaScript', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#JavaScript', 12);
INSERT INTO `CONOCIMIENTO` VALUES (29, 'Jquery', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Jquery', 12);
INSERT INTO `CONOCIMIENTO` VALUES (30, 'AngularJS', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#AngularJS', 12);
INSERT INTO `CONOCIMIENTO` VALUES (31, 'Bootstrap', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Bootstrap', 12);
INSERT INTO `CONOCIMIENTO` VALUES (32, 'Highcharts', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Highcharts', 12);
INSERT INTO `CONOCIMIENTO` VALUES (33, 'ASP', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#ASP', 13);
INSERT INTO `CONOCIMIENTO` VALUES (34, 'Windows_Server', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#Windows_Server', 13);
INSERT INTO `CONOCIMIENTO` VALUES (35, 'SQL_SERVER', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#SQL_SERVER', 13);
INSERT INTO `CONOCIMIENTO` VALUES (36, 'SaaS', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#SaaS', 13);
INSERT INTO `CONOCIMIENTO` VALUES (37, 'PaaS', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#PaaS', 13);
INSERT INTO `CONOCIMIENTO` VALUES (38, 'IaaS', 'http://www.semanticweb.org/jonathan/ontologies/2017/11/TI#IaaS', 13);
COMMIT;

-- ----------------------------
-- Procedure structure for pa_QueCargo
-- ----------------------------
DROP PROCEDURE IF EXISTS `pa_QueCargo`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pa_QueCargo`(IN `totalRegistros` INT, IN `registroActual` INT)
BEGIN

  -- parametros para el datatable.js
  DECLARE inicio INT;
  DECLARE fin INT;

  SET inicio = registroActual + 1;
  SET fin = inicio + (totalRegistros - 1);

  select *
  from 
  (SELECT 
    (@row_number := @row_number + 1) AS row,
    c.nId,c.sNombre
    FROM CARGO AS c,(SELECT @row_number:=0) AS t
    -- WHERE
    -- ((P_INDICADORID <> -1 and e.indicadorid = P_INDICADORID) or (P_INDICADORID = -1))
  ) AS resultado WHERE row BETWEEN inicio AND fin;
  
END;
;;
delimiter ;

-- ----------------------------
-- Procedure structure for pa_Total_QueCargo
-- ----------------------------
DROP PROCEDURE IF EXISTS `pa_Total_QueCargo`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pa_Total_QueCargo`()
BEGIN
  SELECT COUNT(1) 'Total'
  FROM CARGO AS c,(SELECT @row_number:=0) AS t;
  -- WHERE
  --  ((P_CONTEXTOID <> -1 and i.contextoid = P_CONTEXTOID) or (P_CONTEXTOID = -1));
END;
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
