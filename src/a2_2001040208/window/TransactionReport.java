package a2_2001040208.window;

import a2_2001040208.db.DBManager;
import a2_2001040208.model.Patron;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TransactionReport extends Window {
    private JComboBox<String> options;
    private JComboBox<Patron> patrons;
    private JTable reportTable;
    private JScrollPane scrollableTable;

    public TransactionReport(JFrame parentGUI) {
        super(parentGUI);
    }

    @Override
    protected void createGUI() {
        gui = new JFrame("Transaction Report");
        gui.setSize(550, 300);
        gui.addWindowListener(this);
        gui.setLocation((int) parentGUI.getLocation().getX() + 100, (int) parentGUI.getLocation().getY() + 100);

        dbManager = DBManager.getInstance();

        // TODO: toolbar
        JPanel toolbar = new JPanel();
        toolbar.setBackground(Color.CYAN);
        // options
        options = new JComboBox<>(new String[]{"All transactions", "All checked-out books", "Overdue books"});
        toolbar.add(options);
        // patrons
        patrons = new JComboBox<>(dbManager.getAllPatrons());
        patrons.setSelectedIndex(-1);
        patrons.setEnabled(false);
        toolbar.add(patrons);
        // buttons
        JButton getReportBtn = new JButton("Get report");
        getReportBtn.addActionListener(this);
        toolbar.add(getReportBtn);

        gui.add(toolbar, BorderLayout.NORTH);

        // TODO: table
        reportTable = new JTable();
        reportTable.setEnabled(false);
        scrollableTable = new JScrollPane(reportTable);
        scrollableTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        gui.add(scrollableTable, BorderLayout.CENTER);

        // extra TODO: enable/disable patron field
        options.addActionListener(e -> {
            if (Objects.equals(options.getSelectedItem(), "All transactions")) {
                patrons.setSelectedIndex(-1);
                patrons.setEnabled(false);
            } else {
                patrons.setEnabled(true);
            }
            gui.validate();
        });
    }

    @Override
    public void windowClosing(WindowEvent e) {
        disposeGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Get report")) {
            loadDataToTable(String.valueOf(options.getSelectedItem()));
        }
    }

    private void loadDataToTable(String dataType) {
        switch (dataType) {
            // "All checked-out books" is similar to "All transactions"
            // Because return date is not considered...
            // So I make them different by 1 feature, using getCheckedOutBooks(Patron) in assignment 1
            case "All transactions":
                patrons.setSelectedIndex(-1);
                loadAllTransactions();
                break;
            case "All checked-out books":
                if (patrons.getSelectedItem() != null) {
                    loadCheckedOutBooksBy((Patron) patrons.getSelectedItem(), false);
                } else {
                    loadAllCheckedOutBooks(false);
                }
                break;
            case "Overdue books":
                if (patrons.getSelectedItem() != null) {
                    loadCheckedOutBooksBy((Patron) patrons.getSelectedItem(), true);
                } else {
                    loadAllCheckedOutBooks(true);
                }
                break;
        }
        resetTable();
    }

    private void loadAllTransactions() {
        // load data
        String[] headers = new String[]{"ID", "Book ID", "Patron ID", "Checkout Date", "Due Date"};
        Object[][] data = dbManager.getAllTransactionsData();
        // setup table
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        reportTable = new JTable(tm);
        setColumnWidths(0, 0.10);
        setColumnWidths(1, 0.10);
        setColumnWidths(2, 0.10);
        setColumnWidths(3, 0.35);
        setColumnWidths(4, 0.35);
    }

    private void loadAllCheckedOutBooks(boolean overdue) {
        // load data
        String[] headers = new String[]{"Book Title", "Book ISBN", "Patron ID", "Checkout Date", "Due Date"};
        Object[][] data = dbManager.getAllCheckedOutBooksData();
        if (overdue) data = overdueFilter(data);
        // setup table
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        reportTable = new JTable(tm);
        setColumnWidths(0, 0.35);
        setColumnWidths(1, 0.12);
        setColumnWidths(2, 0.08);
        setColumnWidths(3, 0.20);
        setColumnWidths(4, 0.20);
    }

    private void loadCheckedOutBooksBy(Patron patron, boolean overdue) {
        // load data
        String[] headers = new String[]{"Book Title", "Book ISBN", "Checkout Date", "Due Date"};
        Object[][] data = dbManager.getCheckedOutBooksBy(patron);
        if (overdue) data = overdueFilter(data);
        // setup table
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        reportTable = new JTable(tm);
        setColumnWidths(0, 0.30);
        setColumnWidths(1, 0.20);
        setColumnWidths(2, 0.25);
        setColumnWidths(3, 0.25);
    }

    private Object[][] overdueFilter(Object[][] data) {
        ArrayList<Object[]> overdueData = new ArrayList<>();
        for (Object[] row : data) {
            try {
                // pick the final column
                Date dueDate = DBManager.fromTextToDate((String) row[row.length - 1]);
                // overdue condition
                if (dueDate != null && (new Date()).after(dueDate)) {
                    overdueData.add(row);
                }
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        }
        return overdueData.toArray(new Object[0][]);
    }

    private void setColumnWidths(int columnIndex, double ratio) {
        TableColumnModel columnModel = reportTable.getColumnModel();
        int width = (int) (columnModel.getTotalColumnWidth() * ratio);
        columnModel.getColumn(columnIndex).setPreferredWidth(width);
    }

    private void resetTable() {
        JTableHeader header = reportTable.getTableHeader();
        header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, header.getFont().getSize()));
        scrollableTable.setViewportView(reportTable);
        gui.validate();
    }
}
