package client;


import remote_interface.IRemoteClient;
import remote_interface.IRemoteServer;
import remote_interface.IRemoteWhiteBoard;
import server.ClientManager;


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
    private JFrame frame;
    static IRemoteServer server;
    private boolean isManager;

    private DefaultListModel<String> userList;
    private DefaultListModel<String> chatList;
    private JButton clearBtn, saveBtn, saveAsBtn, openBtn;
    private JButton blackBtn, blueBtn, yellowBtn, cyanBtn,aoiBtn,limeBtn, darkgreyBtn;
    private JButton brownBtn, pinkBtn,skyBtn, greyBtn, purpleBtn, magentaBtn;
    private JButton goldBtn, beigeBtn,greenBtn, redBtn, orangeBtn;
    private JButton drawBtn, lineBtn,ovalBtn, rectBtn, circleBtn, textBtn;
    private boolean havePermission;
    private JButton triangleBtn,trapezoidBtn;
    private JScrollPane msgArea;
    private JTextArea tellColor, displayColor;

    private JTextArea displayUsers;
    private JList<String> chat;
    private ArrayList<JButton> btnList;
    private WhiteBoardArea whiteboardAllUI;
    private String mode;

    private JLabel manager;

    private JTextField msgText;
    private JButton sendBtn;
    private JScrollPane currUsers;
    private String clientName; // store client's name
//    private String picName; // if saveAs then keep on saving on that location
//    private String picPath; // if saveAs then keep on saving on that location
    private Color cur_Color;
    private Hashtable<String, Point> startPoints = new Hashtable<String, Point>();


    protected Client() throws RemoteException {
        //一个client包含的field
        isManager = false;
        havePermission = true;
        userList = new DefaultListModel<>();
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
            AdvancedFeature advancedFeature = new AdvancedFeature(frame, whiteboardAllUI,server);


            // 四个基础按键操作
            if (e.getSource() == clearBtn) {
                whiteboardAllUI.reset();
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
                whiteboardAllUI.setColor(Color.black);
            } else if (e.getSource() == blueBtn) {
                whiteboardAllUI.setColor(Color.blue);
            } else if (e.getSource() == greenBtn) {
                whiteboardAllUI.setColor(Color.green);
            } else if (e.getSource() == redBtn) {
                whiteboardAllUI.setColor(Color.red);
            } else if (e.getSource() == orangeBtn) {
                whiteboardAllUI.setColor(Color.orange);
            } else if (e.getSource() == yellowBtn) {
                whiteboardAllUI.setColor(Color.yellow);
            } else if (e.getSource() == cyanBtn) {
                whiteboardAllUI.setColor(Color.cyan);
            } else if (e.getSource() == brownBtn) {
                whiteboardAllUI.setColor(new Color(165, 42, 42));
            } else if (e.getSource() == pinkBtn) {
                whiteboardAllUI.setColor(Color.pink);
            } else if (e.getSource() == greyBtn) {
                whiteboardAllUI.setColor(Color.gray);
            } else if (e.getSource() == purpleBtn) {
                whiteboardAllUI.setColor(new Color(128, 0, 128));
            } else if (e.getSource() == limeBtn) {
                whiteboardAllUI.setColor(new Color(102, 102, 0));
            }  else if (e.getSource() == beigeBtn) {
                whiteboardAllUI.setColor(new Color(245, 245, 220));
            } else if (e.getSource() == darkgreyBtn) {
                whiteboardAllUI.setColor(Color.darkGray);
            } else if (e.getSource() == goldBtn) {
                whiteboardAllUI.setColor(new Color(184, 134, 11)
                );
            } else if (e.getSource() == magentaBtn) {
                whiteboardAllUI.setColor(Color.magenta);
            } else if (e.getSource() == aoiBtn) {
                whiteboardAllUI.setColor(new Color(0, 128, 128));
            } else if (e.getSource() == skyBtn) {
                whiteboardAllUI.setColor(new Color(135, 206, 235));
            } else if (e.getSource() == drawBtn) {
                whiteboardAllUI.draw();
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
                whiteboardAllUI.line();
                for (JButton button : btnList) {
                    if (button == lineBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            }else if(e.getSource()==ovalBtn){
                whiteboardAllUI.oval();
                for (JButton button : btnList) {
                    if (button == ovalBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            }else if (e.getSource() == rectBtn) {
                whiteboardAllUI.rect();
                for (JButton button : btnList) {
                    if (button == rectBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == circleBtn) {
                whiteboardAllUI.circle();
                for (JButton button : btnList) {
                    if (button == circleBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == triangleBtn) {
                whiteboardAllUI.triangle();
                for (JButton button : btnList) {
                    if (button == triangleBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            } else if (e.getSource() == textBtn) {
                whiteboardAllUI.text();
                for (JButton button : btnList) {
                    if (button == textBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            }else if (e.getSource() == trapezoidBtn) {
                whiteboardAllUI.trapezoid();
                for (JButton button : btnList) {
                    if (button == trapezoidBtn) {
                        button.setBorder(box);
                    } else {
                        button.setBorder(empty);
                    }
                }
            }

            // 如果我们换了颜色， 我们display current color也需要换掉

            //颜色需要修改，还不够

//            cur_Color = whiteboardAllUI.getCurrColor();
            mode = whiteboardAllUI.gerCurrMode();
            if (e.getSource() == blackBtn || e.getSource() == blueBtn || e.getSource() == greenBtn ||
                    e.getSource() == redBtn || e.getSource() == orangeBtn || e.getSource() == yellowBtn ||
                    e.getSource() == cyanBtn || e.getSource()==beigeBtn || e.getSource() == aoiBtn || e.getSource() == brownBtn ||
                    e.getSource() == pinkBtn || e.getSource()==goldBtn || e.getSource() == skyBtn || e.getSource() == greyBtn ||
                    e.getSource() == purpleBtn || e.getSource() == limeBtn || e.getSource() == darkgreyBtn ||
                    e.getSource() == magentaBtn) {
                displayColor.setBackground(whiteboardAllUI.getCurrColor());
                LineBorder tool_border = new LineBorder(Color.black, 1);
                switch (mode) {
                    case "draw":
                        drawBtn.setBorder(tool_border);
                        break;
                    case "line":
                        lineBtn.setBorder(tool_border);
                        break;
                    case "oval":
                        ovalBtn.setBorder(tool_border);
                        break;
                    case "rectangle":
                        rectBtn.setBorder(tool_border);
                        break;
                    case "circle":
                        circleBtn.setBorder(tool_border);
                        break;
                    case "triangle":
                        triangleBtn.setBorder(tool_border);
                        break;
                    case "text":
                        textBtn.setBorder(tool_border);
                        break;
                    case"trapezoid":
                        trapezoidBtn.setBorder(tool_border);
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
    public void ToBeClientManager() throws RemoteException {
        this.isManager = true;

    }

    @Override
    public boolean CheckManager() throws RemoteException {
        return this.isManager;
    }

    @Override
    public void clearBoard() throws RemoteException {
        if (isManager == false) {
            this.whiteboardAllUI.reset();
        }

    }

    @Override
    public byte[] sendBoard() throws RemoteException, IOException {
        //把image变成byte 然后发送出去给each user
        ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
        //write the image to the imageArray
        ImageIO.write(this.whiteboardAllUI.getCanvas(), "png", imageArray);
        return imageArray.toByteArray();
    }

    @Override
    public void drawOpenBoard(byte[] targetBoard) throws IOException {
        System.out.println("error in drawOpenBoard111");
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(targetBoard));
        this.whiteboardAllUI.drawImage(image);
        System.out.println("error in drawOpenBoard2222");

    }

    @Override
    public void updateChat(String msg) throws RemoteException {
        this.chatList.addElement(msg);

    }

    @Override
    public boolean requestPermission(String name) throws IOException {
        if (JOptionPane.showConfirmDialog(frame,
                name + " wants to share your whiteboard."+ "Do you approve this user?", "Give a permission",
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
    public void drawUI(IRemoteServer server) {

        Color_Button colorButton = new Color_Button();

        //build the GUI
        frame = new JFrame( clientName +"'s WhiteBoard");
        Container content = frame.getContentPane();

        whiteboardAllUI = new WhiteBoardArea(clientName, isManager,server);

        //关于颜色的button
        blackBtn = colorButton.ColorButton(Color.black);
        blackBtn.addActionListener(actionListener);

        blueBtn = colorButton.ColorButton(Color.blue);
        blueBtn.addActionListener(actionListener);

        greenBtn = colorButton.ColorButton(Color.green);
        greenBtn.addActionListener(actionListener);

        redBtn = colorButton.ColorButton(Color.red);
        redBtn.addActionListener(actionListener);

        goldBtn=colorButton.ColorButton(new Color(184, 134, 11));
        goldBtn.addActionListener(actionListener);

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

        beigeBtn =colorButton.ColorButton(new Color(245, 245, 220));
        beigeBtn.addActionListener(actionListener);

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
        ovalBtn = tools.toolButton(iconAddress.oval_tool,"Draw a oval",actionListener);
        ovalBtn.setBorder(border);
        rectBtn = tools.toolButton(iconAddress.rectangle_tool,"Draw a rectangle",actionListener);
        rectBtn.setBorder(border);
        circleBtn = tools.toolButton(iconAddress.cicle_tool,"Draw a cycle",actionListener);
        circleBtn.setBorder(border);
        triangleBtn = tools.toolButton(iconAddress.triangle_tool,"Draw a Triangle",actionListener);
        triangleBtn.setBorder(border);
        textBtn = tools.toolButton(iconAddress.text_tool,"Write in text box",actionListener);
        textBtn.setBorder(border);
        trapezoidBtn=tools.toolButton(iconAddress.trapezoid_tool,"Draw a trapezoid",actionListener);
        trapezoidBtn.setBorder(border);






        btnList.add(drawBtn);
        btnList.add(lineBtn);
        btnList.add(ovalBtn);
        btnList.add(rectBtn);
        btnList.add(circleBtn);
        btnList.add(triangleBtn);
        btnList.add(textBtn);
        btnList.add(trapezoidBtn);

        // four advanced feature的button
        clearBtn = new JButton("New Board");
        clearBtn.setToolTipText("Create a new board");
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

        //about UI display current colour
        tellColor = new JTextArea("The chosen color is:");
        tellColor.setBackground(new Color(238,238,238));
        displayColor = new JTextArea("");
        displayColor.setBackground(Color.black);


        //about user list
        displayUsers = new JTextArea("Online users :");
        displayUsers.setBackground(new Color(238,238,238));





        //manager pic
        manager = new JLabel();
        ImageIcon icon = new ImageIcon(iconAddress.manager);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        manager.setIcon(scaledIcon);




        //manager advanced  control button

        if (isManager == false) {
            clearBtn.setVisible(false);
            saveBtn.setVisible(false);
            saveAsBtn.setVisible(false);
            openBtn.setVisible(false);
            manager.setVisible(false);
        }


        // get all the users

        JList<String> list = new JList<>(userList);
        currUsers = new JScrollPane(list);
        currUsers.setMinimumSize(new Dimension(90, 140));
        if(!isManager) {
            currUsers.setMinimumSize(new Dimension(90, 280));
        }
        //manager can kick someone here
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
                                int dialogResult = JOptionPane.showConfirmDialog (frame, "Do you want to kick  " + selectedName +  " off ?",
                                        "Warning", JOptionPane.YES_NO_OPTION);
                                if(dialogResult == JOptionPane.YES_OPTION) {
                                    try {
                                        ///////////////////bug
                                        server.RemoveTargetUser(selectedName);
                                        System.out.println("remove select user" + selectedName);
                                        updateCurrentUserList(server.getUserList());
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
        msgArea.setMinimumSize(new Dimension(90, 90));
        msgText = new JTextField();
        msgText.setMinimumSize(new Dimension(50, 50));
        sendBtn = new JButton("Send"); //addMouseListener here 直接call server 去broadcast message
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


        LayoutManager layoutManager =new LayoutManager(content);

        // set the minimum frame size
        frame.setMinimumSize(new Dimension(850, 700));

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
                            updateCurrentUserList(server.getUserList());
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


    public class LayoutManager {
        private GroupLayout layout;

        public LayoutManager(Container content) {
            layout = new GroupLayout(content);
            content.setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            setHorizontalPart();
            setVerticalPart();
            blackBtn.setPreferredSize(new Dimension(20, 20));

            layout.linkSize(SwingConstants.HORIZONTAL, clearBtn, saveBtn, saveAsBtn, openBtn);
        }
        public void setHorizontalPart() {
            layout.setHorizontalGroup(layout.createSequentialGroup()

                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                            .addComponent(drawBtn)
                            .addComponent(lineBtn)
                            .addComponent(ovalBtn)
                            .addComponent(rectBtn)
                            .addComponent(circleBtn)
                            .addComponent(triangleBtn)
                            .addComponent(textBtn)
                            .addComponent(trapezoidBtn)
                            )
                            .addGroup(layout.createParallelGroup(CENTER)
                                .addComponent(whiteboardAllUI)
                                .addComponent(msgArea)

                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(msgText)
                                    .addComponent(sendBtn)
                                )
                            )
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(blackBtn)
                                    .addComponent(yellowBtn)
                                    .addComponent(cyanBtn)
                                    .addComponent(aoiBtn)
                                    .addComponent(skyBtn)
                                    .addComponent(beigeBtn)

                            )
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(pinkBtn)
                                    .addComponent(redBtn)
                                    .addComponent(greenBtn)
                                    .addComponent(limeBtn)
                                    .addComponent(orangeBtn)
                                    .addComponent(goldBtn)

                            )
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(brownBtn)
                                    .addComponent(darkgreyBtn)
                                    .addComponent(magentaBtn)
                                    .addComponent(blueBtn)
                                    .addComponent(greyBtn)
                                    .addComponent(purpleBtn)
                            )


                    )
                    .addGroup(layout.createParallelGroup(CENTER)
                            .addGroup(layout.createParallelGroup(CENTER)
                                .addComponent(tellColor)
                                .addComponent(displayColor)
                                .addComponent(clearBtn)
                                .addComponent(openBtn)
                                .addComponent(saveBtn)
                                .addComponent(saveAsBtn)
                                .addComponent(displayUsers)
                                .addComponent(currUsers)
                            )

                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(manager)
                            )

                    )

            );
        }

        public void setVerticalPart(){
            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(BASELINE)
                                    .addComponent(drawBtn)
                                    .addComponent(lineBtn)
                                    .addComponent(ovalBtn)
                                    .addComponent(rectBtn)
                                    .addComponent(circleBtn)
                                    .addComponent(triangleBtn)
                                    .addComponent(textBtn)
                                    .addComponent(trapezoidBtn)
                            )
                    )
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(whiteboardAllUI)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(tellColor)
                                    .addComponent(displayColor)
                                    .addComponent(clearBtn)
                                    .addComponent(openBtn)
                                    .addComponent(saveBtn)
                                    .addComponent(saveAsBtn)
                                    .addComponent(displayUsers)
                                    .addComponent(currUsers)
                            )

                    )
                    .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(msgArea)
                            .addComponent(manager)
                            )
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
                                    .addComponent(aoiBtn)
                                    .addComponent(skyBtn)
                                    .addComponent(beigeBtn)

                            )
                            .addGroup(layout.createParallelGroup(BASELINE)

                                    .addComponent(pinkBtn)
                                    .addComponent(redBtn)
                                    .addComponent(greenBtn)
                                    .addComponent(limeBtn)
                                    .addComponent(orangeBtn)
                                    .addComponent(goldBtn)

                            )
                            .addGroup(layout.createParallelGroup(BASELINE)

                                    .addComponent(brownBtn)
                                    .addComponent(darkgreyBtn)
                                    .addComponent(magentaBtn)
                                    .addComponent(blueBtn)
                                    .addComponent(greyBtn)
                                    .addComponent(purpleBtn)
                            )

//                            .addGroup(layout.createParallelGroup(BASELINE)
//                                    .addComponent(currUsers)
//                            )

                    )
            );
        }
    }

    @Override
    public void updateCurrentUserList(Set<IRemoteClient> clientSet) throws RemoteException {
        //更新之前，先清空被地地user list
        this.userList.removeAllElements();
        for(IRemoteClient c: clientSet) {
            try {
                userList.addElement(c.getClientName());
                System.out.println("The new join user is :" + c.getClientName());
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
        if (syncBoard.WhiteBoardName().compareTo(clientName) == 0) {
            return ;
        }

        Mode_Shape mode_shape = new Mode_Shape();


        if (syncBoard.WhiteBoardState().equals("To start")) {
            startPoints.put(syncBoard.WhiteBoardName(), syncBoard.WhiteBoardPoint());
            return;
        }

        Point startPt = (Point)startPoints.get(syncBoard.WhiteBoardName());

        whiteboardAllUI.getGraphic().setPaint(syncBoard.WhiteBoardColor());
        cur_Color = whiteboardAllUI.getCurrColor();



        if (syncBoard.WhiteBoardState().equals("drawing")) {
            mode_shape.makeLine(startPt, syncBoard.WhiteBoardPoint());
            startPoints.put(syncBoard.WhiteBoardName(), syncBoard.WhiteBoardPoint());
            whiteboardAllUI.getGraphic().draw(mode_shape.getShape());
            whiteboardAllUI.repaint();
//            whiteboardAllUI.getGraphic().setPaint(cur_Color);
            return;
        }



        if (syncBoard.WhiteBoardState().equals("end drawing")) {
            if (syncBoard.WhiteBoardMode().equals("draw") || syncBoard.WhiteBoardMode().equals("line")) {
                mode_shape.makeLine(startPt, syncBoard.WhiteBoardPoint());
                System.out.println("sync successful on draw");
            }else if(syncBoard.WhiteBoardMode().equals("oval")){
                mode_shape.makeOval(startPt, syncBoard.WhiteBoardPoint());
            } else if (syncBoard.WhiteBoardMode().equals("rectangle")) {
                mode_shape.makeRect(startPt, syncBoard.WhiteBoardPoint());
            } else if (syncBoard.WhiteBoardMode().equals("circle")) {
                mode_shape.makeCircle(startPt, syncBoard.WhiteBoardPoint());
            } else if(syncBoard.WhiteBoardMode().equals("trapezoid")){
                mode_shape.makeTrapezoid(startPt, syncBoard.WhiteBoardPoint());
            } else if (syncBoard.WhiteBoardMode().equals("triangle")) {
                mode_shape.makeTriangle(startPt, syncBoard.WhiteBoardPoint());
            }else if (syncBoard.WhiteBoardMode().equals("text")) {
                whiteboardAllUI.getGraphic().setFont(new Font("TimesRoman", Font.PLAIN, 16));
                whiteboardAllUI.getGraphic().drawString(syncBoard.WhiteboardText(), syncBoard.WhiteBoardPoint().x, syncBoard.WhiteBoardPoint().y);
            }

//            //draw shape if in shape mode: triangle, circle, rectangle
            if (!syncBoard.WhiteBoardMode().equals("text")) {
                try {
                    whiteboardAllUI.getGraphic().draw(mode_shape.getShape());
                    whiteboardAllUI.repaint();
//                    startPoints.remove(syncBoard.getName());
                    return;
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            whiteboardAllUI.repaint();
            startPoints.remove(syncBoard.WhiteBoardName());

        }
    }





        public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, ServerNotActiveException {


        if(args.length != 2) {
            System.out.println("We need two args here");
            return;
        }

        try {

            if(!(args[0].equals("localhost") || args[0].equals("127.0.0.1"))) {
                System.out.println("Please enter localhost or 127.0.0.1 here");
                return;
            }
            String serverAddress = "//" + args[0]+":"+args[1] + "/WhiteBoard";


//            String hostname = "localhost";
//            String port = "8888";
//            String serverAddress ="//" + hostname+":"+port+ "/WhiteBoard";

            //Look up the Server from the RMI registry
            server = (IRemoteServer) Naming.lookup(serverAddress);
            IRemoteClient client = new Client();

            String client_name = "";


            boolean validName = false;

            while(!validName) {
                client_name = JOptionPane.showInputDialog("Please input your name:");
                if(client_name.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter your name!");
                }else {
                    validName = true;
                }
                for(IRemoteClient c : server.getUserList()) {
                    if(client_name.equals(c.getClientName()) || c.getClientName().equals("*"+client_name)) {
                        validName = false;
                        JOptionPane.showMessageDialog(null, "The name is taken by other users, input a different name please!");
                    }
                }
            }
            client.setClientName(client_name);
            try {

                server.register(client);

            } catch(RemoteException e) {
                System.err.println("Error registering with the remote server");
            }catch (Exception e){
                System.out.println("error in register part");
            }



            client.drawUI(server);
            if(!(client.getPermission())) {
                server.RemoveTargetUser(client.getClientName());
            }
        } catch(ConnectException e) {
            System.err.println("Server has connection error or wrong IP address or Port number.");
        }catch (NotBoundException e) {
            System.err.println("Remote object not found in the registry");
        } catch (MalformedURLException  e) {
            System.err.println("Malformed URL for server address is invalid");
        }catch(Exception e) {
            System.err.println("Please enter Valid IP and Port number.");
        }

    }



}