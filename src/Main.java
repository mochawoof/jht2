import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;

class Main {
    public static ArrayList<Byte> bytes = new ArrayList<Byte>();
    public static File file;

    public static JFrame f;
    public static JSplitPane split;
    public static JTable table;
    public static DefaultTableModel model;
    public static void main(String[] args) {
        f = new JFrame("JHT 2");
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            f.setIconImage(new ImageIcon(Main.class.getResource("icon64.png").toURI().toURL()).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        split.setDividerLocation(700);
        f.add(split, BorderLayout.CENTER);

        table = new JTable();
        table.setCellSelectionEnabled(true);
        split.setLeftComponent(table);

        f.setVisible(true);
    }

    public static void load() {
        bytes = Files.readAllBytes(file.toPath());
    }

    public static void render() {
        model = new DefaultTableModel();
        table.setModel(model);
    }
}