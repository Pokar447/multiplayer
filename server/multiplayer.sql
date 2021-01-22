DROP SCHEMA IF EXISTS `multiplayer`;
CREATE SCHEMA `multiplayer`;
USE `multiplayer`;

CREATE TABLE `application_user` (
                        `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                        `username` VARCHAR(20) NOT NULL UNIQUE,
                        `email` VARCHAR(100) NOT NULL UNIQUE,
                        `password` VARCHAR(100) NOT NULL
) DEFAULT CHARSET=utf8;

# Password == 'Password123!'
INSERT INTO application_user (id, username, email, password) VALUES
(1, 'Jorn', 'jorn.ihlenfeldt@stud.th-luebeck.de', '$2a$10$ADIbMfPC6jLkBrKVe/v1ceKMYn3FGh6Z/m6M2VHiLSjkuEsAT2gg.'),
(2, 'Nora', 'nora.kuehnel@stud.th-luebeck.de', '$2a$10$bvvOPTPKsnVyeFAy1nN/yuPRq1w/atbzs.vhic2u.BWzUwhiZYBtG');

CREATE TABLE `history` (
                                    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                    `user_id` INT(20) NOT NULL,
                                    `opponent_id` INT(20) NOT NULL,
                                    `user_hp` INT(20) NOT NULL,
                                    `opponent_hp` INT(20) NOT NULL,
                                    `date_time` DATETIME NOT NULL
) DEFAULT CHARSET=utf8;

INSERT INTO history (id, user_id, opponent_id, user_hp, opponent_hp, date_time) VALUES
(1, 1, 2, 3, 0, now()),
(2, 2, 1, 0, 3, now());