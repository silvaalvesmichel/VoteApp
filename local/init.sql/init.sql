CREATE TABLE IF NOT EXISTS
`USERS` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(255) NOT NULL,
`email` varchar(255) NOT NULL,
`password` varchar(255) NOT NULL,
`cpf` varchar(255) NOT NULL,
PRIMARY KEY (`id`)
)