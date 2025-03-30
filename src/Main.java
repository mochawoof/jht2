import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

class Main {
    public static JFrame f;
    public static JSplitPane split;
    public static JTable table;
    public static DefaultTableModel tableModel;
    public static JTable editTable;
    public static DefaultTableModel editTableModel;

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
        split.setLeftComponent(table);

        editTable = new JTable();
        editTable.setCellSelectionEnabled(true);
        split.setRightComponent(editTable);

        f.setVisible(true);
    }
}