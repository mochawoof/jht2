import javax.swing.*;
import java.awt.*;

class HexEditor extends JComponent {
    public byte[] bytes;
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int fontSize = 14;
        g.setFont(new Font("monospace", Font.PLAIN, fontSize));
        g.setColor(Color.BLACK);

        for (int i = 0; i < bytes.length; i++) {
            int row = i / 16;
            int col = i - (row * 16);

            if (row * fontSize < getHeight()) {
                g.drawChars(String.format("%02X", bytes[i] & 0xff).toCharArray(), 0, 2, col * (fontSize * 2), row * fontSize);
            } else {
                break;
            }
        }
    }
}