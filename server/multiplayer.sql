DROP SCHEMA IF EXISTS `multiplayer`;
CREATE SCHEMA `multiplayer`;
USE `multiplayer`;

CREATE TABLE `user` (
                        `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                        `username` VARCHAR(20) NOT NULL UNIQUE,
                        `email` VARCHAR(100) NOT NULL UNIQUE,
                        `password` VARCHAR(100) NOT NULL
) DEFAULT CHARSET=utf8;

INSERT INTO application_user (`id`, username, `email`, password) VALUES
(1, 'Jorn', 'jorn.ihlenfeldt@stud.th-luebeck.de', '$2y$10$e/5oVjcIZOWEAPckO2V90./Tj1GWmscQaDmIGQfrvzmIN5IjbWBV6'),
(2, 'Nora', 'nora.kuehnel@stud.th-luebeck.de', '$2y$10$e/5oVjcIZOWEAPckO2V90./Tj1GWmscQaDmIGQfrvzmIN5IjbWBV6');