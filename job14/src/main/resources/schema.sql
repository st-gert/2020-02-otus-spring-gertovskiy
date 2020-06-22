
CREATE TABLE IF NOT EXISTS genre (
    id char(24),
	genre_name varchar(255) NOT NULL,
	CONSTRAINT genre_pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS author (
    id char(24),
	first_name varchar(255) NULL,
	last_name varchar(255) NOT NULL,
	CONSTRAINT author_pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS book (
    id char(24),
	title varchar(255) NOT NULL,
	genre_id char(24) NOT NULL,
	CONSTRAINT book_pk PRIMARY KEY (id),
	CONSTRAINT book_fk FOREIGN KEY (genre_id) REFERENCES  genre(id)
);

CREATE TABLE IF NOT EXISTS author_book (
	book_id char(24) NOT NULL,
	author_id char(24) NOT NULL,
	CONSTRAINT author_book_pk PRIMARY KEY (author_id, book_id),
	CONSTRAINT author_book_fk_0 FOREIGN KEY (book_id) REFERENCES  book(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT author_book_fk_1 FOREIGN KEY (author_id) REFERENCES  author(id)
);

CREATE TABLE IF NOT EXISTS review (
    id char(24),
	book_id char(24) NOT NULL,
	opinion varchar(1500) NOT NULL,
	CONSTRAINT review_pk PRIMARY KEY (id),
	CONSTRAINT review_fk FOREIGN KEY (book_id) REFERENCES  book(id)  ON DELETE CASCADE ON UPDATE CASCADE
);

