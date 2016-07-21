-- Adminer 4.2.4 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `event_id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` text,
  `place` varchar(128) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `startDate` datetime NOT NULL,
  `finishDate` datetime DEFAULT NULL,
  `photo` mediumblob,
  `organizer_id` int(4) NOT NULL,
  PRIMARY KEY (`event_id`),
  KEY `organizer_id` (`organizer_id`),
  CONSTRAINT `organizer` FOREIGN KEY (`organizer_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `events` (`event_id`, `name`, `description`, `place`, `latitude`, `longitude`, `startDate`, `finishDate`, `photo`, `organizer_id`) VALUES
(1,	'plop',	'',	'maison',	0,	0,	'2017-04-06 00:00:00',	NULL,	NULL,	4),
(2,	'Soirée Mousse',	'Tous en slip de bain',	'ESGI',	48.849145,	48.849145,	'2016-07-21 00:00:00',	'2016-07-23 00:00:00',	NULL,	4),
(8,	'Plop zuper event',	'',	'Paris',	0,	0,	'2016-07-14 00:00:00',	NULL,	'',	6),
(9,	'Plop zuper event',	'',	'Paris',	51.5073509,	51.5073509,	'2016-07-16 00:00:00',	'2016-07-16 00:00:00',	'',	6),
(10,	'Plop zuper event',	'Plop zuper event',	'Paris',	51.5073509,	51.5073509,	'2016-07-16 00:00:00',	'2016-07-16 00:00:00',	'',	6),
(11,	'Plop zuper event',	'',	'Paris',	0,	0,	'2016-07-21 00:00:00',	NULL,	'',	6);

DROP TABLE IF EXISTS `shared`;
CREATE TABLE `shared` (
  `shared_id` int(5) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `event_id` int(4) NOT NULL,
  PRIMARY KEY (`shared_id`),
  KEY `owner_id` (`owner_id`),
  KEY `user_id` (`user_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `shared_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION,
  CONSTRAINT `shared_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION,
  CONSTRAINT `shared_ibfk_3` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `shared` (`shared_id`, `owner_id`, `user_id`, `event_id`) VALUES
(1,	4,	6,	2);

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `pseudo` varchar(128) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `pseudo` (`pseudo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `users` (`user_id`, `pseudo`, `password`) VALUES
(4,	'lorem',	'$2a$12$Ly1O0qua1kVdTOCS3Czx5OnxlTaefEqCr5TCu4EdOyxRRRCzGoQyG'),
(5,	'plop',	'$2a$12$ROr5oTA5d1ZwILP3IF5.3.peGnAy915ydiI0mIXh2UnO3R8tEYDDy'),
(6,	'syu',	'$2a$12$JVptR2Vl8htWxxfZutVJBu2goshHp.J0KtF.CqWJxM.BZDl9yJ8Z2');

-- 2016-07-21 18:27:50
