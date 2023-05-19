package server;

import remote_interface.IRemoteClient;
import remote_interface.IRemoteServer;
import remote_interface.IRemoteWhiteBoard;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.Set;

//Servant receives requests from clients, processes them and sends back responses
//在 RemoteBoardServant 构造函数中创建了一个 ClientManager 实例，用于管理客户端。
//        实现了 register(IRemoteClient client) 方法，用于注册客户端。如果客户端列表为空，将该客户端指定为客户端管理器；如果客户端列表不为空，
//        客户端需要请求许可，只有被客户端管理器允许加入才能成功加入客户端列表。
//        实现了 getClientList() 方法，用于获取客户端列表。
//        实现了 broadcastBoard(IRemoteWhiteBoard syncBoard) 方法，用于向客户端广播白板内容。
//        实现了 clearBoard() 方法，用于清空白板。
//        实现了 copyCurrentBoard() 方法，用于获取当前白板内容。
//        实现了 sendOpenBoard(byte[] targetBoard) 方法，用于让其他客户端打开指定的白板内容。
//        实现了 updateList(String name) 方法，用于更新客户端列表，当一个客户端离开时调用。
//        实现了 RemoveTargetUser(String chosenUser) 方法，用于强制删除一个客户端。
//        实现了 RemoveAllUser() 方法，用于删除所有客户端。
//        实现了 updateChat(String msg) 方法，用于将聊天消息广播给所有客户端。
//        整个服务端主要由 RemoteBoardServant 和 ClientManager 两个类组成，其中 ClientManager 是用于管理客户端列表的类。
//        服务端的主要功能是接收来自客户端的请求，处理请求，并返回响应，同时广播白板内容和聊天消息给所有客户端。
public class RemoteBoardServant extends UnicastRemoteObject implements IRemoteServer {

    public ClientManager adm_client;
// server 用来广播 client 的话
    public RemoteBoardServant() throws RemoteException{
        this.adm_client =  new ClientManager(this);

    }

    @Override
    public void register(IRemoteClient client) throws RemoteException {
//        Permission permissions = new Permission();
         // first join user is client manager
        if(this.adm_client.isEmpty()){
            client.assignClientManager();
        }
        boolean permission = true;
        // allow the client to join
        for(IRemoteClient iterator: this.adm_client){
            if(iterator.getManager()){
                try{
                    permission=iterator.requestPermission(client.getClientName());
                } catch (IOException e) {
//改写这里error handle
                    System.out.println("Error about I/O: " + e.getMessage());
                }
            }
        }


        if(!permission ){
            try{
                client.setPermission(permission);
            } catch (IOException e) {
                //改写这里error handle
                System.out.println("Error about I/O: " + e.getMessage());
            }
        }

        // manager with @ symbol
        if(client.getManager()){
            client.setClientName("$" + client.getClientName());
        }
        //add each client to client manager
        adm_client.addClient(client);

        // update list from Client Manager
        for(IRemoteClient c: adm_client){
            c.updateClientList(getClientList());
        }

    }

    @Override
    public Set<IRemoteClient> getClientList() throws RemoteException {
        return this.adm_client.getClientSet();
    }

    //board cast
    @Override
    public void broadcastBoard(IRemoteWhiteBoard syncBoard) throws RemoteException {
        for(IRemoteClient iterator: adm_client){
            iterator.synchronizeBoard(syncBoard);
//            System.out.println("text recieve1111");
        }
    }

    //if administrator new a board
    @Override
    public void clearBoard() throws RemoteException {
        for(IRemoteClient iterator: adm_client){
            iterator.clearBoard();
        }
    }

    //用来找manager的画板
    @Override
    public byte[] sendCurrentBoard() throws IOException {
        byte[] currentBoard = null;
        for(IRemoteClient iterator: adm_client){
            if(iterator.getManager()){
                currentBoard = iterator.sendBoard();
            }

        }

        return currentBoard;
    }

    //由于是manager打开的board，那么剩余人需要draw

    @Override
    public void sendOpenBoard(byte[] targetBoard) throws IOException {
        for(IRemoteClient iterator: adm_client) {
            if(!iterator.getManager()){
                iterator.drawOpenBoard(targetBoard);
            }
        }
    }
    //close by user
    @Override
    public void RemoveSelf(String name) throws RemoteException {
        for(IRemoteClient iterator: adm_client){
//            System.out.println("iterator is : each user");

            if(Objects.equals(iterator.getClientName(), name)){
                adm_client.deleteClient(iterator);
                System.out.println("The user " + name +" leave the board" );
            }
        }for(IRemoteClient iterator: adm_client){
            iterator.updateClientList(getClientList());
        }
    }

    //called by the server to forcefully remove a client
    @Override
    public void RemoveTargetUser(String chosenUser) throws IOException {
        System.out.println("The user "+ chosenUser + "   is removed by server");
        for(IRemoteClient iterator: adm_client){

            if(Objects.equals(iterator.getClientName(), chosenUser)){
                try {

                    adm_client.deleteClient(iterator);
                    iterator.closeUI_Client();

                }catch (Exception e){
                    e.printStackTrace();
                }
//                adm_client.deleteClient(iterator);
//                iterator.closeUI();
            }
        }


    }

    // when all user removed
    @Override
    public void RemoveAllUser() throws IOException {
        System.out.println("The administrator terminate the Board");
        for(IRemoteClient iterator: adm_client) {
            adm_client.deleteClient(iterator);
            iterator.closeUIbyManager();
        }

    }

    @Override
    public void updateChat(String msg) throws RemoteException {
        for(IRemoteClient iterator: adm_client) {
            try{
                iterator.updateChat(msg);
            }catch (IOException e){
                //修改这里error
                System.out.println("Error about I/O: " + e.getMessage());
            }
        }

    }



}
