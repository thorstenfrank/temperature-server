CREATE TABLE measurements
(
  `id` bigint(20) PRIMARY KEY ,
  `name` varchar(255) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `unit` int(11) DEFAULT NULL,
  `value` double NOT NULL
) DEFAULT CHARSET=utf8;

# Not supported until MariaDB10.3, so we'll create the sequence table for hibernate manually
# CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
);

INSERT INTO `hibernate_sequence` (`next_val`) VALUES (1);