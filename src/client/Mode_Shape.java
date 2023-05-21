package client;


import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

//define and create the shape of drawing mode
public class Mode_Shape {
    private Shape shape;

    public Mode_Shape() {
        super();
    }

    public Shape getShape() {
        return shape;
    }

    public void makeLine(Point start, Point end) {
        shape = new Line2D.Double(start.x, start.y, end.x, end.y);
    }

    public void makeOval(Point start, Point end){
        int x = Math.min(start.x, end.x);
        int y= Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Ellipse2D.Double(x, y, width, height);
    }

    public void eraser(Point start, Point end){
        int x = Math.min(start.x, end.x);
        int y= Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Rectangle2D.Double(x, y, width, height);
    }

    public void makeTrapezoid(Point start, Point end){
        int x1 = start.x;
        int y1 = start.y;
        int x2 = end.x;
        int y2 = start.y;
        int x3 = end.x - (end.x - start.x) / 4;
        int y3 = end.y;
        int x4 = start.x + (end.x - start.x) / 4;
        int y4 = end.y;

        int[] xPoints = {x1, x2, x3, x4};
        int[] yPoints = {y1, y2, y3, y4};
        shape = new Polygon(xPoints, yPoints, 4);

    }


    //draw Rectangle
    public void makeRect( Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y= Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Rectangle2D.Double(x, y, width, height);
    }

    //draw circle
    public void makeCircle( Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y= Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        shape = new Ellipse2D.Double(x, y, Math.max(width, height), Math.max(width, height));
    }

    //Make text
    public void makeText(Point start) {
        int x = start.x - 5;
        int y= start.y - 20;
        int width = 110;
        int height = 20;
        shape = new RoundRectangle2D.Double(x, y, width, height, 12, 12);
    }

    //Draw Triangle
    public void makeTriangle( Point start, Point end) {
        //store the start and end x and y
        int minx = Math.min(start.x, end.x);
        int min_y= Math.min(start.y, end.y);
        int max_x = Math.max(start.x, end.x);
        int maxy= Math.max(start.y, end.y);
        int [] x = {minx,(minx+max_x)/2,max_x};
        int [] y = {maxy,min_y,maxy};
        shape = new Polygon(x, y, 3);
        if(end.y < start.y) {
            int [] dy = {min_y,maxy,min_y};
            shape = new Polygon(x, dy, 3);
        }

    }


}
