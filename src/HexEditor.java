import javax.swing.*;
import java.awt.*;

class HexEditor extends JComponent {
    public byte[] bytes;
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int row = 0;
        int fontSize = 14;
        g.setFont(new Font("monospace", Font.PLAIN, fontSize));
        
    }
}