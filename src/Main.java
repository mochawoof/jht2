import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.event.*;
import java.awt.event.*;

class Main {
    public static JFrame f;
    public static JSplitPane split;
    public static HexEditor area;
    public static JPanel editPanel;
    public static JScrollPane areaScrollPane;
    public static JScrollPane editPanelScrollPane;
    public static JToolBar toolBar;
    public static JButton fileButton;
    public static JButton reloadButton;
    public static JButton saveButton;
    public static JButton saveAsButton;
    public static JComboBox recentBox;
    public static JComboBox modeBox;

    public static File openFile;

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
        area = new HexEditor();
        areaScrollPane = new JScrollPane(area);
        split.setLeftComponent(areaScrollPane);
        editPanel = new JPanel();
        editPanelScrollPane = new JScrollPane(editPanel);
        split.setRightComponent(editPanelScrollPane);

        toolBar = new JToolBar();
        f.add(toolBar, BorderLayout.PAGE_START);

        fileButton = new JButton("New (0b)");
        toolBar.add(fileButton);

        reloadButton = new JButton("Reload");
        toolBar.add(reloadButton);

        saveButton = new JButton("Save");
        toolBar.add(saveButton);

        saveAsButton = new JButton("Save As");
        toolBar.add(saveAsButton);

        recentBox = new JComboBox(new String[] {"Load Recent"});
        recentBox.setMaximumSize(new Dimension(200, 100));
        toolBar.add(recentBox);

        modeBox = new JComboBox(new String[] {"Hex Mode", "Text Mode"});
        modeBox.setMaximumSize(new Dimension(200, 100));
        toolBar.add(modeBox);

        f.setVisible(true);
    }
}