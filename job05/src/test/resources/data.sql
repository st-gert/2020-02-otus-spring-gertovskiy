
INSERT INTO genre (id,genre_name) VALUES 
(1,'Фантастика')
,(3,'Детектив')
,(4,'Дневники')
,(2,'Сатира')
ON CONFLICT ON CONSTRAINT genre_pk DO NOTHING
;

INSERT INTO author (id,first_name,last_name) VALUES 
(1,'Аркадий','Стругацкий')
,(2,'Борис','Стругацкий')
,(3,'Илья','Ильф')
,(4,'Евгений','Петров')
,(5,'Кир','Булычев')
,(6,'Борис','Акунин')
,(7,'Агата','Кристи')
ON CONFLICT ON CONSTRAINT author_pk DO NOTHING
;

INSERT INTO book (id,title,genre_id) VALUES 
(1,'Понедельник начинается в субботу',1)
,(2,'Сказка о Тройке',1)
,(3,'Комментарии к пройденному',4)
,(4,'Путешествие Алисы',1)
,(5,'Сто лет тому вперед',1)
,(6,'Двенадцать стульев',2)
,(7,'Золотой телёнок',2)
,(8,'Записные книжки',4)
,(9,'Десять негритят',3)
,(10,'Убийство в „Восточном экспрессе“',3)
,(11,'Азазель',3)
,(12,'Статский советник',3)
ON CONFLICT ON CONSTRAINT book_pk DO NOTHING
;

INSERT INTO author_book (book_id,author_id) VALUES 
(1,1)
,(1,2)
,(2,1)
,(2,2)
,(4,5)
,(5,5)
,(6,3)
,(6,4)
,(7,3)
,(7,4)
,(8,3)
,(9,7)
,(10,7)
,(11,6)
,(12,6)
,(3,2)
ON CONFLICT ON CONSTRAINT author_book_pk DO NOTHING
;
