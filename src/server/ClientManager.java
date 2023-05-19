package server;

import remote_interface.IRemoteClient;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ClientManager implements Iterable<IRemoteClient>{

    private Set<IRemoteClient> clientSet;
    ConcurrentHashMap<IRemoteClient, Boolean> clientMap= new ConcurrentHashMap<>();
    private boolean havePermission;

    // make the clientSet iterate
    @Override
    public Iterator<IRemoteClient> iterator() {
        return clientSet.iterator();
    }
    public ClientManager(RemoteBoardServant remoteBoardServant) {
        this.clientSet = Collections.newSetFromMap(clientMap);
    }

    //add each client into the set
    public void addClient(IRemoteClient client) {
        clientSet.add(client);
    }
    // getter
    public Set<IRemoteClient> getClientSet(){
        return clientSet;
    }

    // check the clientList empty or not
    public boolean isEmpty(){
        if(this.clientSet.size() == 0){
            return true;
        }
        return false;
    }

    // delete the client from the list
    public void deleteClient (IRemoteClient currentClient){
        this.clientSet.remove(currentClient);
    }




}
