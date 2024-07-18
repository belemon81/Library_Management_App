package a2_2001040208.window;

import a2_2001040208.common.Helper;
import a2_2001040208.common.PatronType;
import a2_2001040208.db.DBManager;
import a2_2001040208.model.Patron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.text.ParseException;

public class NewPatron extends Window {
    private JTextField nameField;
    private JFormattedTextField dobField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<PatronType> patronTypeField;

    public NewPatron(JFrame parentGUI) {
        super(parentGUI);
    }

    @Override
    protected void createGUI() {
        gui = new JFrame("New Patron");
        gui.setSize(400, 270);
        gui.addWindowListener(this);
        gui.setLocation((int) parentGUI.getLocation().getX() + 100, (int) parentGUI.getLocation().getY() + 100);

        // extra TODO: banner
        createBanner("Add New Patron", Color.PINK);

        // TODO: fields
        JPanel input = new JPanel(new GridBagLayout());
        input.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        input.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);

        // name
        JLabel name = new JLabel("Name");
        nameField = new JTextField();
        setConstraints(gbc, 0, 0, 0.2);
        input.add(name, gbc);
        setConstraints(gbc, 1, 0, 0.8);
        input.add(nameField, gbc);

        // dob
        JLabel dob = new JLabel("Date of Birth");
        if (Helper.getDateFormatterInstance() != null) {
            dobField = new JFormattedTextField(Helper.getDateFormatterInstance());
        } else {
            dobField = new JFormattedTextField();
        }
        dobField.setColumns(10);
        setConstraints(gbc, 0, 1, 0.2);
        input.add(dob, gbc);
        setConstraints(gbc, 1, 1, 0.8);
        input.add(dobField, gbc);

        // email
        JLabel email = new JLabel("Email");
        emailField = new JTextField();
        setConstraints(gbc, 0, 2, 0.2);
        input.add(email, gbc);
        setConstraints(gbc, 1, 2, 0.8);
        input.add(emailField, gbc);

        // phone
        JLabel phone = new JLabel("Phone");
        phoneField = new JTextField();
        setConstraints(gbc, 0, 3, 0.2);
        input.add(phone, gbc);
        setConstraints(gbc, 1, 3, 0.8);
        input.add(phoneField, gbc);

        // patron type
        JLabel patronType = new JLabel("Patron Type");
        patronTypeField = new JComboBox<>(PatronType.values());
        patronTypeField.setBackground(Color.WHITE);
        setConstraints(gbc, 0, 4, 0.2);
        input.add(patronType, gbc);
        setConstraints(gbc, 1, 4, 0.8);
        input.add(patronTypeField, gbc);

        JScrollPane scrollableInput = new JScrollPane(input);
        gui.add(scrollableInput, BorderLayout.CENTER);

        // extra TODO: buttons
        JPanel commands = new JPanel();
        JButton addBtn = new JButton("Add patron");
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
            case "Add patron":
                try {
                    // TODO: check validity
                    Patron patron = new Patron(
                            nameField.getText(),
                            DBManager.fromTextToDate(dobField.getText()),
                            emailField.getText(),
                            phoneField.getText(),
                            (PatronType) patronTypeField.getSelectedItem());
                    // TODO: add new patron
                    dbManager = DBManager.getInstance();
                    if (dbManager.addNewPatron(patron)) {
                        showSuccessfulMessage("Patron added successfully!");
                    } else {
                        showErrorMessage("Failed to add new patron to DB!");
                    }
                } catch (IllegalArgumentException ex) {
                    showErrorMessage(ex.getMessage());
                } catch (ParseException ex) {
                    showErrorMessage("Failed to parse date-time data!");
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
