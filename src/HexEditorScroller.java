import javax.swing.*;
import java.awt.*;

class HexEditorScroller extends JPanel {
    public HexEditorScroller(HexEditor editor) {
        setLayout(new BorderLayout());
        add(editor, BorderLayout.CENTER);
        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        editor.setScrollBar(scrollBar);
        add(scrollBar, BorderLayout.LINE_END);
    }
}