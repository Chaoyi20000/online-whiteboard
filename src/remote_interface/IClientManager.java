package remote_interface;
//
import java.io.IOException;

public interface IClientManager {

    boolean requestPermission(String name) throws IOException;
    void setPermission(boolean permission) throws IOException;
    boolean getPermission() throws IOException;
}
