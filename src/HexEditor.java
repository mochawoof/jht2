import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class HexEditor extends JComponent {
    public byte[] bytes;
    public JScrollBar scrollBar;
    public int bytesPerRow = 16;
    public String mode = "Hex Mode";

    public void setBytes(byte[] bs) {
        bytes = bs;
        scrollBar.setMaximum((int) Math.ceil((double) bytes.length / bytesPerRow));
        scrollBar.setValue(0);
    }
    
    public void setScrollBar(JScrollBar sb) {
        scrollBar = sb;
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                repaint();
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                scrollBar.setValue(scrollBar.getValue() + (e.getWheelRotation() * 2));
            }
        });
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int fontSize = 14;
        g.setFont(new Font("monospace", Font.PLAIN, fontSize));
        g.setColor(Color.BLACK);

        for (int i = 0; i + (scrollBar.getValue() * bytesPerRow) < bytes.length; i++) {
            int row = i / bytesPerRow;
            int col = i - (row * bytesPerRow);

            String str = "";
            int index = i + (scrollBar.getValue() * bytesPerRow);
            if (mode.equals("Hex Mode")) {
                str = String.format("%02X", bytes[index] & 0xff);
            } else if (mode.equals("Text Mode")) {
                str = new String(Character.toChars(bytes[index] & 0xff));
            }

            g.drawChars(str.toCharArray(), 0, str.length(), col * (fontSize * 2), (row * fontSize) + fontSize);
        }
    }
}