package remote_interface;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteWhiteBoard extends Remote {

    public String WhiteBoardName() throws RemoteException;
    public String WhiteboardText() throws RemoteException;
    public String WhiteBoardMode() throws RemoteException;
    public Color WhiteBoardColor() throws RemoteException;
    public Point WhiteBoardPoint() throws RemoteException;
    public String WhiteBoardState() throws RemoteException;


}
