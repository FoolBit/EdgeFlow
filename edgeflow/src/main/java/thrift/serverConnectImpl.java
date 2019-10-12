package thrift;

import org.apache.thrift.TException;
import servers.Server;

import java.io.IOException;

public class serverConnectImpl implements serverConnect.Iface{
    private int n;
    private Server server;

    public serverConnectImpl(Server server)
    {
        n = 0;
        this.server = server;
    }

    @Override
    public int connect(String serverStr) throws IOException, ClassNotFoundException {
        server.addChild(serverStr);
        System.out.println("Connected to" + n);
        return n++;
    }

    @Override
    public boolean sendCommand(String cmd) throws TException {
        if(server.getType() == Server.serverType.APLayer){
            server.sendCommandToAllBackground();
        }
        else{
            server.uploadFileBackground();
        }
        return true;
    }

    @Override
    public boolean uploadFile(String data, int id) throws IOException, InterruptedException {
        long pid = server.uploadFile(data, id);
        //SysUtils.limitSpeedByPID(pid, id);
        return true;
    }
}
