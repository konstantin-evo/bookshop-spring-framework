DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

create table authors (
                         id INT,
                         first_name VARCHAR(50),
                         last_name VARCHAR(50)
);

CREATE TABLE books(
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      price_old VARCHAR(250) DEFAULT NULL,
                      price VARCHAR(250) DEFAULT NULL,
                      title VARCHAR(250) NOT NULL,
                      author_id INT,
                      FOREIGN KEY (author_id) REFERENCES authors(id)
);