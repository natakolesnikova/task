DROP TABLE IF EXISTS users;

CREATE TABLE users (
  `id` INT AUTO_INCREMENT  PRIMARY KEY NOT NULL,
  `first_name` VARCHAR(250) NOT NULL,
  `last_name` VARCHAR(250) NOT NULL,
  `user_name` VARCHAR(250) DEFAULT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into users (id, first_name, last_name, user_name, password) values (1, 'testFirstName', 'testLastName', 'user', '$2a$10$AlvB7yhm.5h7DvwcSJLBceUcp.5MdJ9FChD5O9XW1GRsKf/aft8gq');
