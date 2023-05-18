package client;

//1. Chat Window (text based): To allow users to communicate with each other by typing a text.
//2. A “File” menu with new, open, save, saveAs and close should be provided (only the manager can control this)
//3. Allow the manager to kick out a certain peer/user

import remote_interface.IRemoteServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class AdvancedFeature {
    static IRemoteServer server ;
    private JFrame frame;
    private String file_Path ;
    private String file_name;
    private WhiteBoardArea board;


    public AdvancedFeature(JFrame frame, WhiteBoardArea board, IRemoteServer server) {
        this.server = server;
        this.frame = frame;
        this.board = board;
    }







    //// 新方法在 action listening里 advanced feature
    public void open() throws IOException {


        // 用户可以打开文件，用于上传的窗口
        FileDialog open_image = new FileDialog(frame, "open image", FileDialog.LOAD);
        open_image.setVisible(true);
        if (open_image.getFile() != null) {
            //获取file 路径
            // 获取文件名称
            file_Path = open_image.getDirectory();
            file_name = open_image.getFile();
            // open 一个image
            System.out.println("pic_name:"+file_name);
            System.out.println("pic_path:"+file_Path);
            BufferedImage image = ImageIO.read(new File(file_Path + file_name));
            System.out.println("read image error222");

            //bug 修改
            board.drawImage(image);
            System.out.println("read image error333");
            //把image变成byte 然后发送出去给each user
            ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
            //write the image to the imageArray
            ImageIO.write(image, "png", imageArray);
            System.out.println("read image error3。5。。。");
            server.sendOpenBoard(imageArray.toByteArray());
            System.out.println("read image error444");

        }else{
            System.out.println("Board is null. Cannot draw image.");
        }
    }



    public void save() throws IOException {
        if(file_name == null) {
            JOptionPane.showMessageDialog(null, "Please saveAs png file first.");
        }
        else {
            ImageIO.write(board.getCanvas(), "png", new File(file_Path + file_name));
        }
    }


    // 先另存为，再保存
    public void saveAs() throws IOException {
        FileDialog saveAs_image = new FileDialog(frame, "save as image", FileDialog.SAVE);
        saveAs_image.setVisible(true);

        if (saveAs_image.getFile() != null) {
            this.file_Path = saveAs_image.getDirectory();
            this.file_name = saveAs_image.getFile();
            //保存在这个位置，以png 的格式
            ImageIO.write(board.getCanvas(), "png", new File(file_Path + file_name));
        }
    }



}
