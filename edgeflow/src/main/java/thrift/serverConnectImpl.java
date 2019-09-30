package thrift;

import org.apache.thrift.TException;
import servers.Server;

public class serverConnectImpl implements serverConnect.Iface{
    private int n;
    private Server server;

    public serverConnectImpl()
    {
        n = 0;
    }

    @Override
    public int connect(String server){
        // TODO
        ++n;
        System.out.println(n);
        return n;
    }

    @Override
    public boolean sendCommand(String cmd)
    {
        //TODO
        return true;
    }

    @Override
    public boolean uploadFile(String data){
        //TODO
        server.uploadFile(data);
        return true;
    }
}
