CREATE DATABASE IF NOT EXISTS bibliotech_db;
USE bibliotech_db;

--  TABLA USERS
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    identification VARCHAR(50) NOT NULL UNIQUE,
    user_type INT NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT chk_user_type CHECK (user_type IN (1, 2, 3))
);

--  TABLA BOOKS
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE
);

-- TABLA LOANS
CREATE TABLE IF NOT EXISTS loans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(50) NOT NULL,
    user_identification VARCHAR(50) NOT NULL,
    user_type INT NOT NULL,
    loan_date DATE NOT NULL,
    max_return_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    
    -- Restricciones
    CONSTRAINT chk_loan_user_type CHECK (user_type IN (1, 2, 3)),
    CONSTRAINT fk_loan_book FOREIGN KEY (isbn) REFERENCES books(isbn),
    CONSTRAINT fk_loan_user FOREIGN KEY (user_identification) REFERENCES users(identification)
);
