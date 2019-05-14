# Host: 192.168.1.5  (Version 5.7.26-0ubuntu0.18.04.1)
# Date: 2019-05-06 11:28:59
# Generator: MySQL-Front 6.1  (Build 1.26)


#
# Structure for table "UserInfo"
#

DROP TABLE IF EXISTS `UserInfo`;
CREATE TABLE `UserInfo` (
  `Id` int(11) NOT NULL,
  `UserName` varchar(255) NOT NULL DEFAULT '',
  `Email` varchar(255) DEFAULT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "UserInfo"
#

INSERT INTO `UserInfo` VALUES (3,'hale','1','1\n1','1'),(2,'root','564654','ssssssssssss','sdfdfdf'),(1,'wsl','123','123','123');

#
# Structure for table "Friends"
#

DROP TABLE IF EXISTS `Friends`;
CREATE TABLE `Friends` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL DEFAULT '',
  `FriendName` varchar(255) DEFAULT '',
  PRIMARY KEY (`Id`),
  KEY `FriendName` (`FriendName`),
  CONSTRAINT `Friends_ibfk_1` FOREIGN KEY (`FriendName`) REFERENCES `UserInfo` (`UserName`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

#
# Data for table "Friends"
#

INSERT INTO `Friends` VALUES (1,'wsl','root'),(8,'wsl','hale'),(10,'hale','wsl');
