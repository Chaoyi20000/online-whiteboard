package remote_interface;


import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

//those methods for implementation in Client side
//the server side can use those methods、

//The methods defined by the interface are called by the client and allow the server side to perform the corresponding operations
public interface IRemoteClient extends Remote {

// draw the UI of Client side
    public void drawClientUI(IRemoteServer server) throws RemoteException;
//// update the user list in UI
    public void updateClientList(Set<IRemoteClient> clientSet) throws RemoteException;
// synchronize the whiteboard
    public void synchronizeBoard (IRemoteWhiteBoard syncBoard) throws RemoteException;

//Getter
    public String getClientName() throws RemoteException;
//Setter
    public void setClientName(String name) throws RemoteException;

// assign a client manager to manage all the client
    public void assignClientManager() throws RemoteException;
// get this manager
    public boolean getManager() throws RemoteException;

// clear the current board
    public void clearBoard() throws RemoteException;

// send the board to server
    public byte[] sendBoard() throws RemoteException, IOException;
// draw the graph on open board
    public void drawOpenBoard(byte[] targetBoard) throws IOException;


// update the chat
    public void updateChat(String msg) throws RemoteException;


// client Manager function part
    public boolean requestPermission(String name) throws IOException;
    public void setPermission(boolean permission) throws IOException;
    public boolean getPermission() throws IOException;

// shutdown UI
    public void closeUI() throws IOException;










}
