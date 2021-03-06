# Задание 2. Конфигурирование Spring-приложений
###Описание задания

Выполняется на основе предыдущего домашнего задания.

**Цель:** конфигурировать Spring-приложения современным способом, как это и делается в современном мире.

**Результат:** готовое современное приложение на чистом Spring.

1. Переписать конфигурацию в виде **Java + Annotation-based конфигурации.**
2. **Локализовать выводимые сообщения и вопросы** (в CSV-файле).
3. Добавьте **файл настроек для приложения** тестирования студентов. 
В конфигурационный файл можно поместить путь до CSV-файла и/или текущую локаль, 
количество правильных ответов для зачёта - на Ваше усмотрение.
4. Если Вы пишите интеграционные тесты, то не забудьте добавить аналогичный файл и для тестов.

## Классы
#### Модель данных
(Все классы перенесены из предыдущего проекта)
* Person - данные экзаменующегося.
* Questionnaire - вопросник. Содержит список вопросов с ответами и метод подведения итога в процентах.
* QuestionsAnswer - вопрос с ответом; абстрактный. Конкретные классы реализуют 
вопросы с выбором ответа из нескольких вариантов или со свободным ответом. 
Содержит вопрос, варианты ответов, правильный ответ, ответ экзаменующегося. 
#### Сервисы
* ExamController - сценарий экзамена (перенесен из предыдущего проекта).
* QuestionsDAO  - загрузка вопросника (перенесен, изменен поиск файла - по локали).
* MarksCriteria - критерии оценки. 
* UserInterface - реализация пользовательского интерфейса (перенесен, изменение - i18n).
#### Конфигурирование
* ConfigProps - все значения из application.properties.
* MessageSourceConfig - i18n.