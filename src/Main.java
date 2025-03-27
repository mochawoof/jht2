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
    public static ListSelectionListener tableListener;

    public static JPopupMenu popupMenu;
    public static JMenuItem addRowItem;
    public static JMenuItem deleteItem;

    public static MouseAdapter popupAdapter;

    public static JTable sideTable;
    public static DefaultTableModel sideModel;

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

        table = new JTable() {
            public void editingCanceled(ChangeEvent e) {
                super.editingCanceled(e);
            }
            public void editingStopped(ChangeEvent e) {
                super.editingStopped(e);
                onTableEdit();
            }
        };
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);
        split.setLeftComponent(new JScrollPane(table));

        popupMenu = new JPopupMenu();
        addRowItem = new JMenuItem("Add Row");
        addRowItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] fillers = new String[model.getColumnCount()];
                for (int i = 0; i < fillers.length; i++) {
                    fillers[i] = "00";
                }
                model.addRow(fillers);
            }
        });
        popupMenu.add(addRowItem);
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                int[] selectedCols = table.getSelectedColumns();

                for (int r = selectedRows[0]; r <= selectedRows[selectedRows.length - 1]; r++) {
                    for (int c = selectedCols[0]; c <= selectedCols[selectedCols.length - 1]; c++) {
                        table.setValueAt("", r, c);
                    }
                }
            }
        });
        popupMenu.add(deleteItem);

        popupAdapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {}

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(table, e.getX(), e.getY());
                }
            }
        };
        table.addMouseListener(popupAdapter);

        sideTable = new JTable() {
            public void editingCanceled(ChangeEvent e) {
                super.editingCanceled(e);
            }
            public void editingStopped(ChangeEvent e) {
                super.editingStopped(e);
                onSideTableEdit();
            }
        };
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

        sideModel.addRow(new String[] {"Oct", ""});
        sideModel.addRow(new String[] {"ASCII", ""});
        sideModel.addRow(new String[] {"Bin", ""});
        sideModel.addRow(new String[] {"Uint 8", ""});
        sideModel.addRow(new String[] {"Int 8", ""});
        
        sideTable.setModel(sideModel);

        tableListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                onTableSelected();
            }
        };
        table.getSelectionModel().addListSelectionListener(tableListener);
        table.getColumnModel().getSelectionModel().addListSelectionListener(tableListener);

        file = new File("asciitable.bin");
        load();

        f.setVisible(true);
    }

    public static void onSideTableEdit() {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();

        int srow = sideTable.getSelectedRow();
        try {
            if (row != -1 && col != -1) {
                String rowName = (String) sideTable.getValueAt(srow, 0);
                String changed = (String) sideTable.getValueAt(srow, 1);

                int inte = 0;
                if (rowName.equals("Oct")) {
                    inte = Integer.parseInt(changed, 8) & 0xff;
                } else if (rowName.equals("ASCII")) {
                    inte = (int) changed.toCharArray()[0];
                } else if (rowName.equals("Bin")) {
                    inte = Integer.parseInt(changed, 2) & 0xff;
                } else if (rowName.equals("Uint 8")) {
                    inte = Integer.parseInt(changed) & 0xff;
                } else if (rowName.equals("Int 8")) {
                    inte = Integer.parseInt(changed);
                }

                String formatted = String.format("%02X", inte);
                formatted = formatted.substring(formatted.length() - 2);
                table.setValueAt(formatted, row, col);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        
        onTableSelected();
    }

    public static void onTableEdit() {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        try {
            String hex = (String) table.getValueAt(row, col);

            if (hex.length() != 2) {
                throw new Exception();
            }

            int testInt = Integer.parseInt(hex, 16);
            table.setValueAt(hex.toUpperCase(), row, col);
        } catch (Exception e) {
            System.err.println(e);
            table.setValueAt("00", row, col);
        }
        onTableSelected();
    }

    public static void onTableSelected() {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();
        try {
            if (row != -1 && col != -1) {
                String hex = (String) table.getValueAt(row, col);
                int inte = Integer.parseInt(hex, 16);

                sideTable.setValueAt(Integer.toOctalString(inte & 0xff), 0, 1);
                sideTable.setValueAt((char) (inte & 0xff), 1, 1);
                sideTable.setValueAt(Integer.toBinaryString(inte & 0xff), 2, 1);
                sideTable.setValueAt(inte & 0xff, 3, 1);
                sideTable.setValueAt(inte, 4, 1);
            }
        } catch (Exception e) {
            System.err.println(e);
            
            for (int i = 0; i < sideModel.getRowCount(); i++) {
                sideTable.setValueAt("", i, 1);
            }
        }
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
        
        for (int i = 0; i < data.length; i++) {
            if (i % 16 == 0) {
                model.setRowCount(model.getRowCount() + 1);
            }

            int row = (int) Math.floor((double) i / 16);
            int col = i - (row * 16);

            model.setValueAt(String.format("%02X", data[i]), row, col);
        }

        table.setModel(model);
    }
}