import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

class Main {
    public static JFrame f;
    public static JSplitPane split;
    public static JTextArea area;
    public static JTextArea editArea;
    public static JToolBar toolBar;
    public static JButton fileButton;
    public static JButton reloadButton;
    public static JButton saveButton;
    public static JButton saveAsButton;
    public static JComboBox recentBox;
    public static JComboBox modeBox;

    public static byte[] bytes;
    public static File openFile;
    public static boolean isLoaded;
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
        area = new JTextArea("00");
        split.setLeftComponent(new JScrollPane(area));
        editArea = new JTextArea();
        split.setRightComponent(new JScrollPane(editArea));

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

        loadFromDisk(new File("icon800.png"));
    }

    public static JPanel labelled(String label, JComponent c) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(new JLabel(label));
        p.add(c);
        return p;
    }

    public static void loadFromDisk(File f) {
        bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(f.toPath());
            openFile = f;
            fileButton.setText(openFile.getName() + " (" + bytes.length + "b)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();
    }

    public static void refresh() { // NOT reload, loadFromDisk is used for reload
        area.setText("");
        isLoaded = false;
        for (int i = 0; i < bytes.length; i++) {
            if (i % 16 == 0 && i != 0) {
                area.append("\n");
            }
            area.append(String.format("%02X", bytes[i] & 0xff) + " ");
        }
        isLoaded = true;
    }
}