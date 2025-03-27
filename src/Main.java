import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.io.*;
import java.nio.file.*;
class Main {
    public static JFrame f;
    public static JToolBar toolBar;
    public static JButton fileButton;
    public static JSplitPane split;
    public static JTable table;
    public static DefaultTableModel model;
    public static TableModelListener tableModelListener;
    public static ListSelectionListener tableListener;

    public static JTable sideTable;
    public static DefaultTableModel sideModel;
    public static TableModelListener sideTableModelListener;

    public static File file;
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        f = new JFrame("JHT 2");
        f.setSize(600, 300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            f.setIconImage(new ImageIcon(Main.class.getResource("icon64.png").toURI().toURL()).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        toolBar = new JToolBar();
        f.add(toolBar, BorderLayout.PAGE_START);

        fileButton = new JButton();
        toolBar.add(fileButton);

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        split.setDividerLocation(400);
        f.add(split, BorderLayout.CENTER);

        table = new JTable();
        table.setCellSelectionEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);
        split.setLeftComponent(new JScrollPane(table));

        sideTable = new JTable();
        sideTable.getTableHeader().setReorderingAllowed(false);
        split.setRightComponent(new JScrollPane(sideTable));

        sideModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                if (col == 0) {
                    return false;
                }
                return true;
            }

            public String getColumnName(int col) {
                if (col == 0) {
                    return "Type";
                } else if (col == 1) {
                    return "Value";
                }
                return "";
            }
        };
        sideModel.setColumnCount(2);
        
        sideTableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {

            }
        };
        sideModel.addTableModelListener(sideTableModelListener);

        sideModel.addRow(new String[] {"Oct", ""});
        sideModel.addRow(new String[] {"ASCII", ""});
        sideModel.addRow(new String[] {"Bin", ""});
        sideModel.addRow(new String[] {"Uint 8", ""});
        sideModel.addRow(new String[] {"Int 8", ""});
        
        sideTable.setModel(sideModel);

        tableListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

            }
        };
        table.getSelectionModel().addListSelectionListener(tableListener);
        table.getColumnModel().getSelectionModel().addListSelectionListener(tableListener);

        tableModelListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                
            }
        };

        file = new File("asciitable.bin");
        load();

        f.setVisible(true);
    }

    public static void load() {
        try {
            fileButton.setText(file.getName());
            byte[] data = Files.readAllBytes(file.toPath());
            fileButton.setText(file.getName() + " (" + data.length + "b)");

            update(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(byte[] data) {
        model = new DefaultTableModel() {
            public String getColumnName(int col) {
                return String.format("%02X", col & 0xff);
            }
        };
        model.setColumnCount(16);
        model.addTableModelListener(tableModelListener);
        
        for (int i = 0; i < data.length; i++) {
            if (i % 16 == 0) {
                model.setRowCount(model.getRowCount() + 1);
            }

            int row = (int) Math.floor((double) i / 16);
            int col = i - (row * 16);

            model.setValueAt(String.format("%02X", data[i] & 0xff), row, col);
        }

        table.setModel(model);
    }
}