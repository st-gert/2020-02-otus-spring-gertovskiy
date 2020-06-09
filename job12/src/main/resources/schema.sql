-- Security: users with roles
CREATE TABLE IF NOT EXISTS user (
    id bigserial,
    username varchar(30) NOT NULL,
    password varchar(255) NOT NULL,
    roles varchar(255) NOT NULL,    -- list of roles, separated by commas
    CONSTRAINT user_pk PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS genre (
    id bigserial,
	genre_name varchar(255) NOT NULL,
	CONSTRAINT genre_pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS author (
    id bigserial,
	first_name varchar(255) NULL,
	last_name varchar(255) NOT NULL,
	CONSTRAINT author_pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS book (
    id bigserial,
	title varchar(255) NOT NULL,
	genre_id bigint NOT NULL,
	CONSTRAINT book_pk PRIMARY KEY (id),
	CONSTRAINT book_fk FOREIGN KEY (genre_id) REFERENCES  genre(id)
);

CREATE TABLE IF NOT EXISTS author_book (
	book_id bigint NOT NULL,
	author_id bigint NOT NULL,
	CONSTRAINT author_book_pk PRIMARY KEY (author_id, book_id),
	CONSTRAINT author_book_fk_0 FOREIGN KEY (book_id) REFERENCES  book(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT author_book_fk_1 FOREIGN KEY (author_id) REFERENCES  author(id)
);

CREATE TABLE IF NOT EXISTS review (
    id bigserial,
	book_id bigint NOT NULL,
	opinion varchar(1500) NOT NULL,
	CONSTRAINT review_pk PRIMARY KEY (id),
	CONSTRAINT review_fk FOREIGN KEY (book_id) REFERENCES  book(id)  ON DELETE CASCADE ON UPDATE CASCADE
);

