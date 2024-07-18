package a2_2001040208.db;

import a2_2001040208.common.Genre;
import a2_2001040208.common.Helper;
import a2_2001040208.common.PatronType;
import a2_2001040208.model.Book;
import a2_2001040208.model.LibraryTransaction;
import a2_2001040208.model.Patron;
import tutes.meta.NotPossibleException;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class DBManager {
    private static DBManager instance;
    private Connection connection = null;
    private Statement statement = null;

    private DBManager() {
        connectToDB();
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    // TODO: Text -> Date
    public static Date fromTextToDate(String text) throws ParseException {
        return Helper.getDateFormatInstance().parse(text);
    }

    // TODO: Date -> Text
    public static String fromDateToText(Date date) {
        return Helper.getDateFormatInstance().format(date);
    }

    // TODO: connect to database
    private void connectToDB() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:library.db");
            statement = connection.createStatement();
            System.out.println("Connected to DB successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to DB!");
            closeDBConnection();
        }
    }

    // TODO: close connection
    public void closeDBConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("DB connection closed!");
            } catch (SQLException e) {
                System.err.println("Failed to close the DB connection!");
            }
        }
    }

    // TODO: add new patron
    public boolean addNewPatron(Patron patron) {
        if (patron != null) {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO patron(name,dob,email,phone,patronType) VALUES (?,?,?,?,?)");
                ps.setString(1, patron.getName());
                ps.setString(2, fromDateToText(patron.getDob()));
                ps.setString(3, patron.getEmail());
                ps.setString(4, patron.getPhoneNo());
                ps.setString(5, patron.getPatronType().name());
                return ps.executeUpdate() > 0;
                // TODO?: update multiple windows
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    // TODO: add new book
    public boolean addNewBook(Book book) {
        if (book != null) {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO book(ISBN,title,author,genre,pubYear,numCopiesAvailable) VALUES (?,?,?,?,?,?)");
                ps.setString(1, book.getISBN());
                ps.setString(2, book.getTitle());
                ps.setString(3, book.getAuthor());
                ps.setString(4, book.getGenre().name());
                ps.setString(5, String.valueOf(book.getPublicationYear()));
                ps.setInt(6, book.getNumCopiesAvailable());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    // add new transaction
    public boolean addNewTransaction(LibraryTransaction transaction) {
        if (transaction != null) {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO 'transaction'(book_id, patron_id, checkoutDate, dueDate) VALUES (?,?,?,?)");
                ps.setInt(1, getBookId(transaction.getBook()));
                ps.setInt(2, transaction.getPatron().getRawID());
                ps.setString(3, fromDateToText(transaction.getCheckoutDate()));
                ps.setString(4, fromDateToText(transaction.getDueDate()));
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    // TODO: get all patrons (table data)
    public Object[][] getAllPatronsData() {
        Object[][] data = new Object[0][];

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patron");
            ArrayList<Object[]> dataRows = new ArrayList<>();
            while (resultSet.next()) {
                dataRows.add(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("dob"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("patronType")
                });
            }
            data = new Object[dataRows.size()][6];
            data = dataRows.toArray(data);
        } catch (SQLException e) {
            System.err.println("Failed to fetch patrons' data from DB!");
        }
        return data;
    }

    // get all patrons (Patron[])
    public Patron[] getAllPatrons() {
        ArrayList<Patron> patrons = new ArrayList<>();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patron");
            while (resultSet.next()) {
                patrons.add(new Patron(
                        String.valueOf(resultSet.getInt("id")),
                        resultSet.getString("name"),
                        fromTextToDate(resultSet.getString("dob")),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        PatronType.valueOf(resultSet.getString("patronType"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to fetch patron list from DB!");
        } catch (ParseException e) {
            System.err.println("Failed to parse date of birth!");
        }
        return patrons.toArray(new Patron[0]);
    }

    // TODO: get all books (table data)
    public Object[][] getAllBooksData() {
        Object[][] data = new Object[0][];

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
            ArrayList<Object[]> dataRows = new ArrayList<>();
            while (resultSet.next()) {
                dataRows.add(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("ISBN"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getString("pubYear"),
                        resultSet.getInt("numCopiesAvailable")
                });
            }
            data = new Object[dataRows.size()][7];
            data = dataRows.toArray(data);
        } catch (SQLException e) {
            System.err.println("Failed to fetch books' data from DB!");
        }
        return data;
    }

    // get all books (Book[])
    public Book[] getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getString("ISBN"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        Genre.valueOf(resultSet.getString("genre")),
                        Integer.parseInt(resultSet.getString("pubYear")),
                        resultSet.getInt("numCopiesAvailable")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch book list from DB!");
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse publication year!");
        }
        return books.toArray(new Book[0]);
    }

    // TODO: get all transactions (table data)
    public Object[][] getAllTransactionsData() {
        Object[][] data = new Object[0][];

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM 'transaction'");
            ArrayList<Object[]> dataRows = new ArrayList<>();
            while (resultSet.next()) {
                dataRows.add(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getInt("book_id"),
                        resultSet.getInt("patron_id"),
                        resultSet.getString("checkoutDate"),
                        resultSet.getString("dueDate")
                });
            }
            data = new Object[dataRows.size()][5];
            data = dataRows.toArray(data);
        } catch (SQLException e) {
            System.err.println("Failed to fetch all transactions' data from DB!");
        }
        return data;
    }

    // TODO: get all checked-out books (table data)
    public Object[][] getAllCheckedOutBooksData() {
        Object[][] data = new Object[0][];

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM 'transaction'");
            ArrayList<Object[]> dataRows = new ArrayList<>();
            while (resultSet.next()) {
                dataRows.add(new Object[]{
                        getBookBy(resultSet.getInt("book_id")).getTitle(),
                        getBookBy(resultSet.getInt("book_id")).getISBN(),
                        resultSet.getInt("patron_id"),
                        resultSet.getString("checkoutDate"),
                        resultSet.getString("dueDate")
                });
            }
            data = new Object[dataRows.size()][5];
            data = dataRows.toArray(data);
        } catch (SQLException e) {
            System.err.println("Failed to fetch all checked-out books' data from DB!");
        }
        return data;
    }

    // TODO: get checked-out books of a patron (table data - certain data)
    public Object[][] getCheckedOutBooksBy(Patron patron) {
        if (patron != null) {
            Object[][] data = new Object[0][];
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM 'transaction' WHERE patron_id = ?");
                ps.setInt(1, patron.getRawID());
                ResultSet resultSet = ps.executeQuery();
                ArrayList<Object[]> dataRows = new ArrayList<>();
                while (resultSet.next()) {
                    dataRows.add(new Object[]{
                            getBookBy(resultSet.getInt("book_id")).getTitle(),
                            getBookBy(resultSet.getInt("book_id")).getISBN(),
                            resultSet.getString("checkoutDate"),
                            resultSet.getString("dueDate")
                    });
                }
                data = new Object[dataRows.size()][4];
                data = dataRows.toArray(data);
            } catch (SQLException e) {
                System.err.println("Failed to fetch patron's transactions data from DB!");
            }
            return data;
        }
        return new Object[0][];
    }

    // get book id by book's information

    // get transactions of a patron (LibraryTransaction[])
    public LibraryTransaction[] getTransactionsBy(Patron patron) {
        if (patron != null) {
            ArrayList<LibraryTransaction> transactions = new ArrayList<>();
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM 'transaction' WHERE patron_id = ?");
                ps.setInt(1, patron.getRawID());
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    transactions.add(new LibraryTransaction(
                            getPatronBy(resultSet.getInt("patron_id")),
                            getBookBy(resultSet.getInt("book_id")),
                            fromTextToDate(resultSet.getString("checkoutDate")),
                            fromTextToDate(resultSet.getString("dueDate")),
                            null
                    ));
                }
            } catch (SQLException e) {
                System.err.println("Failed to fetch patron's transactions from DB!");
            } catch (ParseException e) {
                System.err.println("Failed to parse date-time data of patron's transactions!");
            }
            return transactions.toArray(new LibraryTransaction[0]);
        }
        return new LibraryTransaction[0];
    }

    // get book by book id
    public Book getBookBy(int id) {
        Book book = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM book WHERE id = ?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                book = new Book(
                        resultSet.getString("ISBN"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        Genre.valueOf(resultSet.getString("genre")),
                        Integer.parseInt(resultSet.getString("pubYear")),
                        resultSet.getInt("numCopiesAvailable")
                );
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch book by id from DB!");
        }
        return book;
    }

    public int getBookId(Book book) {
        if (book != null) {
            int id = 0;
            try {
                PreparedStatement ps = connection.prepareStatement("SELECT id FROM book WHERE ISBN = ? AND title = ? AND numCopiesAvailable = ?");
                ps.setString(1, book.getISBN());
                ps.setString(2, book.getTitle());
                ps.setInt(3, book.getNumCopiesAvailable());
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            } catch (SQLException e) {
                System.err.println("Failed to fetch book's id from DB!");
            }
            return id;
        }
        return 0;
    }

    // get patron by patron id
    public Patron getPatronBy(int id) {
        Patron patron = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM patron WHERE id = ?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                patron = new Patron(
                        String.valueOf(resultSet.getInt("id")),
                        resultSet.getString("name"),
                        fromTextToDate(resultSet.getString("dob")),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        PatronType.valueOf(resultSet.getString("patronType"))
                );
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch patron by id from DB!");
        } catch (ParseException e) {
            System.err.println("Failed to parse date of birth when fetching patron by id from DB!");
        }
        return patron;
    }

    // update number of copies available
    public void updateNumCopiesAvailable(Book book, int newNumber) {
        if (book != null && newNumber >= 0) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE book SET numCopiesAvailable = ? WHERE id = ?");
                ps.setInt(1, newNumber);
                ps.setInt(2, getBookId(book));
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Failed to update number of copies available!");
            }
        }
    }

    // TODO: check out book
    public void checkoutBook(Patron patron, Book book, Date checkoutDate, Date dueDate) {
        LibraryTransaction[] checkedOutBooks = getTransactionsBy(patron);
        if ((patron.getPatronType() == PatronType.REGULAR && checkedOutBooks.length < 3)
                || (patron.getPatronType() == PatronType.PREMIUM && checkedOutBooks.length < 5)) {
            if (book.getNumCopiesAvailable() > 0) {
                // check validity
                LibraryTransaction newTransaction = new LibraryTransaction(patron, book, checkoutDate, dueDate, null);
                // add transaction and update number of copies
                if (addNewTransaction(newTransaction)) {
                    updateNumCopiesAvailable(book, book.getNumCopiesAvailable() - 1);
                    return;
                }
                throw new NotPossibleException("Failed to check out book!");
            }
            throw new NotPossibleException("Book is out of stock!");
        }
        throw new NotPossibleException("Patron has reached the checkout limit!");
    }

    // TODO: return book
    public void returnBook(LibraryTransaction transaction) {
        if (transaction != null) {
            try {
                PreparedStatement ps = connection.prepareStatement("DELETE FROM 'transaction' WHERE book_id = ? AND patron_id = ? AND checkOutDate = ? AND dueDate = ?");
                ps.setInt(1, getBookId(transaction.getBook()));
                ps.setInt(2, transaction.getPatron().getRawID());
                ps.setString(3, fromDateToText(transaction.getCheckoutDate()));
                ps.setString(4, fromDateToText(transaction.getDueDate()));
                if (ps.executeUpdate() == 0) {
                    throw new NotPossibleException("Failed to delete transaction!");
                } else {
                    updateNumCopiesAvailable(transaction.getBook(), transaction.getBook().getNumCopiesAvailable() + 1);
                }
            } catch (SQLException e) {
                throw new NotPossibleException("Failed to return book!");
            }
        }
    }
}
