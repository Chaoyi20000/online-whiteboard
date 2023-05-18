package remote_interface;

import java.io.IOException;
import java.rmi.*;
import java.util.Set;
//这里定义远程调用的方法
//IRemoteClient 接口定义的方法是被客户端调用，可以让服务器端执行相应的操作。也就是说，这些方法是由客户端发起调用，服务器端进行响应的。
//IRemoteServer 接口定义的方法是被服务器端调用，可以让客户端执行相应的操作。
//举个例子，如果服务器端需要更新所有客户端的聊天信息，它会调用 IRemoteClient 接口中的 updateChat 方法，
//以通知所有客户端更新他们的聊天信息。而如果一个客户端需要向服务器端注册自己，它会调用 IRemoteServer 接口中的 register 方法
//以告诉服务器它的存在。因此，这两个接口都是必需的，以便在客户端和服务器端之间建立远程通信。


//The methods defined by the interface are called by the server side and allow the client to perform the corresponding operations
public interface IRemoteServer extends Remote{
//登陆的client给register
	public void register(IRemoteClient client) throws RemoteException;
//	获取client list and unique
	public Set<IRemoteClient> getClientList() throws RemoteException;


//广播每一个whiteboard
	public void broadcastBoard(IRemoteWhiteBoard syncBoard) throws RemoteException;
//	refresh 所有white board
	public void clearBoard() throws RemoteException;



//	when new user join, send the copy to the new user
	public byte[] sendCurrentBoard()throws IOException;
//	when we open image, send the whiteboard to everyone
	public void sendOpenBoard(byte[] targetBoard) throws IOException;



//	update the user list when user quit, need broadcast
//	called by the client to disconnect itself,
	public void RemoveSelf(String name) throws RemoteException;
//	delete the chosen user from the current list
	//called by the server to forcefully remove a client
	public void RemoveTargetUser(String chosenUser) throws IOException;
//remove all the user after server close
	public void RemoveAllUser() throws IOException;

//当一个客户端发送消息时，该消息应该传递给其他客户端，
// 因此需要在服务器端实现updateChat()方法以将该消息转发给所有客户端。

// 同时，客户端需要在其自己的UI中显示其他客户端发送的消息，
// 因此也需要在客户端实现updateChat()方法以更新UI并显示接收到的消息。
// 因此，这两个接口都需要实现updateChat()方法来实现聊天功能。

//	update the chart information
	public void updateChat(String msg) throws RemoteException;


}
