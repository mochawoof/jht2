import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.io.*;
import java.nio.file.*;
class Main {
    public static JFrame f;
    public static JTable table;
    public static DefaultTableModel model;
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        f = new JFrame("JHT 2");
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            f.setIconImage(new ImageIcon(Main.class.getResource("icon64.png").toURI().toURL()).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        table = new JTable();
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        f.add(new JScrollPane(table), BorderLayout.CENTER);

        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            public String getColumnName(int col) {
                return String.format("%02X", col);
            }
        };
        model.setColumnCount(16);
        model.setRowCount(100);
        table.setModel(model);

        f.setVisible(true);
    }
}