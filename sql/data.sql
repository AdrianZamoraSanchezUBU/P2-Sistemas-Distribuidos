CREATE DATABASE IF NOT EXISTS practica2;
USE practica2;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

INSERT INTO users (username, password, rol) VALUES
('admin', '123', 'ADMIN');