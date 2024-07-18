package a2_2001040208.window;

import a2_2001040208.db.DBManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class ListBooks extends Window {
    private JTable booksTable;

    public ListBooks(JFrame parentGUI) {
        super(parentGUI);
    }

    @Override
    protected void createGUI() {
        gui = new JFrame("List Books");
        gui.setSize(650, 300);
        gui.addWindowListener(this);
        gui.setLocation((int) parentGUI.getLocation().getX() + 100, (int) parentGUI.getLocation().getY() + 100);

        // extra TODO: banner
        createBanner("List of Books", Color.YELLOW);

        // TODO: load data to table
        dbManager = DBManager.getInstance();

        Object[][] data = dbManager.getAllBooksData();
        String[] headers = {"ID", "ISBN", "Title", "Author", "Genre", "PubYear", "No. Copies"};
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        booksTable = new JTable(tm);

        // extra TODO: config table
        JTableHeader header = booksTable.getTableHeader();
        header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, header.getFont().getSize()));
        booksTable.setEnabled(false);
        setColumnWidths(0, 0.02);
        setColumnWidths(1, 0.15);
        setColumnWidths(2, 0.25);
        setColumnWidths(3, 0.20);
        setColumnWidths(4, 0.20);
        setColumnWidths(5, 0.08);
        setColumnWidths(6, 0.10);

        JScrollPane scrollableTable = new JScrollPane(booksTable);
        scrollableTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        gui.add(scrollableTable, BorderLayout.CENTER);
    }

    private void setColumnWidths(int columnIndex, double ratio) {
        TableColumnModel columnModel = booksTable.getColumnModel();
        int width = (int) (columnModel.getTotalColumnWidth() * ratio);
        columnModel.getColumn(columnIndex).setPreferredWidth(width);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //
    }

    @Override
    public void windowClosing(WindowEvent e) {
        disposeGUI();
    }
}

