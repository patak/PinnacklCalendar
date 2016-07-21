-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Jeu 21 Juillet 2016 à 23:50
-- Version du serveur :  5.7.9
-- Version de PHP :  5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `javaee`
--

-- --------------------------------------------------------

--
-- Structure de la table `events`
--

DROP TABLE IF EXISTS `events`;
CREATE TABLE IF NOT EXISTS `events` (
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
  KEY `organizer_id` (`organizer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `events`
--

INSERT INTO `events` (`event_id`, `name`, `description`, `place`, `latitude`, `longitude`, `startDate`, `finishDate`, `photo`, `organizer_id`) VALUES
(1, 'plop', '', 'maison', 0, 0, '2017-04-06 00:00:00', NULL, NULL, 4),
(2, 'Soirée Mousse', 'Tous en slip de bain', 'ESGI', 48.849145, 48.849145, '2016-07-21 00:00:00', '2016-07-23 00:00:00', NULL, 4),
(8, 'Plop zuper event', '', 'Paris', 0, 0, '2016-07-14 00:00:00', NULL, '', 6),
(9, 'Plop zuper event', '', 'Paris', 51.5073509, 51.5073509, '2016-07-16 00:00:00', '2016-07-16 00:00:00', '', 6),
(10, 'Plop zuper event', 'Plop zuper event', 'Paris', 51.5073509, 51.5073509, '2016-07-16 00:00:00', '2016-07-16 00:00:00', '', 6),
(11, 'Plop zuper event', '', 'Paris', 0, 0, '2016-07-21 00:00:00', NULL, '', 6);

-- --------------------------------------------------------

--
-- Structure de la table `shared`
--

DROP TABLE IF EXISTS `shared`;
CREATE TABLE IF NOT EXISTS `shared` (
  `shared_id` int(5) NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `event_id` int(4) NOT NULL,
  PRIMARY KEY (`shared_id`),
  KEY `owner_id` (`owner_id`),
  KEY `user_id` (`user_id`),
  KEY `event_id` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `shared`
--

INSERT INTO `shared` (`shared_id`, `owner_id`, `user_id`, `event_id`) VALUES
(1, 4, 6, 2);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(128) NOT NULL,
  `pseudo` varchar(128) NOT NULL,
  `password` varchar(255) NOT NULL,
  `firstname` varchar(128) DEFAULT NULL,
  `lastname` varchar(128) DEFAULT NULL,
  `color` varchar(32) NOT NULL DEFAULT '#3b91ad',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `pseudo` (`pseudo`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`user_id`, `email`, `pseudo`, `password`, `firstname`, `lastname`, `color`) VALUES
(4, 'lorem@lorem', 'lorem', '$2a$12$Ly1O0qua1kVdTOCS3Czx5OnxlTaefEqCr5TCu4EdOyxRRRCzGoQyG', NULL, NULL, '#3b91ad'),
(5, 'plop@plop', 'plop', '$2a$12$ROr5oTA5d1ZwILP3IF5.3.peGnAy915ydiI0mIXh2UnO3R8tEYDDy', NULL, NULL, '#3b91ad'),
(6, 'herve.tutuaku@gmail.com', 'syu', '$2a$12$JVptR2Vl8htWxxfZutVJBu2goshHp.J0KtF.CqWJxM.BZDl9yJ8Z2', NULL, NULL, '#3b91ad'),
(7, 'pataky@hotmail.fr', 'patak', '$2a$12$/NcPhXOol7LfamTFpR.miegwjudoOLIEdbpt4eN9fG/FUSBxCPl4K', NULL, NULL, '#3b91ad');

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `events`
--
ALTER TABLE `events`
  ADD CONSTRAINT `organizer` FOREIGN KEY (`organizer_id`) REFERENCES `users` (`user_id`);

--
-- Contraintes pour la table `shared`
--
ALTER TABLE `shared`
  ADD CONSTRAINT `shared_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION,
  ADD CONSTRAINT `shared_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION,
  ADD CONSTRAINT `shared_ibfk_3` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`) ON DELETE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
