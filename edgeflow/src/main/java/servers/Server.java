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
import utils.FileUtils;
import utils.SysUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {
    public Server() {
    }

    public enum serverType{CCLayer, APLayer, EDLayer}
    private int ndata;
    private int ID;

    public serverType getType() {
        return type;
    }

    public void setType(serverType type) {
        this.type = type;
    }

    private serverType type;

    private List<String> childIP;
    private List<Integer> childPort;
    private String sourceIP;
    private int sourcePort;
    private String targetIP;
    private int targetPort;
    private String devName;

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public void init(String sourceIP, int sourcePort, String targetIP, int targetPort, String devName) throws IOException, InterruptedException {
        ndata = 0;
        childIP = new ArrayList<>();
        childPort = new ArrayList<>();
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.targetIP = targetIP;
        this.targetPort = targetPort;
        this.devName = devName;

        SysUtils.initTcQueue(devName);
    }

    public void addChild(String childIP, int childPort)
    {
        this.childIP.add(childIP);
        this.childPort.add(childPort);
    }

    public int getID() {
        return ID;
    }

    private void setID(int ID) {
        this.ID = ID;
    }

    public void run(boolean background)
    {
        if(background){
            Thread thread = new Thread(new ServerBackgroundRun(this, "run"));
            thread.start();
        }
        else{
            run();
        }
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

    public class ServerConnectClient{
        TTransport transport;
        serverConnect.Client client;
        ServerConnectClient(String targetIP, int targetPort) throws TTransportException {
            int TIMEOUT = 30000;
            transport = new TSocket(targetIP, targetPort, TIMEOUT);
            TProtocol protocol = new TBinaryProtocol(transport);
            serverConnect.Client client = new serverConnect.Client(protocol);
            transport.open();
        }
        void close(){
            transport.close();
        }
    }

    public void connect() throws TException {
        ServerConnectClient serverConnectClient = new ServerConnectClient(targetIP, targetPort);

        int result = serverConnectClient.client.connect(ID, sourceIP, sourcePort);
        setID(result);
        System.out.println("Thrify client result =: " + getID());

        serverConnectClient.close();
    }

    public void sendCommandToAllBackground(){
        Thread thread = new Thread(new ServerBackgroundRun(this, "sendCommandToAll"));
        thread.start();
    }

    public void sendCommandToAll() throws TException {
        for(int i=0; i<childIP.size(); ++i){
            sendCommand(childIP.get(i), childPort.get(i));
        }
    }

    public void sendCommand(String targetIP, int targetPort) throws TException {
        ServerConnectClient serverConnectClient = new ServerConnectClient(targetIP, targetPort);
        boolean result = serverConnectClient.client.sendCommand("1");
        serverConnectClient.close();
        System.out.println("Send Command");
        outputData();
    }

    public void uploadFileBackground(){
        Thread thread = new Thread(new ServerBackgroundRun(this, "uploadFile"));
        thread.start();
    }

    public void outputData(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()));
    }

    public long uploadFile(String data, int id) throws IOException, InterruptedException {
        String filename = null;

        if(type != serverType.EDLayer){
            System.out.println("Receive data from " + id);
            outputData();
            filename  = String.format("/home/resources/mydata%d.data", ndata++);
            FileUtils.writeFile(filename, data);
            System.out.println("Save data");
        }

        if(type != serverType.CCLayer){
            String jarPath = FileUtils.getFullFilename("uploadFile.jar");
            String cmd = String.format("java -jar %s %s %d %s %d",jarPath, targetIP, targetPort, filename, id);
            System.out.println("Send data");
            return SysUtils.execCmd(cmd);
        }

        return -1;
    }
}

class ServerBackgroundRun implements Runnable{
    private Server server;
    private String task;
    ServerBackgroundRun(Server server, String task)
    {
        this.server = server;
        this.task = task;
    }

    @Override
    public void run() {
        if(task.equals("run")){
            server.run();
        }
        else if(task.equals("sendCommandToAll")){
            try {
                server.sendCommandToAll();
            } catch (TException e) {
                e.printStackTrace();
            }
        }
        else if(task.equals("uploadFile")){ // only ED layer
            String filename = FileUtils.getFullFilename("test.txt");
            String data = FileUtils.readFile(filename);
            try {
                long pid = server.uploadFile(data, server.getID());
                SysUtils.limitSpeedByPID(pid, server.getID());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}