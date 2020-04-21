# Задание 7. Spring Data JPA
###Описание задания

Переписать приложение для хранения книг на Spring Data JPA.

Домашнее задание выполняется переписыванием предыдущего на Spring Data JPA.

**Цель:** максимально просто писать слой репозиториев с применением современных подходов.

**Результат:** приложение со слоем репозиториев на Spring Data JPA.

1. Переписать все репозитории по работе с книгами на Spring Data JPA репозитории.
2. Используйте spring-boot-starter-data-jpa.
3. Кастомные методы репозиториев (или с хитрым @Query) покрыть тестами, используя H2.

Это домашнее задание будет использоваться в качестве основы для других ДЗ

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