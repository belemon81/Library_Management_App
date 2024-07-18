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
import java.util.Date;

public class CheckoutBook extends Window {
    private JComboBox<Patron> patronField;
    private JComboBox<Book> bookField;
    private JFormattedTextField dueDateField;
    private JLabel checkoutDateLabel;

    public CheckoutBook(JFrame parentGUI) {
        super(parentGUI);
    }

    @Override
    protected void createGUI() {
        gui = new JFrame("Checkout Book");
        gui.setSize(550, 270);
        gui.addWindowListener(this);
        gui.setLocation((int) parentGUI.getLocation().getX() + 100, (int) parentGUI.getLocation().getY() + 100);

        // extra TODO: banner
        createBanner("Checkout Book", Color.CYAN);

        // TODO: fields
        JPanel input = new JPanel(new GridBagLayout());
        input.setBackground(Color.WHITE);
        input.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dbManager = DBManager.getInstance();

        // patron
        JLabel patron = new JLabel("Patron");
        patronField = new JComboBox<>(dbManager.getAllPatrons());
        setConstraints(gbc, 0, 0, 0.15);
        input.add(patron, gbc);
        setConstraints(gbc, 1, 0, 0.85);
        input.add(patronField, gbc);

        // book
        JLabel book = new JLabel("Book");
        bookField = new JComboBox<>(dbManager.getAllBooks());
        setConstraints(gbc, 0, 1, 0.15);
        input.add(book, gbc);
        setConstraints(gbc, 1, 1, 0.85);
        input.add(bookField, gbc);

        // checkout date
        JLabel checkoutDate = new JLabel("Checkout Date");
        String systemDate = DBManager.fromDateToText(new Date());
        checkoutDateLabel = new JLabel(systemDate);
        checkoutDateLabel.setBorder(BorderFactory.createEtchedBorder());
        setConstraints(gbc, 0, 2, 0.2);
        input.add(checkoutDate, gbc);
        setConstraints(gbc, 1, 2, 0.8);
        input.add(checkoutDateLabel, gbc);

        // due date
        JLabel dueDate = new JLabel("Due Date");
        if (Helper.getDateFormatterInstance() != null) {
            dueDateField = new JFormattedTextField(Helper.getDateFormatterInstance());
        } else {
            dueDateField = new JFormattedTextField();
        }
        dueDateField.setColumns(10);
        setConstraints(gbc, 0, 3, 0.2);
        input.add(dueDate, gbc);
        setConstraints(gbc, 1, 3, 0.8);
        input.add(dueDateField, gbc);

        JScrollPane scrollableInput = new JScrollPane(input);
        gui.add(scrollableInput, BorderLayout.CENTER);

        // TODO: buttons
        JPanel commands = new JPanel();
        JButton checkoutBtn = new JButton("Checkout book");
        JButton cancelBtn = new JButton("Cancel");
        checkoutBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        commands.add(checkoutBtn);
        commands.add(cancelBtn);
        gui.add(commands, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Checkout book":
                try {
                    // TODO: check validity
                    Patron patron = (Patron) patronField.getSelectedItem();
                    Book book = (Book) bookField.getSelectedItem();
                    Date checkoutDate = DBManager.fromTextToDate(checkoutDateLabel.getText());
                    Date dueDate = DBManager.fromTextToDate(dueDateField.getText());
                    LibraryTransaction libraryTransaction = new LibraryTransaction(patron, book, checkoutDate, dueDate, null);
                    // TODO: check out
                    dbManager.checkoutBook(patron, book, checkoutDate, dueDate);
                    // TODO: confirm checkout
                    int confirm = JOptionPane.showConfirmDialog(gui,
                            "Confirm this transaction?\n" + libraryTransaction.getDescription(),
                            "Checkout Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        showSuccessfulMessage("Book checked out successfully!");
                    }
                } catch (ParseException ex) {
                    showErrorMessage("Failed to parse date-time data!");
                } catch (IllegalArgumentException | NotPossibleException ex) {
                    showErrorMessage(ex.getMessage());
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
