package client;

import remote_interface.IRemoteWhiteBoard;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Message extends UnicastRemoteObject implements IRemoteWhiteBoard {
    private static final long serialVersionUID = 1L;
    private String drawState;
    private String clientName;
    private String mode;
    private Color color;
    private Point point;
    private String text;

    public Message(String state, String name, String mode, Color color, Point pt, String text) throws RemoteException {
        this.drawState = state;
        this.clientName = name;
        this.mode = mode;
        this.color = color;
        this.point = pt;
        this.text = text;
    }

    @Override
    public String WhiteBoardName() throws RemoteException {
        return this.clientName;
    }

    @Override
    public String WhiteboardText() throws RemoteException {
        System.out.println("text is "+this.text);
        return this.text;
    }

    @Override
    public String WhiteBoardMode() throws RemoteException {
        return this.mode;
    }

    @Override
    public Color WhiteBoardColor() throws RemoteException {
        return this.color;
    }

    @Override
    public Point WhiteBoardPoint() throws RemoteException {
        return this.point;
    }
    @Override
    public String WhiteBoardState() throws RemoteException {
        return this.drawState;
    }


}
