package client;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ToolButton {
    int width = 25;  // 目标宽度
    int height = 25; // 目标高度
    public JButton toolButton (String iconPath, String tooltip,ActionListener actionListener){


        ImageIcon icon = new ImageIcon(iconPath);
        Image image = icon.getImage();
        // 调整图像大小
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        // 创建新的图标对象
        Icon scaledIcon = new ImageIcon(scaledImage);

        JButton button = new JButton(scaledIcon);
        button.setToolTipText(tooltip);
        button.addActionListener(actionListener);




        return button;
    }
}
