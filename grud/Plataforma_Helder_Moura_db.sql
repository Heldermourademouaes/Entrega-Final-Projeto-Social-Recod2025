CREATE DATABASE IF NOT EXISTS Plataforma_Helder_Moura_db;
USE agenda_db;

CREATE TABLE IF NOT EXISTS eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    local VARCHAR(255) NOT NULL,
    data_hora DATETIME NOT NULL,
    descricao TEXT
);

CREATE TABLE IF NOT EXISTS contatos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    assunto VARCHAR(255),
    mensagem TEXT
);




