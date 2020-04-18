
CREATE SEQUENCE IF NOT EXISTS genre_id_seq
	INCREMENT BY 1
	MINVALUE 100
	MAXVALUE 9223372036854775807
	START 100
	CACHE 1
	NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS author_id_seq
	INCREMENT BY 1
	MINVALUE 100
	MAXVALUE 9223372036854775807
	START 100
	CACHE 1
	NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS book_id_seq
	INCREMENT BY 1
	MINVALUE 100
	MAXVALUE 9223372036854775807
	START 100
	CACHE 1
	NO CYCLE;

-- **************************************************

CREATE TABLE IF NOT EXISTS genre (
    id bigint NOT NULL DEFAULT nextval(' genre_id_seq'::regclass),
	genre_name text NULL,
	CONSTRAINT genre_pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS author (
    id bigint NOT NULL DEFAULT nextval(' author_id_seq'::regclass),
	first_name text NULL,
	last_name text NOT NULL,
	CONSTRAINT author_pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS book (
    id bigint NOT NULL DEFAULT nextval(' book_id_seq'::regclass),
	title text NOT NULL,
	genre_id int4 NOT NULL,
	CONSTRAINT book_pk PRIMARY KEY (id),
	CONSTRAINT book_fk FOREIGN KEY (genre_id) REFERENCES  genre(id)
);

CREATE TABLE IF NOT EXISTS author_book (
	book_id bigint NOT NULL,
	author_id bigint NOT NULL,
	CONSTRAINT author_book_pk PRIMARY KEY (author_id, book_id),
	CONSTRAINT author_book_fk_0 FOREIGN KEY (book_id) REFERENCES  book(id),
	CONSTRAINT author_book_fk_1 FOREIGN KEY (author_id) REFERENCES  author(id)
);

-- **************************************************

CREATE OR REPLACE VIEW  book_lst
AS SELECT b.id AS book_id,
    b.title,
    g.genre_name,
    string_agg((a.first_name || ' '::text) || a.last_name, ', '::text ORDER BY a.id) AS author_name
   FROM book b
     JOIN genre g ON g.id = b.genre_id
     JOIN author_book ab ON b.id = ab.book_id
     JOIN author a ON a.id = ab.author_id
  GROUP BY b.id, b.title, g.genre_name
  ORDER BY b.id;
