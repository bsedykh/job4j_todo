CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   name TEXT,
   login TEXT UNIQUE,
   password TEXT
);
