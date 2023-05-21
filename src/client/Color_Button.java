package client;

import javax.swing.*;
import java.awt.*;

public class Color_Button {
    public JButton ColorButton(Color color){
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(30, 30));
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }
}
