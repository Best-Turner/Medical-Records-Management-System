# (Medical Records Management System) - система управления медицинскими записями.


## Описание:
Программа для системы управления медицинскими записями,
которая позволяет вести учет пациентов,
записей об их медицинской истории,
проведенных процедур и препаратов,
назначенных врачом.

## Требования:

1)	Сделать REST сервис с использованием JDBC и Servlet
2)	Функционал любой на выбор, минимум CRUD сервис с несколькими видами entity
3)	Запрещено использовать Spring, Hibernate, Lombok
4)	Можно использовать Hikari CP, Mapstruckt
5)	Параметры подключения к БД должны быть вынесены в файл
6)	Должны быть реализованы связи ManayToOne(OneToMany), ManyToMany.
7)	Связи должны быть отражены в коде(должны быть соответствующие коллекции внутри энтити)
8)	Сервлет должен возвращать DTO, не возвращаем Entity, принимать также DTO
9)	Должна быть правильная архитектура.
10)	Должен быть сервисный слой
11)	Должны соблюдаться принципы ООП, Solid
12)	Должны быть unit тесты, использовать Mockito и Junit, для проверки работы репозитория(DAO) с БД использовать testcontainers:
13)	Покрытие тестами должно быть больше 80%
14)	Должны быть протестированы все слои приложения
15)	Слой сервлетов, сервисный слой тестировать с помощью Mockito
16)	БД на выбор Pestgres, MySQL