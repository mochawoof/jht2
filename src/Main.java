import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.nio.file.*;
import javax.swing.table.*;
import javax.swing.event.*;

class Main {
    public static ArrayList<Byte> bytes = new ArrayList<Byte>();
    public static File file;
    public static int bytesPerRow = 16;

    public static JFrame f;
    public static JSplitPane split;
    public static JScrollPane tableScrollPane;
    public static JTable table;
    public static DefaultTableModel model;

    public static JSplitPane editorSplit;
    public static JScrollPane previewTableScrollPane;
    public static JTable previewTable;
    public static DefaultTableModel previewModel;

    public static JScrollPane editorPanelScrollPane;
    public static JPanel editorPanel;

    public static JToolBar toolBar;

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

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        split.setDividerLocation(500);
        f.add(split, BorderLayout.CENTER);

        table = new JTable();
        table.setCellSelectionEnabled(true);

        tableScrollPane = new JScrollPane(table);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        split.setLeftComponent(tableScrollPane);

        editorSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        editorSplit.setDividerLocation(200);
        split.setRightComponent(editorSplit);

        previewTable = new JTable();
        previewTable.setCellSelectionEnabled(true);
        previewTableScrollPane = new JScrollPane(previewTable);
        previewTableScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        editorSplit.setTopComponent(previewTableScrollPane);

        editorPanel = new JPanel();
        editorPanelScrollPane = new JScrollPane(editorPanel);
        editorSplit.setBottomComponent(editorPanelScrollPane);

        toolBar = new JToolBar();
        f.add(toolBar, BorderLayout.PAGE_START);

        file = new File("Abcs.txt");
        load();
        render();

        f.setVisible(true);
    }

    public static void load() {
        bytes.clear();
        try {
            for (byte b : Files.readAllBytes(file.toPath())) {
                bytes.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void render() {
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            public String getColumnName(int col) {
                return String.format("%02X", col);
            }
        };
        model.setColumnCount(bytesPerRow);
        table.setModel(model);

        previewModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            public String getColumnName(int col) {
                return String.format("%02X", col);
            }
        };
        previewModel.setColumnCount(bytesPerRow);
        previewTable.setModel(previewModel);

        int n = 0;
        for (byte b : bytes) {
            if (n % bytesPerRow == 0) {
                model.setRowCount(model.getRowCount() + 1);
                previewModel.setRowCount(previewModel.getRowCount() + 1);
            }

            int row = (int) Math.floor((double) n / bytesPerRow);
            int col = n - (row * bytesPerRow);

            model.setValueAt(b, row, col);

            Object pre = (char) b;
            previewModel.setValueAt(pre, row, col);

            n++;
        }
    }
}