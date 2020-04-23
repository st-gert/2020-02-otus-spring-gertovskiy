# Задание 8. Spring Data MongoDB
###Описание задания

Использовать MongoDB и Spring Data для хранения информации о книгах.

Задание выполняется на основе предыдущего.

**Цель:** После выполнения ДЗ вы сможете использовать Spring Data MongoDB и саму MongoDB
            для разработки приложений с хранением данных в нереляционной БД.

**Результат:** Приложение с использованием MongoDB.

1. Использовать Spring Data MongoDB репозитории, а если не хватает функциональности, то и *Operations.
2. Тесты можно реализовать с помощью Flapdoodle Embedded MongoDB.
3. Hibernate, равно, как и JPA, и spring-boot-starter-data-jpa не должно остаться в зависимостях,
   если ДЗ выполняется на основе предыдущего.
4. Как хранить книги, авторов, жанры и комментарии решать Вам. Но перенесённая с реляционной базы структура
   не всегда будет подходить для MongoDB.

## Классы
#### Модель данных
* Book - Книга.
* Author - Автор. 
* Genre - Жанр.
* Review - Отзыв о книге
 #### Репозитории
 Репозитории для каждого типа объектов:
* BookRepository
* AuthorRepository
* GenreRepository
* ReviewRepository
 #### Сервисы
 Сервисы доступа к репозиториям:
* BookService
* AuthorService
* GenreService
* ReviewService
 #### Контроллеры
 Контроллеры обработки пользовательских запросов. Для каждого типа объктов:
* BookController
* AuthorController
* GenreController
* ReviewController
#### Пользовательский интерфейс
Интерфейс консольного приложения (Spring Shell):
* UIBookShell
* UIAuthorShell
* UIGenreShell
* UIReviewShell