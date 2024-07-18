package a2_2001040208.window;

import a2_2001040208.db.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public abstract class Window extends WindowAdapter implements ActionListener {
    protected JFrame gui = null;
    protected JFrame parentGUI = null;
    protected DBManager dbManager = null;

    public Window() {
        createGUI();
        displayGUI();
    }

    public Window(JFrame parentGUI) {
        this.parentGUI = parentGUI;
        createGUI();
        displayGUI();
    }

    // GENERAL METHODS

    protected static void setConstraints(GridBagConstraints gbc, int gridx, int gridy, double weightx) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
    }

    protected abstract void createGUI();

    protected void createBanner(String name, Color color) {
        JLabel banner = new JLabel(name);
        banner.setOpaque(true);
        banner.setBackground(color);
        banner.setHorizontalAlignment(SwingConstants.CENTER);
        banner.setFont(banner.getFont().deriveFont(14f));
        banner.setPreferredSize(new Dimension(100, 30));
        gui.add(banner, BorderLayout.NORTH);
    }

    protected void displayGUI() {
        gui.setVisible(true);
    }

    protected void disposeGUI() {
        gui.dispose();
    }

    protected void closeProgram() {
        if (dbManager != null) {
            dbManager.closeDBConnection();
        }
        System.exit(0);
    }

    protected void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(gui, message, "Error Message", JOptionPane.ERROR_MESSAGE);
    }

    // GRID-LAYOUT GUI

    protected void showSuccessfulMessage(String message) {
        JOptionPane.showMessageDialog(gui, message, "Successful Message", JOptionPane.INFORMATION_MESSAGE);
        disposeGUI();
    }

}
