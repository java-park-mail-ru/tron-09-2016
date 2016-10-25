CREATE TABLE `Users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Users_login_uindex` (`login`),
  UNIQUE KEY `Users_email_uindex` (`email`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;