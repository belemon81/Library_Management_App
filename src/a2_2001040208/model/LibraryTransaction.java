package a2_2001040208.model;

import a2_2001040208.db.DBManager;
import tutes.meta.NotPossibleException;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class LibraryTransaction {
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.##");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("E, MMM dd yyyy"); // Tue, Apr 25 2023
    private final Patron patron;
    private final Book book;
    private final Date checkoutDate;
    private final Date dueDate;
    private final double fine;
    private Date returnDate;

    public LibraryTransaction(Patron patron, Book book, Date checkoutDate, Date dueDate, Date returnDate) {
        if (!validatePatron(patron)) {
            throw new IllegalArgumentException("LibraryTransaction.init: Invalid patron!");
        } else if (!validateBook(book)) {
            throw new IllegalArgumentException("LibraryTransaction.init: Invalid book!");
        } else if (!validateCheckoutDate(checkoutDate)) {
            throw new IllegalArgumentException("LibraryTransaction.init: Invalid check out date!");
        } else if (!validateDueDate(dueDate)) {
            throw new IllegalArgumentException("LibraryTransaction.init: Invalid due date!");
        } else if (!validateDuration(checkoutDate, dueDate, returnDate)) {
            throw new IllegalArgumentException("LibraryTransaction.init: Invalid date duration!");
        } else {
            this.patron = patron;
            this.book = book;
            this.checkoutDate = checkoutDate;
            this.dueDate = dueDate;
            this.returnDate = returnDate;

            this.fine = calculateFine();
        }
    }

    // TODO 2: add all missing getters

    private static String getFormattedDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).format(DATE_FORMATTER);
    }

    public Patron getPatron() {
        return patron;
    }

    public Book getBook() {
        return book;
    }

    public Date getDueDate() {
        return dueDate;
    }

    // TODO 2: update setReturnDate(Date returnDate) {
    public void setReturnDate(Date returnDate) {
        if (validateDuration(checkoutDate, dueDate, returnDate)) {
            this.returnDate = returnDate;
        } else {
            throw new NotPossibleException("Invalid return date! (Checkout date: " + DBManager.fromDateToText(checkoutDate) + ")");
        }
    }

    // remove setFine(double fine)

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    private boolean validatePatron(Patron patron) {
        return patron != null;
    }

    private boolean validateBook(Book book) {
        return book != null;
    }

    private boolean validateDueDate(Date dueDate) {
        return dueDate != null;
    }

    private boolean validateCheckoutDate(Date checkoutDate) {
        return checkoutDate != null;
    }

    private boolean validateDuration(Date checkoutDate, Date dueDate, Date returnDate) {
        // due date should be later than or equal to check out date
        // TODO 2: make some reasonable change with Date.before()
        if (dueDate.before(checkoutDate)) return false;
        if (returnDate != null) {
            // return date should be later than or equal to check out date
            // TODO 2: fix 1 error of date validation
            return !returnDate.before(checkoutDate);
        } else {
            return true;
        }
    }

    // TODO: calculateFine()
    //      Calculate the fine amount based on the difference between the return date and due date
    //          $1.00 per day for books overdue by 1 to 7 days
    //          $2.00 per day for books overdue by 8 to 14 days
    //          $3.00 per day for books overdue by more than 14 days
    public double calculateFine() {
        // calculate if return date available
        if (returnDate != null) {
            return getFine(returnDate);
        } else {
            // continue calculating fine to the current day
            // TODO 2: use current Date
            return getFine(new Date());
        }
    }

    private double getFine(Date returnDate) {
        // get days between
        long overdue = ChronoUnit.DAYS.between(dueDate.toInstant(), returnDate.toInstant());
        // calculate fine
        if (overdue <= 0) {
            return 0;
        } else if (overdue <= 7) {
            return 1D * overdue;
        } else if (overdue <= 14) {
            return 2D * overdue;
        } else {
            return 3D * overdue;
        }
    }

    // TODO: getDescription()
    //      Include return date (if available), and fine amount (if applicable).
    //      e.g. Transaction Details:
    //               Patron ID: P001
    //               Book ISBN: HL-01-1960
    //               Checkout Date: Sat, Mar 25 2023
    //               Due Date: Tue, Apr 25 2023
    //               Return Date: Sat, May 27 2023
    //               Fine Amount: $96.00
    public String getDescription() {
        return "Transaction Details:" +
                "\n    Patron ID: " + patron.getPatronID() +
                "\n    Book ISBN: " + book.getISBN() +
                "\n    Checkout Date: " + getFormattedDate(checkoutDate) +
                "\n    Due Date: " + getFormattedDate(dueDate) +
                (returnDate != null ? "\n    Return Date: " + getFormattedDate(returnDate) : "") +
                (fine != 0 ? "\n    Fine Amount: $" + DECIMAL_FORMATTER.format(fine) : "");
    }

}
