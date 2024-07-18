package a2_2001040208.window;

import a2_2001040208.db.DBManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class ListPatrons extends Window {
    private JTable patronsTable;

    public ListPatrons(JFrame parentGUI) {
        super(parentGUI);
    }

    @Override
    protected void createGUI() {
        gui = new JFrame("List Patrons");
        gui.setSize(600, 300);
        gui.addWindowListener(this);
        gui.setLocation((int) parentGUI.getLocation().getX() + 100, (int) parentGUI.getLocation().getY() + 100);

        // extra TODO: banner
        createBanner("List of Patrons", Color.PINK);

        // TODO: load data to table
        dbManager = DBManager.getInstance();

        Object[][] data = dbManager.getAllPatronsData();
        String[] headers = {"ID", "Name", "Date of Birth", "Email", "Phone", "Patron Type"};
        DefaultTableModel tm = new DefaultTableModel(data, headers);
        patronsTable = new JTable(tm);

        // extra TODO: config table
        patronsTable.setEnabled(false);
        JTableHeader header = patronsTable.getTableHeader();
        header.setFont(new Font(header.getFont().getFontName(), Font.BOLD, header.getFont().getSize()));
        setColumnWidths(0, 0.02);
        setColumnWidths(1, 0.28);
        setColumnWidths(2, 0.15);
        setColumnWidths(3, 0.30);
        setColumnWidths(4, 0.15);
        setColumnWidths(5, 0.15);

        JScrollPane scrollableTable = new JScrollPane(patronsTable);
        scrollableTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        gui.add(scrollableTable, BorderLayout.CENTER);
    }

    private void setColumnWidths(int columnIndex, double ratio) {
        TableColumnModel columnModel = patronsTable.getColumnModel();
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
