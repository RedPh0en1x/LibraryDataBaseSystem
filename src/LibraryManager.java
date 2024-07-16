import java.sql.*;

public class LibraryManager {

    private String url = "jdbc:postgresql://localhost/";
    private String user;
    private String password;

    public LibraryManager(String dbName, String User, String Password) {
        this.url += dbName;
        this.user = User;
        this.password = Password;

    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void createTables() {
        String createAuthors = "CREATE TABLE IF NOT EXISTS Authors ("
                + "author_id SERIAL PRIMARY KEY,"
                + "name VARCHAR(255) NOT NULL,"
                + "birth_date DATE"
                + ");";

        String createBooks = "CREATE TABLE IF NOT EXISTS Books ("
                + "book_id SERIAL PRIMARY KEY,"
                + "title VARCHAR(255) NOT NULL,"
                + "author_id INT REFERENCES Authors(author_id),"
                + "year INT,"
                + "genre VARCHAR(100)"
                + ");";

        String createReaders = "CREATE TABLE IF NOT EXISTS Readers ("
                + "reader_id SERIAL PRIMARY KEY,"
                + "name VARCHAR(255) NOT NULL,"
                + "contact VARCHAR(255),"
                + "registration_date DATE"
                + ");";

        String createIssuances = "CREATE TABLE IF NOT EXISTS Issuances ("
                + "issuance_id SERIAL PRIMARY KEY,"
                + "book_id INT REFERENCES Books(book_id),"
                + "reader_id INT REFERENCES Readers(reader_id),"
                + "issuance_date DATE NOT NULL,"
                + "return_date DATE"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createAuthors);
            stmt.execute(createBooks);
            stmt.execute(createReaders);
            stmt.execute(createIssuances);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Методы CRUD для авторов
    public void addAuthor(String name, Date birthDate) {
        String SQL = "INSERT INTO Authors (name, birth_date) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, birthDate);
            pstmt.executeUpdate();
            System.out.println("Автор добавлен.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateAuthor(int authorId, String name, Date birthDate) {
        String SQL = "UPDATE Authors SET name = ?, birth_date = ? WHERE author_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            pstmt.setDate(2, birthDate);
            pstmt.setInt(3, authorId);
            pstmt.executeUpdate();
            System.out.println("Автор обновлен.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAuthor(int authorId) {
        String SQL = "DELETE FROM Authors WHERE author_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, authorId);
            pstmt.executeUpdate();
            System.out.println("Автор удален.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayAuthor(int authorId) {
        String SQL = "SELECT * FROM Authors WHERE author_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, authorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Author ID: " + rs.getInt("author_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Birth Date: " + rs.getDate("birth_date"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayAllAuthors() {
        String SQL = "SELECT * FROM Authors";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                System.out.println("Author ID: " + rs.getInt("author_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Birth Date: " + rs.getDate("birth_date"));
                System.out.println("-----------------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Методы CRUD для книг
    public void addBook(String title, int authorId, int year, String genre) {
        String SQL = "INSERT INTO Books (title, author_id, year, genre) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, authorId);
            pstmt.setInt(3, year);
            pstmt.setString(4, genre);
            pstmt.executeUpdate();
            System.out.println("Книга добавлена.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateBook(int bookId, String title, int authorId, int year, String genre) {
        String SQL = "UPDATE Books SET title = ?, author_id = ?, year = ?, genre = ? WHERE book_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, title);
            pstmt.setInt(2, authorId);
            pstmt.setInt(3, year);
            pstmt.setString(4, genre);
            pstmt.setInt(5, bookId);
            pstmt.executeUpdate();
            System.out.println("Книга обновлена.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteBook(int bookId) {
        String checkIssuancesSQL = "SELECT COUNT(*) AS issuance_count FROM Issuances WHERE book_id = ?";
        String deleteBookSQL = "DELETE FROM Books WHERE book_id = ?";
        try (Connection conn = connect();
             PreparedStatement checkPstmt = conn.prepareStatement(checkIssuancesSQL);
             PreparedStatement deletePstmt = conn.prepareStatement(deleteBookSQL)) {

            // Проверка, есть ли у книги выдачи
            checkPstmt.setInt(1, bookId);
            ResultSet rs = checkPstmt.executeQuery();
            if (rs.next() && rs.getInt("issuance_count") > 0) {
                System.out.println("Невозможно удалить книгу, поскольку у нее есть активные выдачи.");
            } else {
                // Proceed with the deletion
                deletePstmt.setInt(1, bookId);
                int affectedRows = deletePstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Книга удалена.");
                } else {
                    System.out.println("Книга не найдена.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayBook(int bookId) {
        String SQL = "SELECT * FROM Books WHERE book_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("book_id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Author ID: " + rs.getInt("author_id"));
                System.out.println("Year: " + rs.getInt("year"));
                System.out.println("Genre: " + rs.getString("genre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayAllBooks() {
        String SQL = "SELECT * FROM Books";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("book_id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Author ID: " + rs.getInt("author_id"));
                System.out.println("Year: " + rs.getInt("year"));
                System.out.println("Genre: " + rs.getString("genre"));
                System.out.println("-----------------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Методы CRUD для читателей
    public void addReader(String name, String contact, Date registrationDate) {
        String SQL = "INSERT INTO Readers (name, contact, registration_date) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, contact);
            pstmt.setDate(3, registrationDate);
            pstmt.executeUpdate();
            System.out.println("Читатель добавлен.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateReader(int readerId, String name, String contact, Date registrationDate) {
        String SQL = "UPDATE Readers SET name = ?, contact = ?, registration_date = ? WHERE reader_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, contact);
            pstmt.setDate(3, registrationDate);
            pstmt.setInt(4, readerId);
            pstmt.executeUpdate();
            System.out.println("Читатель обновлен.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteReader(int readerId) {
        String checkIssuancesSQL = "SELECT COUNT(*) AS issuance_count FROM Issuances WHERE reader_id = ?";
        String deleteReaderSQL = "DELETE FROM Readers WHERE reader_id = ?";
        try (Connection conn = connect();
             PreparedStatement checkPstmt = conn.prepareStatement(checkIssuancesSQL);
             PreparedStatement deletePstmt = conn.prepareStatement(deleteReaderSQL)) {

            // Проверьте, есть ли у читателя книги
            checkPstmt.setInt(1, readerId);
            ResultSet rs = checkPstmt.executeQuery();
            if (rs.next() && rs.getInt("issuance_count") > 0) {
                System.out.println("Невозможно удалить читателя, поскольку у него есть активные выдачи.");
            } else {
                // Удаление
                deletePstmt.setInt(1, readerId);
                int affectedRows = deletePstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Читатель удален.");
                } else {
                    System.out.println("Читатель не найден.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayReader(int readerId) {
        String SQL = "SELECT * FROM Readers WHERE reader_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, readerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Reader ID: " + rs.getInt("reader_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Contact: " + rs.getString("contact"));
                System.out.println("Registration Date: " + rs.getDate("registration_date"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayAllReaders() {
        String SQL = "SELECT * FROM Readers";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                System.out.println("Reader ID: " + rs.getInt("reader_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Contact: " + rs.getString("contact"));
                System.out.println("Registration Date: " + rs.getDate("registration_date"));
                System.out.println("-----------------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Методы CRUD для выдачи
    public void addIssuance(int bookId, int readerId, Date issuanceDate, Date returnDate) {
        String SQL = "INSERT INTO Issuances (book_id, reader_id, issuance_date, return_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, readerId);
            pstmt.setDate(3, issuanceDate);
            pstmt.setDate(4, returnDate);
            pstmt.executeUpdate();
            System.out.println("Выдача добавлена.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateIssuance(int issuanceId, int bookId, int readerId, Date issuanceDate, Date returnDate) {
        String SQL = "UPDATE Issuances SET book_id = ?, reader_id = ?, issuance_date = ?, return_date = ? WHERE issuance_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, readerId);
            pstmt.setDate(3, issuanceDate);
            pstmt.setDate(4, returnDate);
            pstmt.setInt(5, issuanceId);
            pstmt.executeUpdate();
            System.out.println("Выдача обновлена.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteIssuance(int issuanceId) {
        String SQL = "DELETE FROM Issuances WHERE issuance_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, issuanceId);
            pstmt.executeUpdate();
            System.out.println("Выдача удалена.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayIssuance(int issuanceId) {
        String SQL = "SELECT * FROM Issuances WHERE issuance_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, issuanceId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Issuance ID: " + rs.getInt("issuance_id"));
                System.out.println("Book ID: " + rs.getInt("book_id"));
                System.out.println("Reader ID: " + rs.getInt("reader_id"));
                System.out.println("Issuance Date: " + rs.getDate("issuance_date"));
                System.out.println("Return Date: " + rs.getDate("return_date"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void displayAllIssuances() {
        String SQL = "SELECT * FROM Issuances";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                System.out.println("Issuance ID: " + rs.getInt("issuance_id"));
                System.out.println("Book ID: " + rs.getInt("book_id"));
                System.out.println("Reader ID: " + rs.getInt("reader_id"));
                System.out.println("Issuance Date: " + rs.getDate("issuance_date"));
                System.out.println("Return Date: " + rs.getDate("return_date"));
                System.out.println("-----------------------");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Метод выполнения произвольных команд SQL
    public void executeSQL(String sql) {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            String trimmedSQL = sql.trim().toLowerCase();
            if (trimmedSQL.startsWith("select")) {
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        int columnCount = rs.getMetaData().getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(rs.getString(i) + "\t");
                        }
                        System.out.println();
                    }
                }
            } else {
                int result = stmt.executeUpdate(sql);
                System.out.println("Команда выволнена: " + result);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
