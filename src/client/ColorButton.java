package client;

import javax.swing.*;
import java.awt.*;

public class ColorButton {
    public JButton ColorButton(Color color){
        JButton button = new JButton();
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }
}
