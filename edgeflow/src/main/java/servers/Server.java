package servers;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import thrift.serverConnect;
import thrift.serverConnectImpl;

public class Server {
    public enum serverType{CCLayer, APLayer, EDLayer}
    private int ID;
    private serverType type;

    private String sourceIP;
    private int sourcePort;
    private String targetIP;
    private int targetPort;

    public void init()
    {
        sourceIP = "127.0.0.1";
        sourcePort = 8000;
        targetIP = "127.0.0.1";
        targetPort = 8000;
    }

    public int getID() {
        return ID;
    }

    private void setID(int ID) {
        this.ID = ID;
    }

    public void run()
    {
        try {

            TServerSocket serverTransport = new TServerSocket(sourcePort);
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(serverTransport);

            tArgs.protocolFactory(new TBinaryProtocol.Factory());

            TProcessor tprocessor = new serverConnect.Processor<serverConnect.Iface>(new serverConnectImpl());
            tArgs.processor(tprocessor);

            TServer server = new TThreadPoolServer(tArgs);
            server.serve();

        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    public void connect()
    {
        int TIMEOUT = 30000;
        try (TTransport transport = new TSocket(targetIP, targetPort, TIMEOUT)) {

            TProtocol protocol = new TBinaryProtocol(transport);

            serverConnect.Client client = new serverConnect.Client(protocol);

            transport.open();

            int result = client.connect("123");
            setID(result);
            System.out.println("Thrify client result =: " + getID());
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String data)
    {

    }
}
