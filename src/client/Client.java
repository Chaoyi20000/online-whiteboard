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
public class Client extends UnicastRemoteObject implements IRemoteClient{
    private static final long serialVersionUID = 1L;
    static IRemoteServer server;
    private boolean isManager; //true if is manager
    private boolean havePermission; //permission granted by manager
    private JFrame frame;
    private DefaultListModel<String> userList;
    private DefaultListModel<String> chatList;
    private JButton clearBtn, saveBtn, saveAsBtn, openBtn;
    private JButton blackBtn, blueBtn, greenBtn, redBtn, orangeBtn, yellowBtn, cyanBtn,aoiBtn;
    private JButton brownBtn, pinkBtn,skyBtn, greyBtn, purpleBtn, limeBtn, darkgreyBtn, magentaBtn;
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
            LineBorder empty = new LineBorder(new Color(238, 238, 238), 1);
            LineBorder box = new LineBorder(Color.black, 1);
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

//            cur_Color = canvasUI.getCurrColor();
            mode = canvasUI.gerCurrMode();
            if (e.getSource() == blackBtn || e.getSource() == blueBtn || e.getSource() == greenBtn ||
                    e.getSource() == redBtn || e.getSource() == orangeBtn || e.getSource() == yellowBtn ||
                    e.getSource() == cyanBtn || e.getSource() == aoiBtn || e.getSource() == brownBtn ||
                    e.getSource() == pinkBtn || e.getSource() == skyBtn || e.getSource() == greyBtn ||
                    e.getSource() == purpleBtn || e.getSource() == limeBtn || e.getSource() == darkgreyBtn ||
                    e.getSource() == magentaBtn) {
                cur_Color = canvasUI.getCurrColor();
                LineBorder border1 = new LineBorder(cur_Color, 1);
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
    public void closeUI_Client() throws IOException {
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
                JOptionPane.showMessageDialog(frame, "you have been removed by the Administrator.\n" +
                                "The board will be closed.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
        t.start();
    }

    public void closeUIbyManager() throws IOException {
        //if kicked out or manager quit
        Thread t = new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(frame, "The manager has shut down the server.\n" +
                                "The board will be closed.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
        t.start();
    }


//
//    //// 剩余部分是来写UI board 的部分
    public void drawClientUI(IRemoteServer server) {

        ColorButton colorButton = new ColorButton();

        //build the GUI
        frame = new JFrame(clientName + "'s WhiteBoard");
        Container content = frame.getContentPane();

        canvasUI = new WhiteBoardArea(clientName, isManager,server);

        //关于颜色的button
        blackBtn = colorButton.ColorButton(Color.black);
        blackBtn.addActionListener(actionListener);

        blueBtn = colorButton.ColorButton(Color.blue);
        blueBtn.addActionListener(actionListener);

        greenBtn = colorButton.ColorButton(Color.green);
        greenBtn.addActionListener(actionListener);

        redBtn = colorButton.ColorButton(Color.red);
        redBtn.addActionListener(actionListener);

        orangeBtn = colorButton.ColorButton(Color.orange);
        orangeBtn.addActionListener(actionListener);

        yellowBtn = colorButton.ColorButton(Color.yellow);
        yellowBtn.addActionListener(actionListener);

        cyanBtn = colorButton.ColorButton(Color.cyan);
        cyanBtn.addActionListener(actionListener);

        brownBtn = colorButton.ColorButton(new Color(153, 76, 0));
        brownBtn.addActionListener(actionListener);

        pinkBtn = colorButton.ColorButton(new Color(255, 153, 204));
        pinkBtn.addActionListener(actionListener);

        greyBtn = colorButton.ColorButton(Color.gray);
        greyBtn.addActionListener(actionListener);

        purpleBtn = colorButton.ColorButton(new Color(102, 0, 204));
        purpleBtn.addActionListener(actionListener);

        limeBtn = colorButton.ColorButton(new Color(102, 102, 0));
        limeBtn.addActionListener(actionListener);

        darkgreyBtn = colorButton.ColorButton(Color.darkGray);
        darkgreyBtn.addActionListener(actionListener);

        magentaBtn = colorButton.ColorButton(Color.magenta);
        magentaBtn.addActionListener(actionListener);

        aoiBtn = colorButton.ColorButton(new Color(0, 102, 102));
        aoiBtn.addActionListener(actionListener);

        skyBtn = colorButton.ColorButton(new Color(0, 128, 255));
        skyBtn.addActionListener(actionListener);


        ToolButton tools = new ToolButton();
        IconAddress iconAddress = new IconAddress();


        //create six tools button
        LineBorder border = new LineBorder(Color.black, 1);
        drawBtn = tools.toolButton(iconAddress.pencil_tool, "Draw by pencil",actionListener);


        drawBtn.setBorder(border);


        border = new LineBorder(new Color(238,238,238), 1);

        lineBtn = tools.toolButton(iconAddress.line_tool,"Draw a line",actionListener);
        lineBtn.setBorder(border);
        rectBtn = tools.toolButton(iconAddress.rectangle_tool,"Draw a rectangle",actionListener);
        rectBtn.setBorder(border);
        circleBtn = tools.toolButton(iconAddress.cicle_tool,"Draw a cycle",actionListener);
        circleBtn.setBorder(border);
        triangleBtn = tools.toolButton(iconAddress.triangle_tool,"Draw a Triangle",actionListener);
        triangleBtn.setBorder(border);
        textBtn = tools.toolButton(iconAddress.text_tool,"Write in text box",actionListener);
        textBtn.setBorder(border);





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
//                                        updateClientList(server.getClientList());
                                        updateClientList(server.getClientList());
//                                        System.out.println(server.getClientList());
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
                System.out.println(c.getClientName());
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    //比较复杂，用来同步画板地操作

    //get the info sent from the other clients, and update the white board accordingly
    public void synchronizeBoard(IRemoteWhiteBoard syncBoard) throws RemoteException {

        // skip syncboard from itself
        if (syncBoard.getName().compareTo(clientName) == 0) {
            return ;
        }

        Mode_Shape mode_shape = new Mode_Shape();


        if (syncBoard.getState().equals("To start")) {
            //Let startPoint stores the start point of client x and wait for the next draw action
            startPoints.put(syncBoard.getName(), syncBoard.getPoint());
            return;
        }

        //start from the start point of client x
        Point startPt = (Point)startPoints.get(syncBoard.getName());

        //set canvas stroke color
        canvasUI.getGraphic().setPaint(syncBoard.getColor());
        cur_Color = canvasUI.getCurrColor();



        if (syncBoard.getState().equals("drawing")) {
            mode_shape.makeLine(startPt, syncBoard.getPoint());
            startPoints.put(syncBoard.getName(), syncBoard.getPoint());
            canvasUI.getGraphic().draw(mode_shape.getShape());
            canvasUI.repaint();
            canvasUI.getGraphic().setPaint(cur_Color);
            return;
        }


//        the mouse is released so we draw from start point to the broadcast point

        if (syncBoard.getState().equals("end drawing")) {
            if (syncBoard.getMode().equals("draw") || syncBoard.getMode().equals("line")) {
                mode_shape.makeLine(startPt, syncBoard.getPoint());
                System.out.println("sync successful on draw");
            } else if (syncBoard.getMode().equals("rectangle")) {
                mode_shape.makeRect(startPt, syncBoard.getPoint());
            } else if (syncBoard.getMode().equals("circle")) {
                mode_shape.makeCircle(startPt, syncBoard.getPoint());
            }  else if (syncBoard.getMode().equals("triangle")) {
                mode_shape.makeTriangle(startPt, syncBoard.getPoint());
            } else if (syncBoard.getMode().equals("text")) {
                canvasUI.getGraphic().setFont(new Font("TimesRoman", Font.PLAIN, 16));
                canvasUI.getGraphic().drawString(syncBoard.getText(), syncBoard.getPoint().x, syncBoard.getPoint().y);
//                System.out.println("client name is "+clientName);
//                System.out.println("Output " + (syncboard.getText()) +" " +(syncboard.getPoint().x)+ (syncboard.getPoint().y));

//                canvasUI.repaint();
            }





//            //draw shape if in shape mode: triangle, circle, rectangle
            if (!syncBoard.getMode().equals("text")) {
                try {
                    canvasUI.getGraphic().draw(mode_shape.getShape());
                    canvasUI.repaint();
                    //once finished drawing remove the start point of client x
                    startPoints.remove(syncBoard.getName());
                    return;
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            canvasUI.repaint();
            startPoints.remove(syncBoard.getName());

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
//            IClientManager clientManager =new ClientManager(this);

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

            //修改的。。。
//            Permission permission = new Permission();



            client.drawClientUI(server);
            //dont have permission access
            if(!(client.getPermission())) {
                server.RemoveTargetUser(client.getClientName());
            }
        } catch(ConnectException e) {
            System.err.println("Server is down or wrong IP address or Port number.");
        } catch(Exception e) {
            System.err.println("Please enter Valid IP and Port number sss.");
            e.printStackTrace();
        }

    }



}