/**
 * Author:  ridhv
 */

DROP TABLE users;

CREATE TABLE users
(
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1),
    username VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL
);