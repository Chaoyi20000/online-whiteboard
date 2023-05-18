package client;


import remote_interface.IRemoteClient;
import remote_interface.IRemoteServer;
import remote_interface.IRemoteWhiteBoard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;

//要分区 除了白板以外的部分
public class Client extends UnicastRemoteObject implements IRemoteClient {
    private static final long serialVersionUID = 1L;
    static IRemoteServer server;
    private boolean isManager; //true if is manager
    private boolean havePermission; //permission granted by manager
    private JFrame frame;
    private DefaultListModel<String> userList;
    private DefaultListModel<String> chatList;
    private JButton clearBtn, saveBtn, saveAsBtn, openBtn, blackBtn, blueBtn, greenBtn, redBtn, orangeBtn, yellowBtn, cyanBtn;
    private JButton brownBtn, pinkBtn, greyBtn, purpleBtn, limeBtn, darkgreyBtn, magentaBtn, aoiBtn, skyBtn;
    private JButton drawBtn, lineBtn, rectBtn, circleBtn, triangleBtn, textBtn;
    private JScrollPane msgArea;
    private JTextArea tellColor, displayColor;
    private JList<String> chat;
    private ArrayList<JButton> btnList;
    private WhiteBoardArea canvasUI;
    private String mode;
    private String clientName; // store client's name
    private String picName; // if saveAs then keep on saving on that location
    private String picPath; // if saveAs then keep on saving on that location
    private Color cur_Color;
    private Hashtable<String, Point> startPoints = new Hashtable<String, Point>();


    protected Client() throws RemoteException {
        //一个client包含的field
        userList = new DefaultListModel<>();
        isManager = false;
        havePermission = true;
        chatList = new DefaultListModel<>();
        btnList = new ArrayList<>();
    }


    //看鼠标拿到了什么action
    // 监听鼠标上操作


    ActionListener actionListener = new ActionListener() {


        public void actionPerformed(ActionEvent e) {
            // the chosen mode will be boxed with black border
            LineBorder empty = new LineBorder(new Color(238, 238, 238), 2);
            LineBorder box = new LineBorder(Color.black, 2);
            AdvancedFeature advancedFeature = new AdvancedFeature(frame, canvasUI,server);


            // 四个基础按键操作
            if (e.getSource() == clearBtn) {
                canvasUI.reset();
                //如果你是manager,你要清空所有人画板
                //需要修改 修改
                if (isManager) {
                    try {
                        server.clearBoard();
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit.");
                    }
                }
            } else if (e.getSource() == openBtn) {
                try {
                    advancedFeature.open();
                } catch (IOException ex) {
                    System.err.println("Error opening file: " + ex.getMessage());
                }
            } else if (e.getSource() == saveBtn) {
                try {
                    advancedFeature.save();
                } catch (IOException ex) {
                    System.err.println("There is an IO error.");
                }
            } else if (e.getSource() == saveAsBtn) {
                try {
                    advancedFeature.saveAs();
                } catch (IOException ex) {
                    System.err.println("There is an IO error.");
                }
                //change color of canvas
            }


            // 颜色操作板块按钮


            else if (e.getSource() == blackBtn) {
                canvasUI.setColor(Color.black);
            } else if (e.getSource() == blueBtn) {
                canvasUI.setColor(Color.blue);
            } else if (e.getSource() == greenBtn) {
                canvasUI.setColor(Color.green);
            } else if (e.getSource() == redBtn) {
                canvasUI.setColor(Color.red);
            } else if (e.getSource() == orangeBtn) {
                canvasUI.setColor(Color.orange);
            } else if (e.getSource() == yellowBtn) {
                canvasUI.setColor(Color.yellow);
            } else if (e.getSource() == cyanBtn) {
                canvasUI.setColor(Color.cyan);
            } else if (e.getSource() == brownBtn) {
                canvasUI.setColor(new Color(165, 42, 42));
            } else if (e.getSource() == pinkBtn) {
                canvasUI.setColor(Color.pink);
            } else if (e.getSource() == greyBtn) {
                canvasUI.setColor(Color.gray);
            } else if (e.getSource() == purpleBtn) {
                canvasUI.setColor(new Color(128, 0, 128));
            } else if (e.getSource() == limeBtn) {
                canvasUI.setColor(Color.green);
            } else if (e.getSource() == darkgreyBtn) {
                canvasUI.setColor(Color.darkGray);
            } else if (e.getSource() == magentaBtn) {
                canvasUI.setColor(Color.magenta);
            } else if (e.getSource() == aoiBtn) {
                canvasUI.setColor(new Color(0, 128, 128));
            } else if (e.getSource() == skyBtn) {
                canvasUI.setColor(new Color(135, 206, 235));
            } else if (e.getSource() == drawBtn) {
                canvasUI.draw();
                for (JButton button : btnList) {
                    if (button == drawBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            }


            //不同形状的graph
            else if (e.getSource() == lineBtn) {
                canvasUI.line();
                for (JButton button : btnList) {
                    if (button == lineBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == rectBtn) {
                canvasUI.rect();
                for (JButton button : btnList) {
                    if (button == rectBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == circleBtn) {
                canvasUI.circle();
                for (JButton button : btnList) {
                    if (button == circleBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == triangleBtn) {
                canvasUI.triangle();
                for (JButton button : btnList) {
                    if (button == triangleBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == textBtn) {
                canvasUI.text();
                for (JButton button : btnList) {
                    if (button == textBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            }

            // 如果我们换了颜色， 我们display current color也需要换掉

            //颜色需要修改，还不够

            cur_Color = canvasUI.getCurrColor();
            mode = canvasUI.gerCurrMode();
            if (e.getSource() == blackBtn || e.getSource() == blueBtn || e.getSource() == greenBtn || e.getSource() == redBtn
                    || e.getSource() == orangeBtn || e.getSource() == yellowBtn || e.getSource() == cyanBtn) {
                cur_Color = canvasUI.getCurrColor();
                LineBorder border1 = new LineBorder(cur_Color, 2);
                displayColor.setBackground(canvasUI.getCurrColor());
                switch (mode) {
                    case "draw":
                        drawBtn.setBorder(border1);
                        break;
                    case "line":
                        lineBtn.setBorder(border1);
                        break;
                    case "rectangle":
                        rectBtn.setBorder(border1);
                        break;
                    case "circle":
                        circleBtn.setBorder(border1);
                        break;
                    case "triangle":
                        triangleBtn.setBorder(border1);
                        break;
                    case "text":
                        textBtn.setBorder(border1);
                        break;
                    default :
                        break;
                }
            }
        }
    };




    @Override
    public String getClientName() throws RemoteException {
        return this.clientName;
    }

    @Override
    public void setClientName(String name) throws RemoteException {
        this.clientName = name;

    }

    @Override
    public void assignClientManager() throws RemoteException {
        this.isManager = true;

    }

    @Override
    public boolean getManager() throws RemoteException {
        return this.isManager;
    }

    @Override
    public void clearBoard() throws RemoteException {
        if (isManager == false) {
            this.canvasUI.reset();
        }

    }

    @Override
    public byte[] sendBoard() throws RemoteException, IOException {
        //把image变成byte 然后发送出去给each user
        ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
        //write the image to the imageArray
        ImageIO.write(this.canvasUI.getCanvas(), "png", imageArray);
        return imageArray.toByteArray();
    }

    @Override
    public void drawOpenBoard(byte[] targetBoard) throws IOException {
        System.out.println("error in drawOpenBoard111");
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(targetBoard));
        this.canvasUI.drawImage(image);
        System.out.println("error in drawOpenBoard2222");

    }

    @Override
    public void updateChat(String msg) throws RemoteException {
        this.chatList.addElement(msg);

    }

    @Override
    public boolean requestPermission(String name) throws IOException {
        if (JOptionPane.showConfirmDialog(frame,
                name + " wants to join. Do you approve?", "Grant permission",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setPermission(boolean permission) throws IOException {
        this.havePermission = permission;
    }

    @Override
    public boolean getPermission() throws IOException {
        return this.havePermission;
    }

    @Override
    public void closeUI() throws IOException {
        //if manager does not give permission
        if (!this.havePermission) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null, "Sorry, You were not grant access to the shared whiteboard." + "\n",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }
            });
            t.start();
            return;
        }
        //if kicked out or manager quit
        Thread t = new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(frame, "The manager has quit.\n or you have been removed.\n" +
                                "Your application will be closed.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
        t.start();
    }


//
//    //// 剩余部分是来写UI board 的部分
    public void drawClientUI(IRemoteServer server) {

        //build the GUI
        frame = new JFrame(clientName + "'s WhiteBoard");
        Container content = frame.getContentPane();

        canvasUI = new WhiteBoardArea(clientName, isManager,server);

        //关于颜色的button
        blackBtn = new JButton();
        blackBtn.setBackground(Color.black);
        blackBtn.setBorderPainted(false);
        blackBtn.setOpaque(true);
        blackBtn.addActionListener(actionListener);

        blueBtn = new JButton();
        blueBtn.setBackground(Color.blue);
        blueBtn.setBorderPainted(false);
        blueBtn.setOpaque(true);
        blueBtn.addActionListener(actionListener);

        greenBtn = new JButton();
        greenBtn.setBackground(Color.green);
        greenBtn.setBorderPainted(false);
        greenBtn.setOpaque(true);
        greenBtn.addActionListener(actionListener);

        redBtn = new JButton();
        redBtn.setBackground(Color.red);
        redBtn.setBorderPainted(false);
        redBtn.setOpaque(true);
        redBtn.addActionListener(actionListener);

        orangeBtn = new JButton();
        orangeBtn.setBackground(Color.orange);
        orangeBtn.setBorderPainted(false);
        orangeBtn.setOpaque(true);
        orangeBtn.addActionListener(actionListener);

        yellowBtn = new JButton();
        yellowBtn.setBackground(Color.yellow);
        yellowBtn.setBorderPainted(false);
        yellowBtn.setOpaque(true);
        yellowBtn.addActionListener(actionListener);

        cyanBtn = new JButton();
        cyanBtn.setBackground(Color.cyan);
        cyanBtn.setBorderPainted(false);
        cyanBtn.setOpaque(true);
        cyanBtn.addActionListener(actionListener);

        brownBtn = new JButton();
        brownBtn.setBackground(new Color(153,76,0));
        brownBtn.setBorderPainted(false);
        brownBtn.setOpaque(true);
        brownBtn.addActionListener(actionListener);

        pinkBtn = new JButton();
        pinkBtn.setBackground(new Color(255,153,204));
        pinkBtn.setBorderPainted(false);
        pinkBtn.setOpaque(true);
        pinkBtn.addActionListener(actionListener);

        greyBtn = new JButton();
        greyBtn.setBackground(Color.gray);
        greyBtn.setBorderPainted(false);
        greyBtn.setOpaque(true);
        greyBtn.addActionListener(actionListener);

        purpleBtn = new JButton();
        purpleBtn.setBackground(new Color(102,0,204));
        purpleBtn.setBorderPainted(false);
        purpleBtn.setOpaque(true);
        purpleBtn.addActionListener(actionListener);

        limeBtn = new JButton();
        limeBtn.setBackground(new Color(102,102,0));
        limeBtn.setBorderPainted(false);
        limeBtn.setOpaque(true);
        limeBtn.addActionListener(actionListener);

        darkgreyBtn = new JButton();
        darkgreyBtn.setBackground(Color.darkGray);
        darkgreyBtn.setBorderPainted(false);
        darkgreyBtn.setOpaque(true);
        darkgreyBtn.addActionListener(actionListener);

        magentaBtn = new JButton();
        magentaBtn.setBackground(Color.magenta);
        magentaBtn.setBorderPainted(false);
        magentaBtn.setOpaque(true);
        magentaBtn.addActionListener(actionListener);

        aoiBtn = new JButton();
        aoiBtn.setBackground(new Color(0,102,102));
        aoiBtn.setBorderPainted(false);
        aoiBtn.setOpaque(true);
        aoiBtn.addActionListener(actionListener);

        skyBtn = new JButton();
        skyBtn.setBackground(new Color(0,128,255));
        skyBtn.setBorderPainted(false);
        skyBtn.setOpaque(true);
        skyBtn.addActionListener(actionListener);

        int width = 25;  // 目标宽度
        int height = 25; // 目标高度
        //绘画mode的button 不同icon
        LineBorder border = new LineBorder(Color.black, 2);
        Icon icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/pen_icon.png");
        System.out.println("icon test :" + icon.getIconHeight());
        Image image = ((ImageIcon) icon).getImage();
    // 调整图像大小
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    // 创建新的图标对象
        Icon scaledIcon = new ImageIcon(scaledImage);
        drawBtn = new JButton(scaledIcon);
        //可以换一下tips
        drawBtn.setToolTipText("Pencil draw");
        drawBtn.setBorder(border);
        drawBtn.addActionListener(actionListener);
        border = new LineBorder(new Color(238,238,238), 2);

        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/straight_line.png");
        image = ((ImageIcon) icon).getImage();
        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        scaledIcon = new ImageIcon(scaledImage);
        lineBtn = new JButton(scaledIcon);
        lineBtn.setToolTipText("Draw line");
        lineBtn.setBorder(border);
        lineBtn.addActionListener(actionListener);

        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/rectangle.png");
        image = ((ImageIcon) icon).getImage();
        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        scaledIcon = new ImageIcon(scaledImage);

        rectBtn = new JButton(scaledIcon);
        rectBtn.setToolTipText("Draw rectangle");
        rectBtn.setBorder(border);
        rectBtn.addActionListener(actionListener);

        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/circle.png");
        image = ((ImageIcon) icon).getImage();
        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        scaledIcon = new ImageIcon(scaledImage);
        circleBtn = new JButton(scaledIcon);
        circleBtn.setToolTipText("Draw circle");
        circleBtn.setBorder(border);
        circleBtn.addActionListener(actionListener);

        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/triangle.png");
        image = ((ImageIcon) icon).getImage();
        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        scaledIcon = new ImageIcon(scaledImage);
        triangleBtn = new JButton(scaledIcon);
        triangleBtn.setToolTipText("Draw triangle");
        triangleBtn.setBorder(border);
        triangleBtn.addActionListener(actionListener);

        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/text_box.png");
        image = ((ImageIcon) icon).getImage();
        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        scaledIcon = new ImageIcon(scaledImage);
        textBtn = new JButton(scaledIcon);
        textBtn.setToolTipText("write in text box");
        textBtn.setBorder(border);
        textBtn.addActionListener(actionListener);



        btnList.add(drawBtn);
        btnList.add(lineBtn);
        btnList.add(rectBtn);
        btnList.add(circleBtn);
        btnList.add(triangleBtn);
        btnList.add(textBtn);
//        btnList.add(eraserBtn);

        // 四个advanced feature的button
        clearBtn = new JButton("New Board");
        clearBtn.setToolTipText("Create a new board");
        //这样点击之后才能注册
        clearBtn.addActionListener(actionListener);
        saveBtn = new JButton("Save Image");
        saveBtn.setToolTipText("Save as image file");
        saveBtn.addActionListener(actionListener);
        saveAsBtn = new JButton("Save as");
        saveAsBtn.setToolTipText("Save image file");
        saveAsBtn.addActionListener(actionListener);
        openBtn = new JButton("Open Image");
        openBtn.setToolTipText("Open an image file");
        openBtn.addActionListener(actionListener);

        //关于颜色
        tellColor = new JTextArea("The current color is:");
        tellColor.setBackground(new Color(238,238,238));
        displayColor = new JTextArea("");
        displayColor.setBackground(Color.black);

        // if the client is the manager, he can save, open and clear the white board image
        if (isManager == false) {
            clearBtn.setVisible(false);
            saveBtn.setVisible(false);
            saveAsBtn.setVisible(false);
            openBtn.setVisible(false);
        }


        ////////////////////修改
        //list all other user

        JList<String> list = new JList<>(userList);
        JScrollPane currUsers = new JScrollPane(list);
        currUsers.setMinimumSize(new Dimension(100, 150));
        if(!isManager) {
            currUsers.setMinimumSize(new Dimension(100, 290));
        }
        //有问题需要修改
        //manager 可以踢人的功能
        // if the client is the manager, he has the right to remove the client
        if (isManager) {
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    @SuppressWarnings("unchecked")
                    JList<String> list = (JList<String>)evt.getSource();
                    if (evt.getClickCount() == 2) {
                        int index = list.locationToIndex(evt.getPoint());
                        String selectedName = list.getModel().getElementAt(index);
                        try {
                            //manager can't remove him/herself
                            if(! getClientName().equals(selectedName)) {
                                int dialogResult = JOptionPane.showConfirmDialog (frame, "Are you sure to remove " + selectedName + "?",
                                        "Warning", JOptionPane.YES_NO_OPTION);
                                if(dialogResult == JOptionPane.YES_OPTION) {
                                    try {
                                        ///////////////////bug
                                        server.RemoveTargetUser(selectedName);
                                        System.out.println("remove select user" + selectedName);
                                        updateClientList(server.getClientList());
                                        System.out.println(server.getClientList());
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        System.err.println("There is an IO error.");
                                    }
                                }
                            }
                        } catch (HeadlessException e) {
                            // TODO Auto-generated catch block
                            System.err.println("There is an headless error.");
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            System.err.println("There is an IO error.");
                        }
                    }
                }
            });
        }








//         chat box 功能

        chat = new JList<>(chatList);
        msgArea = new JScrollPane(chat);
        msgArea.setMinimumSize(new Dimension(100, 100));
        JTextField msgText = new JTextField();
        JButton sendBtn = new JButton("Send"); //addMouseListener here 直接call server 去broadcast message
        sendBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(!msgText.getText().equals("")) {
                    try {
                        server.updateChat(clientName + ": "+ msgText.getText());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    // let the scrollpane to always show the newest chat message
                    SwingUtilities.invokeLater(() -> {
                        JScrollBar vertical = msgArea.getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum());
                    });
                    msgText.setText("");
                }
            }
        });



        // layout
        GroupLayout layout = new GroupLayout(content);
        content.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
//
//
//
//        ////修改bug
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(drawBtn)
                        ///修改bug

                        .addComponent(lineBtn)
                        .addComponent(rectBtn)
                        .addComponent(circleBtn)
                        .addComponent(triangleBtn)
                        .addComponent(textBtn)
//                        .addComponent(eraserBtn)
                )
                .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(canvasUI)
                        .addComponent(msgArea)
                        .addGroup(layout.createSequentialGroup()

                                .addComponent(msgText)
                                .addComponent(sendBtn)
                        )
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(blackBtn)
                                .addComponent(yellowBtn)
                                .addComponent(cyanBtn)
                                .addComponent(brownBtn)
                                .addComponent(greyBtn)
                                .addComponent(purpleBtn)
                                .addComponent(limeBtn)
                                .addComponent(orangeBtn)


                        )
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pinkBtn)
                                .addComponent(redBtn)
                                .addComponent(greenBtn)
                                .addComponent(blueBtn)
                                .addComponent(darkgreyBtn)
                                .addComponent(magentaBtn)
                                .addComponent(aoiBtn)
                                .addComponent(skyBtn)
                        )

                )
                .addGroup(layout.createParallelGroup(CENTER)
                        .addComponent(clearBtn)
                        .addComponent(openBtn)
                        .addComponent(saveBtn)
                        .addComponent(saveAsBtn)
                        .addComponent(currUsers)
                        .addComponent(tellColor)
                        .addComponent(displayColor)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(drawBtn)
                                .addComponent(lineBtn)
                                .addComponent(rectBtn)
                                .addComponent(circleBtn)
                                .addComponent(triangleBtn)
                                .addComponent(textBtn)
                        )
                        .addComponent(canvasUI)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(clearBtn)
                                .addComponent(openBtn)
                                .addComponent(saveBtn)
                                .addComponent(saveAsBtn)
                                .addComponent(currUsers)
                                .addComponent(tellColor)
                                .addComponent(displayColor)
                        )
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(msgArea)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(msgText)
                                .addComponent(sendBtn)
                        )
                )
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(BASELINE)
                                .addComponent(blackBtn)
                                .addComponent(yellowBtn)
                                .addComponent(cyanBtn)
                                .addComponent(brownBtn)
                                .addComponent(greyBtn)
                                .addComponent(purpleBtn)
                                .addComponent(limeBtn)
                                .addComponent(orangeBtn)

                        )
                        .addGroup(layout.createParallelGroup(BASELINE)

                                .addComponent(pinkBtn)
                                .addComponent(redBtn)
                                .addComponent(greenBtn)
                                .addComponent(blueBtn)
                                .addComponent(darkgreyBtn)
                                .addComponent(magentaBtn)
                                .addComponent(aoiBtn)
                                .addComponent(skyBtn)
                        )
                )
        );
        layout.linkSize(SwingConstants.HORIZONTAL, clearBtn, saveBtn, saveAsBtn, openBtn); //format to same size

        // set the minimum framesize
        if (isManager) frame.setMinimumSize(new Dimension(820, 600));
        else frame.setMinimumSize(new Dimension(820, 600));

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);

        //if the manager close the window, all other client are removed and the all clients' window are force closed

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (isManager) {
                    if (JOptionPane.showConfirmDialog(frame,
                            "You are the manager? Are you sure close the application?", "Close Application?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            server.RemoveAllUser();

                        } catch (IOException e) {
                            System.err.println("There is an IO error");
                        } finally {
                            System.exit(0);
                        }
                    }
                } else {
                    if (JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to quit?", "Close Paint Board?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            server.RemoveSelf(clientName);
                            updateClientList(server.getClientList());
                        } catch (RemoteException e) {
                            JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit.");
                        } finally {
                            System.exit(0);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void updateClientList(Set<IRemoteClient> clientSet) throws RemoteException {
        //更新之前，先清空被地地user list
        this.userList.removeAllElements();
        for(IRemoteClient c: clientSet) {
            try {
                userList.addElement(c.getClientName());
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    //比较复杂，用来同步画板地操作

    //get the info sent from the other clients, and update the white board accordingly
    public void synchronizeBoard(IRemoteWhiteBoard syncboard) throws RemoteException {

        // skip syncboard from itself
        if (syncboard.getName().compareTo(clientName) == 0) {
            return ;
        }

        Mode_Shape mode_shape = new Mode_Shape();


        if (syncboard.getState().equals("To start")) {
            //Let startPoint stores the start point of client x and wait for the next draw action
            startPoints.put(syncboard.getName(), syncboard.getPoint());
            return;
        }

        //start from the start point of client x
        Point startPt = (Point)startPoints.get(syncboard.getName());

        //set canvas stroke color
        canvasUI.getGraphic().setPaint(syncboard.getColor());
        cur_Color = canvasUI.getCurrColor();



        if (syncboard.getState().equals("drawing")) {
            mode_shape.makeLine(startPt, syncboard.getPoint());
            startPoints.put(syncboard.getName(), syncboard.getPoint());
            canvasUI.getGraphic().draw(mode_shape.getShape());
            canvasUI.repaint();
            canvasUI.getGraphic().setPaint(cur_Color);
            return;
        }


//        the mouse is released so we draw from start point to the broadcast point

        if (syncboard.getState().equals("end drawing")) {
            if (syncboard.getMode().equals("draw") || syncboard.getMode().equals("line")) {
                mode_shape.makeLine(startPt, syncboard.getPoint());
                System.out.println("sync successful on draw");
            } else if (syncboard.getMode().equals("rectangle")) {
                mode_shape.makeRect(startPt, syncboard.getPoint());
            } else if (syncboard.getMode().equals("circle")) {
                mode_shape.makeCircle(startPt, syncboard.getPoint());
            }  else if (syncboard.getMode().equals("triangle")) {
                mode_shape.makeTriangle(startPt, syncboard.getPoint());
            } else if (syncboard.getMode().equals("text")) {
                canvasUI.getGraphic().setFont(new Font("TimesRoman", Font.PLAIN, 16));
                canvasUI.getGraphic().drawString(syncboard.getText(), syncboard.getPoint().x, syncboard.getPoint().y);
//                System.out.println("client name is "+clientName);
//                System.out.println("Output " + (syncboard.getText()) +" " +(syncboard.getPoint().x)+ (syncboard.getPoint().y));

//                canvasUI.repaint();
            }





//            //draw shape if in shape mode: triangle, circle, rectangle
            if (!syncboard.getMode().equals("text")) {
                try {
                    canvasUI.getGraphic().draw(mode_shape.getShape());
                    canvasUI.repaint();
                    //once finished drawing remove the start point of client x
                    startPoints.remove(syncboard.getName());
                    return;
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            canvasUI.repaint();
            startPoints.remove(syncboard.getName());

        }
    }




    //几种画画地mode实现 没写完
    //修改bug这里






        public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, ServerNotActiveException {


//        if(args.length != 2) {
//            throw new IllegalArgumentException("Need exactly two arguments.");
//        }

        try {

//            if(!(args[0].equals("localhost") || args[0].equals("127.0.0.1"))) {
//                System.err.println("Please enter localhost or 127.0.0.1");
//                return;
//            }
//            String serverAddress = "//" + args[0]+":"+args[1] + "/WhiteBoardServer";


            String hostname = "localhost";
            String port = "8888";
            String serverAddress ="//" + hostname+":"+port+ "/WhiteBoard";

            //Look up the Canvas Server from the RMI name registry
            server = (IRemoteServer) Naming.lookup(serverAddress);
            IRemoteClient client = new Client();
            //show user register GUI and register the user name to server
            boolean validName = false;
            String client_name = "";
            while(!validName) {
                client_name = JOptionPane.showInputDialog("Please type in your name:");
                if(client_name.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a name!");
                }else {
                    validName = true;
                }
                for(IRemoteClient c : server.getClientList()) {
                    if(client_name.equals(c.getClientName()) || c.getClientName().equals("*"+client_name)) {
                        validName = false;
                        JOptionPane.showMessageDialog(null, "The name is taken, think a different name!");
                    }
                }
            }
            client.setClientName(client_name);
            try {
                server.register(client);
                System.out.println("Registered with remote sever");

            } catch(RemoteException e) {
                System.err.println("Error registering with remote server");
            }

            //launch the White Board GUI and start drawing
            client.drawClientUI(server);
            //dont have permission access
//            if(!client.getPermission()) {
//                server.RemoveTargetUser(client.getClientName());
//
//            }
        } catch(ConnectException e) {
            System.err.println("Server is down or wrong IP address or Port number.");
        } catch(Exception e) {
            System.err.println("Please enter Valid IP and Port number sss.");
            e.printStackTrace();
        }

    }



}