# Задание 16. Использовать метрики, healthchecks, logfile и Spring Data REST Repository

###Описание задания

**Цель:** Реализовать production-grade мониторинг и прозрачность в приложении.

**Результат:** Приложение с применением Spring Boot Actuator.

1. Подключить Spring Boot Actuator в приложение.
2. Включить метрики, healthchecks и logfile.
3. Реализовать собственную метрику, InfoContributor, HealthIndicator или Endpoint
4. UI для данных от Spring Boot Actuator реализовывать не нужно.
5. Опционально: переписать приложение на HATEOAS принципах с помощью Spring Data REST Repository
--------------------------------------------------------

Работа выполнена на основании "Задание 10.
 Приложение с использованием AJAX и REST-контроллеров. 
 Отображение на страницах с помощью jQuery".
 
Контроллер и сервис для жанров заменены на Spring Data REST Repository.

Подключены Spring Boot Actuator и логирование.

Для Spring Boot Actuator добавлена информация о версиях приложения, JDK, JVM и 
нестандартный HealthIndicator. 