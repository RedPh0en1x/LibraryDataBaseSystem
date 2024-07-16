# Система управления библиотекой

## Обзор

Этот проект демонстрирует проектирование и реализацию системы управления библиотекой с использованием PostgreSQL для управления базой данных и Java JDBC для взаимодействия с базой данных. Система поддерживает операции CRUD (Создание, Чтение, Обновление, Удаление) для управления авторами, книгами, читателями и выдачами книг.

## Функции

- Управление авторами: добавление, обновление, удаление и отображение авторов.
- Управление книгами: добавление, обновление, удаление и отображение книг.
- Управление читателями: добавление, обновление, удаление и отображение читателей.
- Управление выдачами: добавление, удаление и отображение выдач книг.
- Предотвращение удаления читателей или книг, которые имеют активные выдачи.
- Выполнение пользовательских SQL-запросов.

## Требования

- PostgreSQL
- Java Development Kit (JDK)
- JDBC драйвер для PostgreSQL

## Схема базы данных

База данных состоит из четырех основных таблиц: `Authors`, `Books`, `Readers` и `Issuances`.

<img width="1000" alt="image" src="https://github.com/user-attachments/assets/167d853b-1e60-4515-a364-1501ed223e4d">


### Таблица Authors

| Колонка    | Тип     | Описание               |
|------------|---------|------------------------|
| author_id  | SERIAL  | Первичный ключ         |
| name       | VARCHAR | Имя автора             |
| birth_date | DATE    | Дата рождения автора   |

### Таблица Books

| Колонка    | Тип     | Описание                               |
|------------|---------|----------------------------------------|
| book_id    | SERIAL  | Первичный ключ                         |
| title      | VARCHAR | Название книги                         |
| author_id  | INT     | Внешний ключ, ссылающийся на `Authors` |
| year       | INT     | Год публикации                         |
| genre      | VARCHAR | Жанр книги                             |

### Таблица Readers

| Колонка    | Тип     | Описание                 |
|------------|---------|--------------------------|
| reader_id  | SERIAL  | Первичный ключ           |
| name       | VARCHAR | Имя читателя             |
| contact    | VARCHAR | Контактная информация    |
| reg_date   | DATE    | Дата регистрации         |

### Таблица Issuances

| Колонка      | Тип     | Описание                               |
|--------------|---------|----------------------------------------|
| issuance_id  | SERIAL  | Первичный ключ                         |
| book_id      | INT     | Внешний ключ, ссылающийся на `Books`   |
| reader_id    | INT     | Внешний ключ, ссылающийся на `Readers` |
| issuance_date| DATE    | Дата выдачи                            |
| return_date  | DATE    | Дата возврата                          |

## Установка

1. Установите PostgreSQL и создайте базу данных.
2. Настройте подключение к базе данных в файле `LibraryManager.java`, изменив параметры `url`, `user` и `password` на ваши настройки.
3. Скомпилируйте и запустите приложение.

## Использование

### Создание таблиц

Вызовите метод `createTables()` для создания таблиц в базе данных:

```java
LibraryManager manager = new LibraryManager();
manager.createTables();
```

### Добавление автора

Вызовите метод `addAuthor()` для добавления автора:

```java
manager.addAuthor("Стивен Кинг", Date.valueOf("1965-07-31"));
```

### Добавление книги

Вызовите метод `addBook()` для добавления книги:

```java
manager.addBook("Зелёная миля", 1, 1996, "фантастика");
```

### Добавление читателя

Вызовите метод `addReader()` для добавления читателя:

```java
manager.addReader("Лобода Сотир", "H0bbNT@bk.ru", Date.valueOf("2024-01-01"));
```

### Добавление выдачи

Вызовите метод `addIssuance()` для добавления выдачи книги:

```java
manager.addIssuance(1, 1, Date.valueOf("2024-07-14"), Date.valueOf("2024-07-21"));
```

### Удаление книги

Вызовите метод `deleteBook()` для удаления книги:

```java
manager.deleteBook(1);
```

### Удаление читателя

Вызовите метод `deleteReader()` для удаления читателя:

```java
manager.deleteReader(1);
```

### Отображение всех авторов

Вызовите метод `displayAllAuthors()` для отображения всех авторов:

```java
manager.displayAllAuthors();
```

### Отображение всех книг

Вызовите метод `displayAllBooks()` для отображения всех книг:

```java
manager.displayAllBooks();
```

### Отображение всех читателей

Вызовите метод `displayAllReaders()` для отображения всех читателей:

```java
manager.displayAllReaders();
```

### Отображение всех выдач

Вызовите метод `displayAllIssuances()` для отображения всех выдач:

```java
manager.displayAllIssuances();
```

### Выполнение произвольного SQL-запроса

Вызовите метод `executeSQL()` для выполнения произвольного SQL-запроса:

```java
manager.executeSQL("SELECT * FROM Books WHERE genre = 'фантастика'");
```

## Заключение

Этот проект демонстрирует, как можно разработать систему управления библиотекой с использованием PostgreSQL и Java JDBC. Проект включает основные операции для управления данными и показывает, как обеспечить целостность данных с помощью проверок перед удалением записей.