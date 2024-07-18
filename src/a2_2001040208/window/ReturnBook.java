package a2_2001040208.window;

import a2_2001040208.common.Helper;
import a2_2001040208.db.DBManager;
import a2_2001040208.model.Book;
import a2_2001040208.model.LibraryTransaction;
import a2_2001040208.model.Patron;
import tutes.meta.NotPossibleException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.text.ParseException;

public class ReturnBook extends Window {
    private JComboBox<Object> patronField;
    private JComboBox<Book> bookField;
    private JFormattedTextField returnDateField;

    public ReturnBook(JFrame parentGUI) {
        super(parentGUI);
    }

    @Override
    protected void createGUI() {
        gui = new JFrame("Return Book");
        gui.setSize(550, 240);
        gui.addWindowListener(this);
        gui.setLocation((int) parentGUI.getLocation().getX() + 100, (int) parentGUI.getLocation().getY() + 100);

        dbManager = DBManager.getInstance();

        // TODO: banner
        createBanner("Return Book", Color.CYAN);

        // TODO: fields
        JPanel input = new JPanel(new GridBagLayout());
        input.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        input.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // patron
        JLabel patron = new JLabel("Patron");
        patronField = new JComboBox<>(dbManager.getAllPatrons());
        patronField.setSelectedIndex(-1);
        setConstraints(gbc, 0, 0, 0.15);
        input.add(patron, gbc);
        setConstraints(gbc, 1, 0, 0.85);
        input.add(patronField, gbc);

        // book
        JLabel book = new JLabel("Book");
        bookField = new JComboBox<>();
        setConstraints(gbc, 0, 1, 0.15);
        input.add(book, gbc);
        setConstraints(gbc, 1, 1, 0.85);
        input.add(bookField, gbc);

        // return date
        JLabel returnDate = new JLabel("Return Date");
        if (Helper.getDateFormatterInstance() != null) {
            returnDateField = new JFormattedTextField(Helper.getDateFormatterInstance());
        } else {
            returnDateField = new JFormattedTextField();
        }
        returnDateField.setColumns(10);
        setConstraints(gbc, 0, 3, 0.15);
        input.add(returnDate, gbc);
        setConstraints(gbc, 1, 3, 0.85);
        input.add(returnDateField, gbc);

        JScrollPane scrollableInput = new JScrollPane(input);
        gui.add(scrollableInput, BorderLayout.CENTER);

        // TODO: buttons
        JPanel commands = new JPanel();
        JButton returnBtn = new JButton("Return book");
        JButton cancelBtn = new JButton("Cancel");
        returnBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        commands.add(returnBtn);
        commands.add(cancelBtn);
        gui.add(commands, BorderLayout.SOUTH);

        // TODO: load books checked out according to patron
        patronField.addActionListener(e -> {
            bookField.removeAllItems();
            Patron selectedPatron = (Patron) patronField.getSelectedItem();
            for (LibraryTransaction transaction : dbManager.getTransactionsBy(selectedPatron)) {
                bookField.addItem(transaction.getBook());
            }
            gui.validate();
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Return book":
                try {
                    Patron patron = (Patron) patronField.getSelectedItem();
                    Book book = (Book) bookField.getSelectedItem();
                    LibraryTransaction[] libraryTransactions = dbManager.getTransactionsBy(patron);
                    if (libraryTransactions.length != 0) {
                        for (LibraryTransaction libraryTransaction : libraryTransactions) {
                            if (libraryTransaction.getBook().equals(book)) {
                                libraryTransaction.setReturnDate(DBManager.fromTextToDate(returnDateField.getText())); // catch exception if finding invalid return date
                                int confirm = JOptionPane.showConfirmDialog(gui,
                                        (libraryTransaction.calculateFine() != 0 ? "The book should be returned along with a fine of $" + libraryTransaction.calculateFine() + "\n" : "") + "Confirm to return book?",
                                        "Return Book Confirmation", JOptionPane.YES_NO_OPTION);
                                if (confirm == JOptionPane.YES_OPTION) {
                                    dbManager.returnBook(libraryTransaction); // return date not important now
                                    showSuccessfulMessage("Book returned successfully!");
                                }
                                break;
                            }
                        }
                    } else {
                        showErrorMessage("No transactions found!");
                    }
                } catch (IllegalArgumentException | NotPossibleException ex) {
                    showErrorMessage(ex.getMessage());
                } catch (ParseException ex) {
                    showErrorMessage("Failed to parse return date!");
                }
                break;
            case "Cancel":
                disposeGUI();
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        disposeGUI();
    }
}
