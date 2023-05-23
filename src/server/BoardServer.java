package server;

import remote_interface.IRemoteServer;

import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BoardServer {
    public static void main(String[] args) {
        //Created a remote object server and bound it to the RMI registry
        // with port number 8888 and used the name "WhiteBoard" as the object's identifier in the registry
//        try{
            //server is skeleton object
//            IRemoteServer server = new RemoteBoardServant();
            //When a client needs to call a remote method, it can look up a reference to the remote object through the
            //Registry and use that reference to call the remote method.
            //crete a registry, then client use registry to access server
//            Registry registry = LocateRegistry.createRegistry(Integer.parseInt("8888")); // arg[0]
        if (args.length > 0) {
            try {
                int port = Integer.parseInt(args[0]);
                Registry registry = LocateRegistry.createRegistry(port);

                IRemoteServer server = new RemoteBoardServant();

                registry.bind("WhiteBoard", server);
                System.out.println("The WhiteBoard server is ready to start!!!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input of port number: " + args[0]);
            } catch (RemoteException e) {
                System.out.println("Error occurred while creating registry: " + e.getMessage());
            } catch (AlreadyBoundException e) {
                System.out.println("Error about already bind the server" + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error occurred in server " + e.getMessage());
            }
        }else{
            System.out.println("missing the input args");
        }

//        } catch (Exception e) {
//        	System.out.println("ERROR happened. Server not running");
//        }
//        try {
//            IRemoteServer server = new RemoteBoardServant();
//            LocateRegistry.createRegistry(8888);
//            Naming.rebind("WhiteBoard", server);
//            System.out.println("WhiteBoard server is ready");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("ERROR happened. Server not running");
//        }
    }
}
