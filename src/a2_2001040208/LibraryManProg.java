package a2_2001040208;

import a2_2001040208.window.Window;
import a2_2001040208.window.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class LibraryManProg extends Window {
    public LibraryManProg() {
        super();
    }

    public static void main(String[] args) {
        new LibraryManProg();
    }

    @Override
    protected void createGUI() {
        // empty frame
        gui = new JFrame("Library Management System");
        gui.setSize(400, 350);
        gui.addWindowListener(this);
        gui.setLocationRelativeTo(null);

        // TODO: menu bar
        JMenuBar menuBar = new JMenuBar();
        // file menu
        menuBar.add(new JMenu("File"))
                .add(new JMenuItem("Exit")).addActionListener(this);
        // patron menu
        JMenu patronMenu = new JMenu("Patron");
        patronMenu.add(new JMenuItem("New patron")).addActionListener(this);
        patronMenu.add(new JMenuItem("List patrons")).addActionListener(this);
        menuBar.add(patronMenu);
        // book menu
        JMenu bookMenu = new JMenu("Book");
        bookMenu.add(new JMenuItem("New book")).addActionListener(this);
        bookMenu.add(new JMenuItem("List books")).addActionListener(this);
        menuBar.add(bookMenu);
        // transaction menu
        JMenu transactionMenu = new JMenu("Transaction");
        transactionMenu.add(new JMenuItem("Checkout book")).addActionListener(this);
        transactionMenu.add(new JMenuItem("Transaction report")).addActionListener(this);
        transactionMenu.add(new JMenuItem("Return book")).addActionListener(this);
        menuBar.add(transactionMenu);
        // set menu to gui
        gui.setJMenuBar(menuBar);

        // TODO: body
        createBanner("Library Management System", Color.GREEN);

        JTextArea copyright = new JTextArea("Copyright © 2023 by Đặng Quỳnh Trang\n" +
                "\n" +
                "This program was developed as the submission for assignment 2 in Java Software Development subject at the Faculty of Information Technology, Hanoi University.\n" +
                "\n" +
                "This program is the intellectual property of the developer and is provided for assignment purposes under the following license:\n" +
                "\n" +
                "Permission is hereby granted to Hanoi University and its staff for review and evaluation of this submission. Any distribution or reuse outside of that context " +
                "is strictly prohibited without permission from the copyright holder.");
        copyright.setEditable(false);
        copyright.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        copyright.setLineWrap(true);
        copyright.setWrapStyleWord(true);
        JScrollPane scrollableCopyright = new JScrollPane(copyright);
        gui.add(scrollableCopyright, BorderLayout.CENTER);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        closeProgram();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Exit":
                closeProgram();
                break;
            case "New patron":
                new NewPatron(gui);
                break;
            case "List patrons":
                new ListPatrons(gui);
                break;
            case "New book":
                new NewBook(gui);
                break;
            case "List books":
                new ListBooks(gui);
                break;
            case "Checkout book":
                new CheckoutBook(gui);
                break;
            case "Transaction report":
                new TransactionReport(gui);
                break;
            case "Return book":
                new ReturnBook(gui);
                break;
        }
    }
}
