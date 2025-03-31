import javax.swing.*;
import java.awt.*;

class HexEditor extends JComponent {
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}