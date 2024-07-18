package a2_2001040208.window;

import a2_2001040208.common.Genre;
import a2_2001040208.common.Helper;
import a2_2001040208.db.DBManager;
import a2_2001040208.model.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class NewBook extends Window {
    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<Genre> genreField;
    private JTextField pubYearField;
    private JTextField numCopiesField;

    public NewBook(JFrame parentGUI) {
        super(parentGUI);
    }

    @Override
    protected void createGUI() {
        gui = new JFrame("New Book");
        gui.setSize(400, 270);
        gui.addWindowListener(this);
        gui.setLocation((int) parentGUI.getLocation().getX() + 100, (int) parentGUI.getLocation().getY() + 100);

        // extra TODO: banner
        createBanner("Add New Book", Color.YELLOW);

        // TODO: fields
        JPanel input = new JPanel(new GridBagLayout());
        input.setBackground(Color.WHITE);
        input.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);

        // title
        JLabel title = new JLabel("Title");
        titleField = new JTextField();
        setConstraints(gbc, 0, 0, 0.15);
        input.add(title, gbc);
        setConstraints(gbc, 1, 0, 0.85);
        input.add(titleField, gbc);

        // author
        JLabel author = new JLabel("Author");
        authorField = new JTextField();
        setConstraints(gbc, 0, 1, 0.15);
        input.add(author, gbc);
        setConstraints(gbc, 1, 1, 0.85);
        input.add(authorField, gbc);

        // genre
        JLabel genre = new JLabel("Genre");
        genreField = new JComboBox<>(Genre.values());
        genreField.setBackground(Color.WHITE);
        setConstraints(gbc, 0, 2, 0.15);
        input.add(genre, gbc);
        setConstraints(gbc, 1, 2, 0.85);
        input.add(genreField, gbc);

        // publication year
        JLabel pubYear = new JLabel("Publication Year");
        pubYearField = new JTextField();
        pubYearField.addKeyListener(new Helper.NumberFilter());
        setConstraints(gbc, 0, 3, 0.15);
        input.add(pubYear, gbc);
        setConstraints(gbc, 1, 3, 0.85);
        input.add(pubYearField, gbc);

        // number of copies available
        JLabel numCopies = new JLabel("No. Copies Available");
        numCopiesField = new JTextField();
        numCopiesField.addKeyListener(new Helper.NumberFilter());
        setConstraints(gbc, 0, 4, 0.15);
        input.add(numCopies, gbc);
        setConstraints(gbc, 1, 4, 0.85);
        input.add(numCopiesField, gbc);

        JScrollPane scrollableInput = new JScrollPane(input);
        gui.add(scrollableInput, BorderLayout.CENTER);

        // extra TODO: buttons
        JPanel commands = new JPanel();
        JButton addBtn = new JButton("Add book");
        JButton cancelBtn = new JButton("Cancel");
        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        commands.add(addBtn);
        commands.add(cancelBtn);
        gui.add(commands, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Add book":
                try {
                    // TODO: check validity
                    Book book = new Book(
                            titleField.getText(),
                            authorField.getText(),
                            (Genre) genreField.getSelectedItem(),
                            Integer.parseInt(pubYearField.getText()),
                            Integer.parseInt(numCopiesField.getText()));
                    // TODO: add new book
                    dbManager = DBManager.getInstance();
                    if (dbManager.addNewBook(book)) {
                        showSuccessfulMessage("Book added successfully!");
                    } else {
                        showErrorMessage("Failed to add new book to DB!");
                    }
                } catch (NumberFormatException ex) {
                    showErrorMessage("Failed to parse number values!");
                } catch (IllegalArgumentException ex) {
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
