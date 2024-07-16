import java.sql.Date;

public static void main(String[] args) {
    LibraryManager manager = new LibraryManager("Library", "postgres", "postgres");
    manager.createTables();

    System.out.println("1");
    manager.addAuthor("Стивен Кинг", Date.valueOf("1965-07-31"));
    manager.addAuthor("Дэн Пинчбек", Date.valueOf("1948-09-20"));
    System.out.println("2");
    manager.addBook("Зелёная миля", 1, 1996, "фантастика");
    manager.addBook("DOOM. Как в битвах с демонами закалялся новый жанр", 2, 2013, "истории успеха");
    System.out.println("3");
    manager.addReader("Лобода Сотир", "H0bbNT@bk.ru", Date.valueOf("2024-01-01"));
    manager.addReader("Брезгун Варфоломей", "ilovedota228@hotmail.com", Date.valueOf("2024-02-01"));
    System.out.println("4");
    manager.addIssuance(1, 1, Date.valueOf("2024-07-14"), Date.valueOf("2024-07-21"));
    manager.addIssuance(2, 2, Date.valueOf("2024-07-14"), Date.valueOf("2024-07-21"));
    System.out.println("5");
    manager.displayAllAuthors();
    System.out.println("6");
    manager.displayAllBooks();
    System.out.println("7");
    manager.displayAllReaders();
    System.out.println("8");
    manager.displayAllIssuances();
    System.out.println("9");
    manager.executeSQL("SELECT b.title, a.name AS author, r.name AS reader, i.issuance_date, i.return_date " +
            "FROM Issuances i " +
            "JOIN Books b ON i.book_id = b.book_id " +
            "JOIN Authors a ON b.author_id = a.author_id " +
            "JOIN Readers r ON i.reader_id = r.reader_id " +
            "WHERE r.name = 'Лобода Сотир'");
    System.out.println("10");
    manager.updateBook(1, "Зелёная миля", 1, 1996, "ЯПлакал");
    System.out.println("11");
    manager.displayBook(1);
    System.out.println("12");
    manager.deleteIssuance(2);
    manager.deleteReader(2);
    System.out.println("13");
    manager.displayAllReaders();
}

