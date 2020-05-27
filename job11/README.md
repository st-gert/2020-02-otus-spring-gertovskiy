# Задание 11. Приложение с использованием WebFlux

###Описание задания
Переписать приложение с использованием WebFlux.

**Цель:** Pазрабатывать Responsive и Resilent приложения на реактивном стеке Spring c помощью Spring Web Flux и Reactive Spring Data Repositories.

**Результат:** Приложение на реактивном стеке Spring.

1. Задание выполняется на основе ДЗ с MongoDB.
2. Вы можете выбрать другую доменную модель (не библиотеку) и другую БД (Redis).
3. Необходимо использовать Reactive Spring Data Repositories. 
    Использовать PostgreSQL и экспериментальную R2DBC не рекомендуется.
4. RxJava vs Project Reactor - на Ваш вкус.
5. Вместо классического Spring MVC и embedded Web-сервера использовать WebFlux.
6. Опционально: реализовать на Functional Endpoints

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
Слой сервисов не используется.
 #### Контроллеры
 REST-контроллеры для каждого типа объектов:
* BookController - в императивном стиле
* GenreController - в императивном стиле
* AuthorController - в функциональном стиле
* ReviewController - в функциональном стиле
#### Пользовательский интерфейс
AJAX c использованием jQuery.

Файлы - в папке resources/public.

