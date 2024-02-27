ALTER TABLE tasks
   ADD COLUMN user_id INT REFERENCES users(id);
