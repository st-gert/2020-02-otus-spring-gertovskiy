-- MERGE !!!  not INSERT

MERGE INTO user (id,username,password,roles)
    KEY (id) VALUES
(1, 'owner', '$2a$15$187FtpP.Ah5bvkib.gUAfOEjdnIJCp1qy0Sgs462gEgjeSVKNqDwC', 'ADMIN,REVIEW')
,(2, 'admin', '$2a$15$l6lfELC00LIAjNULLx9fyei/VBuzikmZ4jwrmGeytQpeApMdBs4S6', 'ADMIN')
,(3, 'user', '$2a$15$JQciZxsPzDMesGUi75uea.oZlou1DXx66PwGvp7CvUKRXeXdUXHOm', 'USER')
,(4, 'friend', '$2a$15$aLHZ/H.lleLursQXgxbxjO2elNspBOU13I43o2Skx0pQQ6EQb/bIK', 'USER')
;

MERGE INTO genre (id,genre_name)
 KEY (id) VALUES
(1,'Фантастика')    ,(2,'Сатира')
,(3,'Детектив')     ,(4,'Дневники')
;

MERGE INTO author (id,first_name,last_name)
    KEY (id) VALUES
(1,'Аркадий','Стругацкий')
,(2,'Борис','Стругацкий')
,(3,'Илья','Ильф')
,(4,'Евгений','Петров')
,(5,'Кир','Булычев')
,(6,'Борис','Акунин')
,(7,'Агата','Кристи')
;

MERGE INTO book (id,title,genre_id)
    KEY (id) VALUES
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
;

MERGE INTO author_book (book_id,author_id)
    KEY (book_id,author_id) VALUES
 (1,1)    ,(1,2)    ,(2,1)    ,(2,2)    ,(4,5)
,(5,5)    ,(6,3)    ,(6,4)    ,(7,3)    ,(7,4)
,(8,3)    ,(9,7)    ,(10,7)   ,(11,6)   ,(12,6)    ,(3,2)
;

MERGE INTO review (id, book_id, opinion)
    KEY (id) VALUES
 (1, 1, 'Хорошая книга')
,(2, 1, 'Замечательная книга')
,(3, 1, 'Превосходная!')
,(4, 4, 'Интересно, полезно')
,(5, 4, 'Увлекательно')
,(6, 6, 'Смешно')
,(7, 7, 'Смешно и жизненно')
,(8, 9, 'Так им всем и надо')
;