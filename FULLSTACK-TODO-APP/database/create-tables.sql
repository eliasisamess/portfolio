-- !!!! PORTFOLIO NOTES !!!!

-- These SQL commands create a fresh install of the database needed to use our application.

-- !!!! ORIGINAL CODE STARTS HERE !!!!

-- TODO APP DATABASE CREATE TABLE MYSQL COMMANDS FOR FRESH INSTALL
--
-- Authors Elias Puukari & Lauri Pääjärvi
-- Last update on 2020-12-20
--
CREATE TABLE task
(
    id INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT NOW(),
    modified TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    title VARCHAR(500) NOT NULL,
    minutes INT UNSIGNED NOT NULL DEFAULT 15,
    due DATE,
    isCompleted BOOLEAN NOT NULL DEFAULT FALSE,
    isArchived BOOLEAN NOT NULL DEFAULT FALSE
)
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
;
CREATE TABLE subtask
(
    id INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    maintask_id INT NOT NULL,
    title VARCHAR(500) NOT NULL,
    minutes INT UNSIGNED NOT NULL DEFAULT 5,
    isCompleted BOOLEAN NOT NULL DEFAULT FALSE
)
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
;
ALTER TABLE subtask
    ADD CONSTRAINT fk_maintask_id
    FOREIGN KEY (maintask_id) REFERENCES task(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
;
