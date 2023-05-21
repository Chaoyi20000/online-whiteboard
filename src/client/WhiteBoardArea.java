package client;

import remote_interface.IRemoteServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;

public class WhiteBoardArea extends JComponent {
    private static final long serialVersionUID = 1L;
    private String clientName;
    private boolean isManager;
    private BufferedImage image;
    private BufferedImage prev_Image;
    private Graphics2D graph_2;

    private Point startPoint, nextPoint;
    private Color currColor ;
    private String currMode;
    private String text;
    private IRemoteServer server;
    private String fileName, filepath;


    public WhiteBoardArea(String name, boolean isManager, IRemoteServer RemoteInterface) {
        this.clientName = name;
        this.server = RemoteInterface;
        this.isManager = isManager;
        this.currColor = Color.black;
        this.currMode = "draw"; // default mode
        this.text = "";



        setDoubleBuffered(false);
        //when the mouse pressed, the coordinate of the mouse pointer is stored as the start point of the current action
        //send the mouse "start" coordinate and info to the server
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.out.println(clientName + " started drawing");
                startPoint = e.getPoint();
                saveCanvas();

                try {
                    Message message = new Message("To start", clientName, currMode, currColor, startPoint, text);
                    server.broadcastBoard(message);
                    System.out.println("current mode " + currMode);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });


        //listen to the mouse motion on the white board.
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                //System.out.println(clientName + " is drawing");
                nextPoint = e.getPoint();
                Mode_Shape shape = new Mode_Shape();

                if (graph_2 != null) {
                    if (currMode.compareTo("draw") == 0) {
                        shape.makeLine(startPoint, nextPoint);
                        startPoint = nextPoint;
                        try {
                            Message message = new Message("drawing", clientName, currMode, currColor, nextPoint, "");
                            server.broadcastBoard(message);
//                            System.out.println("color is  111 "+currColor);

                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }else if(currMode.compareTo("eraser") == 0){
                        shape.makeLine(startPoint, nextPoint);
                        startPoint = nextPoint;
                        graph_2.setPaint(Color.white);
                        graph_2.setStroke(new BasicStroke(10.0f));
                        try {
                            Message message = new Message("drawing", clientName, currMode, Color.white, nextPoint, "");
                            server.broadcastBoard(message);

                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }

                    } else if (currMode.compareTo("line") == 0) {
                        draw_prev_image();
                        shape.makeLine(startPoint, nextPoint);
                    } else if(currMode.compareTo("oval") == 0){
                        draw_prev_image();
                        shape.makeOval(startPoint, nextPoint);
                    } else if (currMode.compareTo("rectangle") == 0) {
                        draw_prev_image();
                        shape.makeRect(startPoint, nextPoint);
                    } else if (currMode.compareTo("circle") == 0) {
                        draw_prev_image();
                        shape.makeCircle( startPoint, nextPoint);
                    } else if(currMode.compareTo("trapezoid") == 0){
                        draw_prev_image();
                        shape.makeTrapezoid( startPoint, nextPoint);
                    } else if (currMode.compareTo("triangle") == 0) {
                        draw_prev_image();
                        shape.makeTriangle(startPoint, nextPoint);
                    }else if (currMode.compareTo("text") == 0) {
                        draw_prev_image();
                        graph_2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                        graph_2.drawString("Enter text here", nextPoint.x, nextPoint.y);
                        shape.makeText( startPoint);
                        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, new float[]{3}, 0);
                        graph_2.setStroke(dashed);
                    }
                    // Display shapes when dragged by local clients, without sending to the server.

//                    graph_2.setPaint(currColor);




                    graph_2.draw(shape.getShape());
                    repaint();
                }
            }
        });


        //when mouse released can draw
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                ///修改 这里
                System.out.println(clientName + " has stopped drawing");
                nextPoint = e.getPoint();
                Mode_Shape shape = new Mode_Shape();

                if (graph_2 != null) {
                    if (currMode.compareTo("line") == 0) {
                        shape.makeLine(startPoint, nextPoint);

                    } else if(currMode.compareTo("oval")==0){
                        shape.makeOval(startPoint, nextPoint);

                    } else if (currMode.compareTo("rectangle") == 0) {
                        shape.makeRect(startPoint, nextPoint);

                    } else if (currMode.compareTo("circle") == 0) {
                        shape.makeCircle(startPoint, nextPoint);

                    } else if (currMode.compareTo("triangle") == 0) {
                        shape.makeTriangle(startPoint, nextPoint);

                    } else if(currMode.compareTo("trapezoid") == 0){
                        shape.makeTrapezoid( startPoint, nextPoint);

                    } else if (currMode.compareTo("text") == 0) {
                        text = JOptionPane.showInputDialog("What text you want to add?");
                        if (text == null)
                            text = "";
                        draw_prev_image();
                        graph_2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                        graph_2.drawString(text, nextPoint.x, nextPoint.y);
                        graph_2.setStroke(new BasicStroke(1.0f));

                    }if(currMode.compareTo("eraser")==0) {
                        try {
                            Message message = new Message("end drawing", clientName, currMode, Color.white, nextPoint, text);
                            server.broadcastBoard(message);

                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                        graph_2.setPaint(Color.white);
                        graph_2.setStroke(new BasicStroke(1.0f));
                        repaint();
                    }else{
                        try {
                            Message message = new Message("end drawing", clientName, currMode, currColor, nextPoint, text);
                            server.broadcastBoard(message);

                        } catch (RemoteException e1) {
                            System.out.println("End of drawing");
                        }
                        graph_2.setPaint(currColor);
                        repaint();
                    }



                }
            }
        });
    }

            //The method for painting the shape on the white board.
        // initialize the white board to synchronize with the manager's image when the client join the shared white board
    protected void paintComponent(Graphics graph) {


        if (image == null) {
            if (isManager) {
                image = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
                graph_2 = (Graphics2D) image.getGraphics();
                graph_2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                reset();
            } else {
                try {
                    byte[] rawImage = server.sendCurrentBoard();
                    image = ImageIO.read(new ByteArrayInputStream(rawImage));
                    graph_2 = (Graphics2D) image.getGraphics();
                    graph_2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    graph_2.setPaint(currColor);
                } catch (IOException e) {
                    System.err.println("Fail for receiving the image!");
                }
            }
        }
        graph.drawImage(image, 0, 0, null);
    }




    // 修改完善这里的代码





//
//
//



    private void draw_prev_image() {
        drawImage(prev_Image);
    }


    //make whole white board become empty
    public void reset() {
        // 绘制网格
        int gridSize = 20; // 网格的大小（单元格宽度和高度）
        graph_2.setColor(Color.GRAY); // 网格线的颜色

        // 清空画板并绘制网格
        graph_2.setColor(Color.WHITE); // 设置画板颜色为白色
        graph_2.fillRect(0, 0, getWidth(), getHeight()); // 填充整个画板为白色

        // 水平线
        for (int y = gridSize; y < getHeight(); y += gridSize) {
            graph_2.setColor(Color.GRAY); // 设置网格线的颜色
            graph_2.drawLine(0, y, getWidth(), y); // 绘制水平网格线
        }

        // 垂直线
        for (int x = gridSize; x < getWidth(); x += gridSize) {
            graph_2.setColor(Color.GRAY); // 设置网格线的颜色
            graph_2.drawLine(x, 0, x, getHeight()); // 绘制垂直网格线
        }

        graph_2.setPaint(currColor);
        setFilepath(null);
        setFileName(null);
        repaint();
    }

    public String getFileName(){
        return fileName;
    }

    public String getFilePath(){
        return filepath;
    }



    public void setFileName(String fileName){
        this.fileName=fileName;
    }

    public void setFilepath(String filepath){
        this.filepath=filepath;
    }



    public void draw() {
        currMode="draw";
    }

    public void line() {
        currMode = "line";
    }

    public void trapezoid(){
        currMode = "trapezoid";
    }

    public void rect() {
        currMode="rectangle";
    }
    public void oval(){
        currMode = "oval";
    }

    public void eraser(){
        currMode = "eraser";
    }

    public void circle() {
        currMode="circle";
    }

    public void triangle() {
        currMode="triangle";
    }


    public void text() {
        currMode="text";
    }


    public Color getCurrColor() {
        return this.currColor;
    }


    public void drawImage(BufferedImage image) {
        graph_2.drawImage(image,null,0,0);
        repaint();
    }


    public BufferedImage getCanvas() {
        saveCanvas();
        return prev_Image;
    }

    private void saveCanvas() {
        ColorModel colorModel = image.getColorModel();
        WritableRaster raster = image.copyData(null);
        prev_Image = new BufferedImage(colorModel,raster,false,null);
    }

    public Graphics2D getGraphic() {
        return graph_2;
    }



    public void setColor(Color color) {
        currColor = color;
        graph_2.setPaint(currColor);
    }




    public String gerCurrMode() {
        return currMode;
    }
}
