# job4j_todo
В рамках проекта разработан сайт для ведения списка задач. Основные функции:
- Просмотр списка задач с возможностью отбора (все / выполненные / новые)
- Редактирование задач
- Выполнение задач

## Используемые технологии
- Java 17, Spring Boot 2.7.3 (Web, Thymeleaf, Test)
-  PostgreSQL 16.2, Liquibase 4.15.0, Hibernate 5.6.11

## Требования к окружению
- Java 17
- Maven 3.9.5
- PostgreSQL 16.2

## Запуск проекта
1. Создать новую базу данных **todo**.
2. Перейти в папку с проектом.
3. В файлах **db/liquibase.properties**, **src/main/resources/hibernate.cfg.xml** скорректировать настройки подключения к БД **todo**.
4. Создать схему базы данных:

    `mvn -P production liquibase:update`

5. Создать jar:

    `mvn package`

6. Запустить программу: 

    `java -jar target/job4j_todo-1.0-SNAPSHOT.jar`

7. Перейти в браузере по адресу: http://localhost:8080/

## Примеры страниц
### Список задач:
<img width="861" alt="task list" src="https://github.com/bsedykh/job4j_todo/assets/84812761/b161c81c-e17a-410c-b3ef-402346fa9a68">

### Создание задачи:
<img width="860" alt="create task" src="https://github.com/bsedykh/job4j_todo/assets/84812761/48aab6ed-e0db-4230-8a5b-2126977d54ce">

### Редактирование задачи:
<img width="993" alt="edit task" src="https://github.com/bsedykh/job4j_todo/assets/84812761/9592f380-46b0-4771-8fc8-ca8f15f41824">
