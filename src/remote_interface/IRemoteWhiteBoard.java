package remote_interface;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteWhiteBoard extends Remote {

    public String getName() throws RemoteException;
    public String getMode() throws RemoteException;
    public String getText() throws RemoteException;
    public String getState() throws RemoteException;
    public Color getColor() throws RemoteException;
    public Point getPoint() throws RemoteException;

}
