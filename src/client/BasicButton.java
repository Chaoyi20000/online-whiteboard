//package client;
//
//import remote_interface.IRemoteServer;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.IOException;
//import java.rmi.RemoteException;
//
//public class BasicButton implements ActionListener {
//    private WhiteBoardArea WhiteBoardUI;
//    private IRemoteServer server;
//    private boolean isManager;
//    private JButton clearBtn, saveBtn, saveAsBtn, openBtn, blackBtn, blueBtn, greenBtn, redBtn, orangeBtn, yellowBtn, cyanBtn;
//
//    public BasicButton(WhiteBoardArea WhiteBoardUI, IRemoteServer server, boolean isManager) {
//        this.WhiteBoardUI = WhiteBoardUI;
//        this.server = server;
//        this.isManager = isManager;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == clearBtn) {
//            WhiteBoardUI.reset();
//            // 如果你是manager,你要清空所有人画板
//            // 需要修改 修改
//            if (isManager) {
//                try {
//                    server.clearBoard();
//                } catch (RemoteException ex) {
//                    JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit.");
//                }
//            }
//        } else if (e.getSource() == openBtn) {
//            try {
//                open();
//            } catch (IOException ex) {
//                System.err.println("Error opening file: " + ex.getMessage());
//            }
//        } else if (e.getSource() == saveBtn) {
//            try {
//                save();
//            } catch (IOException ex) {
//                System.err.println("There is an IO error.");
//            }
//        } else if (e.getSource() == saveAsBtn) {
//            try {
//                saveAs();
//            } catch (IOException ex) {
//                System.err.println("There is an IO error.");
//            }
//            // change color of canvas
//        }
//    }
//
//    private void open() throws IOException {
//        // implementation for opening a file
//    }
//
//    private void save() throws IOException {
//        // implementation for saving a file
//    }
//
//    private void saveAs() throws IOException {
//        // implementation for saving a file with a new name
//    }
//}