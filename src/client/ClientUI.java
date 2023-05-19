//package client;
//
//import remote_interface.IRemoteClient;
//import remote_interface.IRemoteServer;
//
//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.IOException;
//import java.rmi.RemoteException;
//import java.util.ArrayList;
//
//import static javax.swing.GroupLayout.Alignment.BASELINE;
//import static javax.swing.GroupLayout.Alignment.CENTER;
//
//public class ClientUI  {
//
//    static IRemoteServer server;
//
//    public IRemoteClient client;
//
//    private JFrame frame;
//
//    private WhiteBoardArea whiteBoardAreaUI;
//
//    private boolean administrator;
//
//    private JButton clearBtn, saveBtn, saveAsBtn, openBtn, blackBtn, blueBtn, greenBtn, redBtn, orangeBtn, yellowBtn, cyanBtn;
//    private JButton brownBtn, pinkBtn, greyBtn, purpleBtn, limeBtn, darkgreyBtn, magentaBtn, aoiBtn, skyBtn;
//    private JButton drawBtn, lineBtn, rectBtn, circleBtn, triangleBtn, textBtn;
//
//    private String drawMode;
//
//    private ArrayList<JButton> allBtnList;
//
//    private DefaultListModel<String> userList;
//
//    private JList<String> chat;
//
//    private DefaultListModel<String> chatList;
//
//    private String clientName;
//
//    private Color cur_Color;
//
//    private JTextArea textColor, showColor;
//
//    private JScrollPane msgArea;
//
//    private boolean access;
//
//
//
//    public ClientUI(IRemoteServer server, boolean is_adm,IRemoteClient client,DefaultListModel<String> userList,
//                    DefaultListModel<String> chatList,boolean access, ArrayList<JButton> allBtnList,
//                    String clientName,WhiteBoardArea whiteBoardArea) {
//        this.server =server;
//        this.administrator = is_adm;
//        this.client = client;
//        this.userList=userList;
//        this.chatList=chatList;
//        this.access= access;
//        this.allBtnList = allBtnList;
//        this.clientName=clientName;
//        this.whiteBoardAreaUI=whiteBoardArea;
//    }
//
//
//
//
//
//    ActionListener actionListener = new ActionListener() {
//
//
//        public void actionPerformed(ActionEvent e) {
//            // the chosen mode will be boxed with black border
//            LineBorder empty = new LineBorder(new Color(238, 238, 238), 2);
//            LineBorder box = new LineBorder(Color.black, 2);
//            AdvancedFeature advancedFeature = new AdvancedFeature(frame, whiteBoardAreaUI,server);
//
//
//            // 四个基础按键操作
//            if (e.getSource() == clearBtn) {
//                whiteBoardAreaUI.reset();
//                //如果你是manager,你要清空所有人画板
//                //需要修改 修改
//                if (administrator) {
//                    try {
//                        server.clearBoard();
//                    } catch (RemoteException ex) {
//                        JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit.");
//                    }
//                }
//            } else if (e.getSource() == openBtn) {
//                try {
//                    advancedFeature.open();
//                } catch (IOException ex) {
//                    System.err.println("Error opening file: " + ex.getMessage());
//                }
//            } else if (e.getSource() == saveBtn) {
//                try {
//                    advancedFeature.save();
//                } catch (IOException ex) {
//                    System.err.println("There is an IO error.");
//                }
//            } else if (e.getSource() == saveAsBtn) {
//                try {
//                    advancedFeature.saveAs();
//                } catch (IOException ex) {
//                    System.err.println("There is an IO error.");
//                }
//                //change color of canvas
//            }
//
//
//            // 颜色操作板块按钮
//
//
//            else if (e.getSource() == blackBtn) {
//                whiteBoardAreaUI.setColor(Color.black);
//            } else if (e.getSource() == blueBtn) {
//                whiteBoardAreaUI.setColor(Color.blue);
//            } else if (e.getSource() == greenBtn) {
//                whiteBoardAreaUI.setColor(Color.green);
//            } else if (e.getSource() == redBtn) {
//                whiteBoardAreaUI.setColor(Color.red);
//            } else if (e.getSource() == orangeBtn) {
//                whiteBoardAreaUI.setColor(Color.orange);
//            } else if (e.getSource() == yellowBtn) {
//                whiteBoardAreaUI.setColor(Color.yellow);
//            } else if (e.getSource() == cyanBtn) {
//                whiteBoardAreaUI.setColor(Color.cyan);
//            } else if (e.getSource() == brownBtn) {
//                whiteBoardAreaUI.setColor(new Color(165, 42, 42));
//            } else if (e.getSource() == pinkBtn) {
//                whiteBoardAreaUI.setColor(Color.pink);
//            } else if (e.getSource() == greyBtn) {
//                whiteBoardAreaUI.setColor(Color.gray);
//            } else if (e.getSource() == purpleBtn) {
//                whiteBoardAreaUI.setColor(new Color(128, 0, 128));
//            } else if (e.getSource() == limeBtn) {
//                whiteBoardAreaUI.setColor(Color.green);
//            } else if (e.getSource() == darkgreyBtn) {
//                whiteBoardAreaUI.setColor(Color.darkGray);
//            } else if (e.getSource() == magentaBtn) {
//                whiteBoardAreaUI.setColor(Color.magenta);
//            } else if (e.getSource() == aoiBtn) {
//                whiteBoardAreaUI.setColor(new Color(0, 128, 128));
//            } else if (e.getSource() == skyBtn) {
//                whiteBoardAreaUI.setColor(new Color(135, 206, 235));
//            } else if (e.getSource() == drawBtn) {
//                whiteBoardAreaUI.draw();
//                for (JButton button : allBtnList) {
//                    if (button == drawBtn) {
//                        button.setBorder(box);
//                    } else {
//                        button.setBorder(empty);
//                    }
//                }
//            }
//
//
//            //不同形状的graph
//            else if (e.getSource() == lineBtn) {
//                whiteBoardAreaUI.line();
//                for (JButton button : allBtnList) {
//                    if (button == lineBtn) {
//                        button.setBorder(box);
//                    } else {
//                        button.setBorder(empty);
//                    }
//                }
//            } else if (e.getSource() == rectBtn) {
//                whiteBoardAreaUI.rect();
//                for (JButton button : allBtnList) {
//                    if (button == rectBtn) {
//                        button.setBorder(box);
//                    } else {
//                        button.setBorder(empty);
//                    }
//                }
//            } else if (e.getSource() == circleBtn) {
//                whiteBoardAreaUI.circle();
//                for (JButton button : allBtnList) {
//                    if (button == circleBtn) {
//                        button.setBorder(box);
//                    } else {
//                        button.setBorder(empty);
//                    }
//                }
//            } else if (e.getSource() == triangleBtn) {
//                whiteBoardAreaUI.triangle();
//                for (JButton button : allBtnList) {
//                    if (button == triangleBtn) {
//                        button.setBorder(box);
//                    } else {
//                        button.setBorder(empty);
//                    }
//                }
//            } else if (e.getSource() == textBtn) {
//                whiteBoardAreaUI.text();
//                for (JButton button : allBtnList) {
//                    if (button == textBtn) {
//                        button.setBorder(box);
//                    } else {
//                        button.setBorder(empty);
//                    }
//                }
//            }
//
//            // 如果我们换了颜色， 我们display current color也需要换掉
//
//            //颜色需要修改，还不够
//
////            cur_Color = whiteBoardAreaUI.getCurrColor();
//            drawMode = whiteBoardAreaUI.gerCurrMode();
//            if (e.getSource() == blackBtn || e.getSource() == blueBtn || e.getSource() == greenBtn || e.getSource() == redBtn
//                    || e.getSource() == orangeBtn || e.getSource() == yellowBtn || e.getSource() == cyanBtn) {
//                cur_Color = whiteBoardAreaUI.getCurrColor();
//                LineBorder border1 = new LineBorder(cur_Color, 2);
//                showColor.setBackground(whiteBoardAreaUI.getCurrColor());
//                switch (drawMode) {
//                    case "draw":
//                        drawBtn.setBorder(border1);
//                        break;
//                    case "line":
//                        lineBtn.setBorder(border1);
//                        break;
//                    case "rectangle":
//                        rectBtn.setBorder(border1);
//                        break;
//                    case "circle":
//                        circleBtn.setBorder(border1);
//                        break;
//                    case "triangle":
//                        triangleBtn.setBorder(border1);
//                        break;
//                    case "text":
//                        textBtn.setBorder(border1);
//                        break;
//                    default :
//                        break;
//                }
//            }
//        }
//    };
//
//
//    public void drawClientUI(IRemoteServer server) {
//
//        //build the GUI
//        frame = new JFrame(clientName + "'s WhiteBoard");
//        Container content = frame.getContentPane();
//
//        whiteBoardAreaUI = new WhiteBoardArea(clientName, administrator,server);
//
//        //关于颜色的button
//        blackBtn = new JButton();
//        blackBtn.setBackground(Color.black);
//        blackBtn.setBorderPainted(false);
//        blackBtn.setOpaque(true);
//        blackBtn.addActionListener(actionListener);
//
//        blueBtn = new JButton();
//        blueBtn.setBackground(Color.blue);
//        blueBtn.setBorderPainted(false);
//        blueBtn.setOpaque(true);
//        blueBtn.addActionListener(actionListener);
//
//        greenBtn = new JButton();
//        greenBtn.setBackground(Color.green);
//        greenBtn.setBorderPainted(false);
//        greenBtn.setOpaque(true);
//        greenBtn.addActionListener(actionListener);
//
//        redBtn = new JButton();
//        redBtn.setBackground(Color.red);
//        redBtn.setBorderPainted(false);
//        redBtn.setOpaque(true);
//        redBtn.addActionListener(actionListener);
//
//        orangeBtn = new JButton();
//        orangeBtn.setBackground(Color.orange);
//        orangeBtn.setBorderPainted(false);
//        orangeBtn.setOpaque(true);
//        orangeBtn.addActionListener(actionListener);
//
//        yellowBtn = new JButton();
//        yellowBtn.setBackground(Color.yellow);
//        yellowBtn.setBorderPainted(false);
//        yellowBtn.setOpaque(true);
//        yellowBtn.addActionListener(actionListener);
//
//        cyanBtn = new JButton();
//        cyanBtn.setBackground(Color.cyan);
//        cyanBtn.setBorderPainted(false);
//        cyanBtn.setOpaque(true);
//        cyanBtn.addActionListener(actionListener);
//
//        brownBtn = new JButton();
//        brownBtn.setBackground(new Color(153,76,0));
//        brownBtn.setBorderPainted(false);
//        brownBtn.setOpaque(true);
//        brownBtn.addActionListener(actionListener);
//
//        pinkBtn = new JButton();
//        pinkBtn.setBackground(new Color(255,153,204));
//        pinkBtn.setBorderPainted(false);
//        pinkBtn.setOpaque(true);
//        pinkBtn.addActionListener(actionListener);
//
//        greyBtn = new JButton();
//        greyBtn.setBackground(Color.gray);
//        greyBtn.setBorderPainted(false);
//        greyBtn.setOpaque(true);
//        greyBtn.addActionListener(actionListener);
//
//        purpleBtn = new JButton();
//        purpleBtn.setBackground(new Color(102,0,204));
//        purpleBtn.setBorderPainted(false);
//        purpleBtn.setOpaque(true);
//        purpleBtn.addActionListener(actionListener);
//
//        limeBtn = new JButton();
//        limeBtn.setBackground(new Color(102,102,0));
//        limeBtn.setBorderPainted(false);
//        limeBtn.setOpaque(true);
//        limeBtn.addActionListener(actionListener);
//
//        darkgreyBtn = new JButton();
//        darkgreyBtn.setBackground(Color.darkGray);
//        darkgreyBtn.setBorderPainted(false);
//        darkgreyBtn.setOpaque(true);
//        darkgreyBtn.addActionListener(actionListener);
//
//        magentaBtn = new JButton();
//        magentaBtn.setBackground(Color.magenta);
//        magentaBtn.setBorderPainted(false);
//        magentaBtn.setOpaque(true);
//        magentaBtn.addActionListener(actionListener);
//
//        aoiBtn = new JButton();
//        aoiBtn.setBackground(new Color(0,102,102));
//        aoiBtn.setBorderPainted(false);
//        aoiBtn.setOpaque(true);
//        aoiBtn.addActionListener(actionListener);
//
//        skyBtn = new JButton();
//        skyBtn.setBackground(new Color(0,128,255));
//        skyBtn.setBorderPainted(false);
//        skyBtn.setOpaque(true);
//        skyBtn.addActionListener(actionListener);
//
//        int width = 25;  // 目标宽度
//        int height = 25; // 目标高度
//        //绘画mode的button 不同icon
//        LineBorder border = new LineBorder(Color.black, 2);
//        Icon icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/pen_icon.png");
//        System.out.println("icon test :" + icon.getIconHeight());
//        Image image = ((ImageIcon) icon).getImage();
//        // 调整图像大小
//        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        // 创建新的图标对象
//        Icon scaledIcon = new ImageIcon(scaledImage);
//        drawBtn = new JButton(scaledIcon);
//        //可以换一下tips
//        drawBtn.setToolTipText("Pencil draw");
//        drawBtn.setBorder(border);
//        drawBtn.addActionListener(actionListener);
//        border = new LineBorder(new Color(238,238,238), 2);
//
//        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/straight_line.png");
//        image = ((ImageIcon) icon).getImage();
//        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        scaledIcon = new ImageIcon(scaledImage);
//        lineBtn = new JButton(scaledIcon);
//        lineBtn.setToolTipText("Draw line");
//        lineBtn.setBorder(border);
//        lineBtn.addActionListener(actionListener);
//
//        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/rectangle.png");
//        image = ((ImageIcon) icon).getImage();
//        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        scaledIcon = new ImageIcon(scaledImage);
//
//        rectBtn = new JButton(scaledIcon);
//        rectBtn.setToolTipText("Draw rectangle");
//        rectBtn.setBorder(border);
//        rectBtn.addActionListener(actionListener);
//
//        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/circle.png");
//        image = ((ImageIcon) icon).getImage();
//        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        scaledIcon = new ImageIcon(scaledImage);
//        circleBtn = new JButton(scaledIcon);
//        circleBtn.setToolTipText("Draw circle");
//        circleBtn.setBorder(border);
//        circleBtn.addActionListener(actionListener);
//
//        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/triangle.png");
//        image = ((ImageIcon) icon).getImage();
//        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        scaledIcon = new ImageIcon(scaledImage);
//        triangleBtn = new JButton(scaledIcon);
//        triangleBtn.setToolTipText("Draw triangle");
//        triangleBtn.setBorder(border);
//        triangleBtn.addActionListener(actionListener);
//
//        icon = new ImageIcon("/Users/steven/Desktop/eclipse-workspace/WhiteBoard/src/icon/text_box.png");
//        image = ((ImageIcon) icon).getImage();
//        scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        scaledIcon = new ImageIcon(scaledImage);
//        textBtn = new JButton(scaledIcon);
//        textBtn.setToolTipText("write in text box");
//        textBtn.setBorder(border);
//        textBtn.addActionListener(actionListener);
//
//
//
//        allBtnList.add(drawBtn);
//        allBtnList.add(lineBtn);
//        allBtnList.add(rectBtn);
//        allBtnList.add(circleBtn);
//        allBtnList.add(triangleBtn);
//        allBtnList.add(textBtn);
////        allBtnList.add(eraserBtn);
//
//        // 四个advanced feature的button
//        clearBtn = new JButton("New Board");
//        clearBtn.setToolTipText("Create a new board");
//        //这样点击之后才能注册
//        clearBtn.addActionListener(actionListener);
//        saveBtn = new JButton("Save Image");
//        saveBtn.setToolTipText("Save as image file");
//        saveBtn.addActionListener(actionListener);
//        saveAsBtn = new JButton("Save as");
//        saveAsBtn.setToolTipText("Save image file");
//        saveAsBtn.addActionListener(actionListener);
//        openBtn = new JButton("Open Image");
//        openBtn.setToolTipText("Open an image file");
//        openBtn.addActionListener(actionListener);
//
//        //关于颜色
//        textColor = new JTextArea("The current color is:");
//        textColor.setBackground(new Color(238,238,238));
//        showColor = new JTextArea("");
//        showColor.setBackground(Color.black);
//
//        // if the client is the manager, he can save, open and clear the white board image
//        if (administrator == false) {
//            clearBtn.setVisible(false);
//            saveBtn.setVisible(false);
//            saveAsBtn.setVisible(false);
//            openBtn.setVisible(false);
//        }
//
//
//        ////////////////////修改
//        //list all other user
//
//        JList<String> list = new JList<>(userList);
//        JScrollPane currUsers = new JScrollPane(list);
//        currUsers.setMinimumSize(new Dimension(100, 150));
//        if(!administrator) {
//            currUsers.setMinimumSize(new Dimension(100, 290));
//        }
//        //有问题需要修改
//        //manager 可以踢人的功能
//        // if the client is the manager, he has the right to remove the client
//        if (administrator) {
//            list.addMouseListener(new MouseAdapter() {
//                public void mouseClicked(MouseEvent evt) {
//                    @SuppressWarnings("unchecked")
//                    JList<String> list = (JList<String>)evt.getSource();
//                    if (evt.getClickCount() == 2) {
//                        int index = list.locationToIndex(evt.getPoint());
//                        String selectedName = list.getModel().getElementAt(index);
//                        try {
//                            //manager can't remove him/herself
//                            if(! client.getClientName().equals(selectedName)) {
//                                int dialogResult = JOptionPane.showConfirmDialog (frame, "Are you sure to remove " + selectedName + "?",
//                                        "Warning", JOptionPane.YES_NO_OPTION);
//                                if(dialogResult == JOptionPane.YES_OPTION) {
//                                    try {
//                                        ///////////////////bug
//                                        server.RemoveTargetUser(selectedName);
//                                        System.out.println("remove select user" + selectedName);
//                                        client.updateClientList(server.getClientList());
//                                        System.out.println(server.getClientList());
//                                    } catch (IOException e) {
//                                        // TODO Auto-generated catch block
//                                        System.err.println("There is an IO error.");
//                                    }
//                                }
//                            }
//                        } catch (HeadlessException e) {
//                            // TODO Auto-generated catch block
//                            System.err.println("There is an headless error.");
//                        } catch (RemoteException e) {
//                            // TODO Auto-generated catch block
//                            System.err.println("There is an IO error.");
//                        }
//                    }
//                }
//            });
//        }
//
//
//
//
//
//
//
//
////         chat box 功能
//
//        chat = new JList<>(chatList);
//        msgArea = new JScrollPane(chat);
//        msgArea.setMinimumSize(new Dimension(100, 100));
//        JTextField msgText = new JTextField();
//        JButton sendBtn = new JButton("Send"); //addMouseListener here 直接call server 去broadcast message
//        sendBtn.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent evt) {
//                if(!msgText.getText().equals("")) {
//                    try {
//                        server.updateChat(clientName + ": "+ msgText.getText());
//                    } catch (RemoteException e) {
//                        throw new RuntimeException(e);
//                    }
//                    // let the scrollpane to always show the newest chat message
//                    SwingUtilities.invokeLater(() -> {
//                        JScrollBar vertical = msgArea.getVerticalScrollBar();
//                        vertical.setValue(vertical.getMaximum());
//                    });
//                    msgText.setText("");
//                }
//            }
//        });
//
//
//
//        // layout
//        GroupLayout layout = new GroupLayout(content);
//        content.setLayout(layout);
//        layout.setAutoCreateGaps(true);
//        layout.setAutoCreateContainerGaps(true);
////
////
////
////        ////修改bug
//        layout.setHorizontalGroup(layout.createSequentialGroup()
//                        .addGroup(layout.createParallelGroup(CENTER)
//                                        .addComponent(drawBtn)
//                                        ///修改bug
//
//                                        .addComponent(lineBtn)
//                                        .addComponent(rectBtn)
//                                        .addComponent(circleBtn)
//                                        .addComponent(triangleBtn)
//                                        .addComponent(textBtn)
////                        .addComponent(eraserBtn)
//                        )
//                        .addGroup(layout.createParallelGroup(CENTER)
//                                .addComponent(whiteBoardAreaUI)
//                                .addComponent(msgArea)
//                                .addGroup(layout.createSequentialGroup()
//
//                                        .addComponent(msgText)
//                                        .addComponent(sendBtn)
//                                )
//                                .addGroup(layout.createSequentialGroup()
//                                        .addComponent(blackBtn)
//                                        .addComponent(yellowBtn)
//                                        .addComponent(cyanBtn)
//                                        .addComponent(brownBtn)
//                                        .addComponent(greyBtn)
//                                        .addComponent(purpleBtn)
//                                        .addComponent(limeBtn)
//                                        .addComponent(orangeBtn)
//
//
//                                )
//                                .addGroup(layout.createSequentialGroup()
//                                        .addComponent(pinkBtn)
//                                        .addComponent(redBtn)
//                                        .addComponent(greenBtn)
//                                        .addComponent(blueBtn)
//                                        .addComponent(darkgreyBtn)
//                                        .addComponent(magentaBtn)
//                                        .addComponent(aoiBtn)
//                                        .addComponent(skyBtn)
//                                )
//
//                        )
//                        .addGroup(layout.createParallelGroup(CENTER)
//                                .addComponent(clearBtn)
//                                .addComponent(openBtn)
//                                .addComponent(saveBtn)
//                                .addComponent(saveAsBtn)
//                                .addComponent(currUsers)
//                                .addComponent(textColor)
//                                .addComponent(showColor)
//                        )
//        );
//
//        layout.setVerticalGroup(layout.createSequentialGroup()
//                .addGroup(layout.createParallelGroup(BASELINE)
//                        .addGroup(layout.createSequentialGroup()
//                                .addComponent(drawBtn)
//                                .addComponent(lineBtn)
//                                .addComponent(rectBtn)
//                                .addComponent(circleBtn)
//                                .addComponent(triangleBtn)
//                                .addComponent(textBtn)
//                        )
//                        .addComponent(whiteBoardAreaUI)
//                        .addGroup(layout.createSequentialGroup()
//                                .addComponent(clearBtn)
//                                .addComponent(openBtn)
//                                .addComponent(saveBtn)
//                                .addComponent(saveAsBtn)
//                                .addComponent(currUsers)
//                                .addComponent(textColor)
//                                .addComponent(showColor)
//                        )
//                )
//                .addGroup(layout.createSequentialGroup()
//                        .addComponent(msgArea)
//                        .addGroup(layout.createParallelGroup()
//                                .addComponent(msgText)
//                                .addComponent(sendBtn)
//                        )
//                )
//                .addGroup(layout.createSequentialGroup()
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                                .addComponent(blackBtn)
//                                .addComponent(yellowBtn)
//                                .addComponent(cyanBtn)
//                                .addComponent(brownBtn)
//                                .addComponent(greyBtn)
//                                .addComponent(purpleBtn)
//                                .addComponent(limeBtn)
//                                .addComponent(orangeBtn)
//
//                        )
//                        .addGroup(layout.createParallelGroup(BASELINE)
//
//                                .addComponent(pinkBtn)
//                                .addComponent(redBtn)
//                                .addComponent(greenBtn)
//                                .addComponent(blueBtn)
//                                .addComponent(darkgreyBtn)
//                                .addComponent(magentaBtn)
//                                .addComponent(aoiBtn)
//                                .addComponent(skyBtn)
//                        )
//                )
//        );
//        layout.linkSize(SwingConstants.HORIZONTAL, clearBtn, saveBtn, saveAsBtn, openBtn); //format to same size
//
//        // set the minimum framesize
//        if (administrator) frame.setMinimumSize(new Dimension(820, 600));
//        else frame.setMinimumSize(new Dimension(820, 600));
//
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        frame.setVisible(true);
//
//        //if the manager close the window, all other client are removed and the all clients' window are force closed
//
//        frame.addWindowListener(new java.awt.event.WindowAdapter() {
//            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//                if (administrator) {
//                    if (JOptionPane.showConfirmDialog(frame,
//                            "You are the manager? Are you sure close the application?", "Close Application?",
//                            JOptionPane.YES_NO_OPTION,
//                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
//                        try {
//                            server.RemoveAllUser();
//
//                        } catch (IOException e) {
//                            System.err.println("There is an IO error");
//                        } finally {
//                            System.exit(0);
//                        }
//                    }
//                } else {
//                    if (JOptionPane.showConfirmDialog(frame,
//                            "Are you sure you want to quit?", "Close Paint Board?",
//                            JOptionPane.YES_NO_OPTION,
//                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
//                        try {
//                            server.RemoveSelf(clientName);
//                            client.updateClientList(server.getClientList());
//                        } catch (RemoteException e) {
//                            JOptionPane.showMessageDialog(null, "Canvas server is down, please save and exit.");
//                        } finally {
//                            System.exit(0);
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//}
