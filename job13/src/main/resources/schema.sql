-- Security: users with roles, indexed by username
CREATE TABLE IF NOT EXISTS user (
    id bigserial,
    username varchar(30) NOT NULL,
    password varchar(255) NOT NULL,
    roles varchar(255) NOT NULL,    -- list of roles, separated by commas
    CONSTRAINT user_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX IF NOT EXISTS user_iu ON user(username);

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
	user_id bigint NOT NULL,
	CONSTRAINT review_pk PRIMARY KEY (id),
	CONSTRAINT review_fk_0 FOREIGN KEY (book_id) REFERENCES  book(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT review_fk_1 FOREIGN KEY (user_id) REFERENCES  user(id)
);

-- Security - ACL
CREATE TABLE IF NOT EXISTS acl_sid (
    id bigserial NOT NULL,
    principal tinyint NOT NULL,
    sid varchar(100) NOT NULL,
    CONSTRAINT acl_sid_pk PRIMARY KEY (id),
    CONSTRAINT acl_sid_uq UNIQUE (sid, principal)
);
CREATE TABLE IF NOT EXISTS acl_class (
    id bigserial,
    class varchar(255) NOT NULL,
    CONSTRAINT acl_class_pk PRIMARY KEY (id),
    CONSTRAINT acl_class_uq UNIQUE (class)
);
CREATE TABLE IF NOT EXISTS acl_object_identity (
    id bigserial,
    object_id_class bigint  NOT NULL,
    object_id_identity bigint NOT NULL,
    parent_object bigint,
    owner_sid bigint,
    entries_inheriting tinyint NOT NULL,
    CONSTRAINT acl_object_identity_pk PRIMARY KEY (id),
    CONSTRAINT acl_object_identity_fk_0 FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),
    CONSTRAINT acl_object_identity_fk_1 FOREIGN KEY (object_id_class) REFERENCES acl_class (id),
    CONSTRAINT acl_object_identity_fk_2 FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)
);
CREATE TABLE IF NOT EXISTS acl_entry (
    id bigserial,
    acl_object_identity bigint NOT NULL,
    ace_order int NOT NULL,
    sid bigint NOT NULL,
    mask int NOT NULL,
    granting tinyint NOT NULL,
    audit_success tinyint NOT NULL,
    audit_failure tinyint NOT NULL,
    CONSTRAINT acl_entry_pk PRIMARY KEY (id),
    CONSTRAINT acl_entry_uq UNIQUE (acl_object_identity,ace_order)
);
