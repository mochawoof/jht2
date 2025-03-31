import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.event.*;
import java.awt.event.*;

class Main {
    public static JFrame f;
    public static JSplitPane split;
    public static HexEditor editor;
    public static JPanel panel;
    public static HexEditorScroller editorScroller;
    public static JScrollPane panelScrollPane;
    public static JToolBar toolBar;
    public static JLabel fileLabel;
    public static JButton newButton;
    public static JButton openButton;
    public static JButton reloadButton;
    public static JButton saveButton;
    public static JButton saveAsButton;
    public static JComboBox recentBox;
    public static JComboBox modeBox;

    public static PropertiesX props;
    public static File openFile = null;

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

        props = new PropertiesX();

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        split.setDividerLocation(600);
        f.add(split, BorderLayout.CENTER);
        editor = new HexEditor();
        editorScroller = new HexEditorScroller(editor);
        split.setLeftComponent(editorScroller);
        panel = new JPanel();
        panelScrollPane = new JScrollPane(panel);
        split.setRightComponent(panelScrollPane);

        toolBar = new JToolBar();
        f.add(toolBar, BorderLayout.PAGE_START);

        fileLabel = new JLabel("");
        toolBar.add(fileLabel);

        newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile = null;
                loadFromDisk();
            }
        });
        toolBar.add(newButton);

        openButton = new JButton("Open");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.getActionMap().get("viewTypeDetails").actionPerformed(null);
                if (openFile != null && openFile.exists()) {
                    c.setCurrentDirectory(openFile.getParentFile());
                }
                if (c.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
                    openFile = c.getSelectedFile();
                    loadFromDisk();
                }
            }
        });
        toolBar.add(openButton);

        String recents = props.get("recents", "");
        recentBox = new JComboBox((recents.isEmpty()) ? new String[0] : recents.split(","));
        recentBox.setMaximumRowCount(10);
        recentBox.insertItemAt("Open Recent", 0);
        recentBox.setSelectedIndex(0);
        recentBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = (String) recentBox.getSelectedItem();
                if (!selected.equals("Open Recent")) {
                    openFile = new File(selected);
                    loadFromDisk();
                }
            }
        });
        recentBox.setPreferredSize(new Dimension(100, (int) recentBox.getPreferredSize().getHeight()));
        toolBar.add(recentBox);

        reloadButton = new JButton("Reload");
        reloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFromDisk();
            }
        });
        toolBar.add(reloadButton);

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (openFile == null || !openFile.exists()) {
                    saveAs();
                } else {
                    save();
                }
            }
        });
        toolBar.add(saveButton);

        saveAsButton = new JButton("Save As");
        saveAsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });
        toolBar.add(saveAsButton);

        toolBar.add(Box.createGlue());
        modeBox = new JComboBox(new String[] {"Hex Mode", "Text Mode"});
        modeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = (String) modeBox.getSelectedItem();
                editor.mode = selected;
                editor.repaint();
            }
        });
        modeBox.setPreferredSize(new Dimension(100, (int) recentBox.getPreferredSize().getHeight()));
        toolBar.add(modeBox);

        loadFromDisk();
        f.setVisible(true);
    }

    public static void updateFileLabel() {
        fileLabel.setText(openFile.getName() + " (" + editor.bytes.length + "b)");
    }

    public static void loadFromDisk() {
        if (openFile != null) {
            try {
                editor.setBytes(Files.readAllBytes(openFile.toPath()));
                updateFileLabel();

                String absolutePath = openFile.toPath().toAbsolutePath().toString();
                if (((DefaultComboBoxModel) recentBox.getModel()).getIndexOf(absolutePath) == -1) {
                    recentBox.addItem(absolutePath);

                    String recents = props.get("recents", "");
                    if (!recents.isEmpty()) {
                        recents += ",";
                    }
                    recents += absolutePath;

                    if (recentBox.getItemCount() > 10) {
                        recentBox.removeItemAt(0);
                        recents = recents.substring(recents.indexOf(",") + 1);
                    }

                    props.set("recents", recents);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(f, "File could not be read or doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            editor.setBytes(new byte[1]);
            fileLabel.setText("");
        }

        editor.repaint();
    }

    public static void save() {
        try {
            FileOutputStream out = new FileOutputStream(openFile.getAbsolutePath());
            out.write(editor.bytes);
            updateFileLabel();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(f, "Failed to save file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void saveAs() {
        JFileChooser c = new JFileChooser();
        c.getActionMap().get("viewTypeDetails").actionPerformed(null);
        if (c.showSaveDialog(f) == JFileChooser.APPROVE_OPTION) {
            openFile = c.getSelectedFile();
            save();
        }
    }
}