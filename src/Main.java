import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.io.*;
import java.nio.file.*;
class Main {
    public static JFrame f;
    public static JSplitPane split;
    public static JTable table;
    public static DefaultTableModel model;
    public static ListSelectionListener tableListener;

    public static JTable sideTable;
    public static DefaultTableModel sideModel;

    public static File file;
    public static byte[] data;
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
        split.setDividerLocation(600);
        f.add(split, BorderLayout.CENTER);

        table = new JTable();
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        sideModel.addRow(new String[] {"Hex", ""});
        sideModel.addRow(new String[] {"ASCII", ""});
        sideModel.addRow(new String[] {"", ""});
        sideModel.addRow(new String[] {"Uint 8", ""});
        sideModel.addRow(new String[] {"Int 8", ""});
        
        sideTable.setModel(sideModel);

        tableListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() != -1 && table.getSelectedColumn() != -1) {
                    Cell c = (Cell) table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
                    if (c != null) {
                        sideModel.setValueAt(c.toString(), 0, 1);
                        sideModel.setValueAt((char) c.b, 1, 1);
                        sideModel.setValueAt(Integer.parseUnsignedInt(c.toString(), 16), 3, 1);
                        sideModel.setValueAt(Integer.parseInt(c.toString(), 16), 4, 1);
                    } else {
                        sideModel.setValueAt("", 0, 1);
                        sideModel.setValueAt("", 1, 1);
                        sideModel.setValueAt("", 3, 1);
                        sideModel.setValueAt("", 4, 1);
                    }
                }
            }
        };
        table.getSelectionModel().addListSelectionListener(tableListener);
        table.getColumnModel().getSelectionModel().addListSelectionListener(tableListener);

        file = new File("test.bin");
        load();
        update();

        f.setVisible(true);
    }

    public static void load() {
        try {
            data = Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            public String getColumnName(int col) {
                return Decimal.toHex(col);
            }
        };
        model.setColumnCount(16);
        
        for (int i = 0; i < data.length; i++) {
            if (i % 16 == 0) {
                model.setRowCount(model.getRowCount() + 1);
            }

            int row = (int) Math.floor((double) i / 16);
            int col = i - (row * 16);

            model.setValueAt(new Cell(data[i]), row, col);
        }

        table.setModel(model);
    }
}