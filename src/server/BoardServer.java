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
    }
}
