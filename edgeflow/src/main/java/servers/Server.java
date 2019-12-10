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
import utils.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.*;

import com.mathworks.toolbox.javabuilder.*;
import MATLABOptimizer.*;

public class Server implements Serializable {
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

    //------------for AP layer
    private List<DataUtils.DataQueue> dataQueues;
    private List<DataUtils.DataQueue_> dataQueue_s;
    //--------------end-----------

    //-------------for CC layer
    private List<List<DataUtils.DataQueue>> dataQueses_CC;
    private List<List<DataUtils.DataQueue_>> dataQueue_s_CC;
    //=============end

    private List<Server> childs;
    private String sourceIP;
    private int sourcePort;
    private String targetIP;
    private int targetPort;
    private String devName;

    private int RUNNING_TASK = 0;
    private int UPDATE_STRATEGY = 1;

    //------------------------ Strategy params ----------------
    // 我偷懒了 这么写省事- -
    private double compressionRatio;

    // CC & AP params
    private double totalComputeResource;
    private double totalTransmitResource;

    // ED params
    private double generateSpeedED;
    private double computeCapacityED;
    private double divisionPercentageED;
    private double transmitSpeedED;
    private double computeCapacityAP;
    private double divisionPercentageAP;
    private double transmitSpeedAP;
    private double computeCapacityCC;

    public void setDivisionPercentageED(double divisionPercentageED) {
        this.divisionPercentageED = divisionPercentageED;
    }

    public void setTransmitSpeedED(double transmitSpeedED) {
        this.transmitSpeedED = transmitSpeedED;
    }

    public void setComputeCapacityAP(double computeCapacityAP) {
        this.computeCapacityAP = computeCapacityAP;
    }

    public void setDivisionPercentageAP(double divisionPercentageAP) {
        this.divisionPercentageAP = divisionPercentageAP;
    }

    public void setTransmitSpeedAP(double transmitSpeedAP) {
        this.transmitSpeedAP = transmitSpeedAP;
    }

    public void setComputeCapacityCC(double computeCapacityCC) {
        this.computeCapacityCC = computeCapacityCC;
    }
//-------------------------Strategy params end ---------------

    // 把参数存到一个list里，方便传到matlab里
    public List<Double> getStrategyParams()
    {
        List<Double> q = new ArrayList<>();
        if(type != serverType.EDLayer)
        {
            q.add(totalComputeResource);
            q.add(totalTransmitResource);
            return q;
        }
        q.add(generateSpeedED);
        q.add(computeCapacityED);
        q.add(divisionPercentageED);
        q.add(transmitSpeedED);
        q.add(computeCapacityAP);
        q.add(divisionPercentageAP);
        q.add(transmitSpeedAP);
        return q;
    }


    // 初始化，读取配置文件，设置参数
    public void init(Properties properties){
        ndata = 0;
        childs = new ArrayList<>(); // 存储子节点
        // 每个ed节点都要有一个对应的dataQueue，这样他们的存取数据之间不会冲突
        // 但是同一个ed的存和取之间会有等待关系
        dataQueues = new ArrayList<>();
        dataQueue_s = new ArrayList<>();
        dataQueses_CC = new ArrayList<>();
        dataQueue_s_CC = new ArrayList<>();
        this.sourceIP = (String)properties.get("sourceIP");
        this.sourcePort = Integer.parseInt((String)properties.get("sourcePort"));;
        this.targetIP = (String)properties.get("targetIP");;
        this.targetPort = Integer.parseInt((String)properties.get("targetPort"));;
        this.devName = (String)properties.get("devName");
        this.divisionPercentageED = Double.parseDouble((String)properties.get("divisionPercentageED"));;
        this.divisionPercentageAP = Double.parseDouble((String)properties.get("divisionPercentageAP"));;
        String t = (String)properties.get("serverType");
        if(t.equals("EDLayer"))
        {
            type = serverType.EDLayer;
        }
        else if(t.equals("APLayer"))
        {
            type = serverType.APLayer;
        }
        else
        {
            type = serverType.CCLayer;
        }

    }

    // 子节点连接进来时，把信息存下来，并创建一个数据队列
    public void addChild(String serverStr) throws IOException, ClassNotFoundException {
        Server serverObj = (Server)ObjectUtils.stringToObject(serverStr);

        if(type == serverType.APLayer)
        {
            DataUtils.DataQueue q = new DataUtils.DataQueue();
            DataUtils.DataQueue_ q_ = new DataUtils.DataQueue_(q);
            dataQueues.add(q);dataQueue_s.add(q_);
            Thread thread = new Thread(new DataUtils.ComputeThread(this, q));
            thread.start();
        }
        else // CC layer
        {
            // 要为ap的每个ed都创建一个数据队列
            for(DataUtils.DataQueue q:serverObj.dataQueues)
            {
                Thread thread = new Thread(new DataUtils.ComputeThread(this, q));
                thread.start();
            }
            dataQueses_CC.add(serverObj.dataQueues);
            dataQueue_s_CC.add(serverObj.dataQueue_s);
        }

        this.childs.add(serverObj);
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
            TServerSocket serverTransport = new TServerSocket(new InetSocketAddress(sourceIP,sourcePort));
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(serverTransport);

            tArgs.protocolFactory(new TBinaryProtocol.Factory());

            TProcessor tprocessor = new serverConnect.Processor<serverConnect.Iface>(new serverConnectImpl(this));
            tArgs.processor(tprocessor);

            TServer server = new TThreadPoolServer(tArgs);
            server.serve();

        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    // 写起来省事，不用每次都写这一段重复的部分了
    // 这部分是通过thrift建立连接的过程
    public class ServerConnectClient{
        TTransport transport;
        serverConnect.Client client;
        ServerConnectClient(String targetIP, int targetPort) throws TTransportException {
            int TIMEOUT = 30000;
            transport = new TSocket(targetIP, targetPort, TIMEOUT);
            TProtocol protocol = new TBinaryProtocol(transport);
            client = new serverConnect.Client(protocol);
            transport.open();
        }
        void close(){
            transport.close();
        }
    }

    // 连接上层节点，并设定自己的id值
    public void connect() throws TException, IOException {
        ServerConnectClient serverConnectClient = new ServerConnectClient(targetIP, targetPort);
        int result = serverConnectClient.client.connect(ObjectUtils.objectToString(this));
        setID(result);
        System.out.println("Thrift client result =: " + getID());
        serverConnectClient.close();
    }

    // 向自己的所有子节点发送指令 新建线程进行
    public void sendCommandToAllBackground(int cmd, String arg){
        Thread thread = new Thread(new ServerBackgroundRun(this, "sendCommandToAll", cmd, arg));
        thread.start();
    }

    public void sendCommandToAll(int cmd, String arg) throws TException {
        for(int i=0; i<childs.size(); ++i){
            sendCommand(cmd, arg, childs.get(i).sourceIP, childs.get(i).sourcePort);
        }
    }

    public void sendCommand(int cmd, String arg, String targetIP, int targetPort) throws TException {
        ServerConnectClient serverConnectClient = new ServerConnectClient(targetIP, targetPort);
        boolean result = serverConnectClient.client.sendCommand(cmd, arg);
        serverConnectClient.close();
        System.out.println("Send Command");
        outputDate();
    }

    // 通知ed开始执行任务
    // 同时把所需数据下传
    public void sendStartCmd() throws IOException {
        int cmd = RUNNING_TASK;
        String arg = ObjectUtils.objectToString(DataUtils.getTargetData());
        sendCommandToAllBackground(cmd, arg);
    }

    // 没用了
    public void uploadFileBackground(){
        Thread thread = new Thread(new ServerBackgroundRun(this, "uploadFile"));
        thread.start();
    }

    // 输出时间 debug用
    public void outputDate(){
        System.out.println(System.currentTimeMillis());
    }

    static ServerConnectClient uploadServerConnectClient;
    // 把处理好的数据上传
    public long uploadFile(String arg, int id) throws IOException, TException, ClassNotFoundException {
        if(type!=serverType.CCLayer && uploadServerConnectClient == null)
            uploadServerConnectClient = new ServerConnectClient(targetIP, targetPort);

        if(type == serverType.CCLayer)
        {
            DataUtils.ProcessedData data = (DataUtils.ProcessedData)ObjectUtils.stringToObject(arg);
            System.out.println(String.format("Receive data from ED: %d\nResult = %s", data.getId_ED(),data.getResult()));
            return 1;
        }

        uploadServerConnectClient.client.uploadFile(arg, id);
        return -1;
    }

    // 建立一个持续连接
    // 这里偷懒了，可以优化
    // 使用同一个client时，需要进行排队
    // 如果new一个新的client，可以并行
    static ServerConnectClient execServerConnectClient;
    public void executeTask(String arg) throws IOException, ClassNotFoundException, TException {
        if(execServerConnectClient == null)
            execServerConnectClient = new ServerConnectClient(targetIP, targetPort);

        DataUtils.Data data = (DataUtils.Data)ObjectUtils.stringToObject(arg);

        if(type == serverType.APLayer)
        {
            double rand = Math.random();
            if(rand > data.getDivisionPercentageAP()) // 需要继续上传计算
            {
                System.out.println("rand:"+rand);
                System.out.println("divisionPercentageAP:"+data.getDivisionPercentageAP());
                data.setId_AP(ID);
                execServerConnectClient.client.executeTask(ObjectUtils.objectToString(data));
                return;
            }
        }

        // 需要在本地计算了
        String dataFilename = String.format("%s/%s/data%d.jpg", DataUtils.dataDir, DataUtils.sourceDir, ndata++);
        DataUtils.saveData(dataFilename, data.getData());
        data.setData(dataFilename);

        int id = data.getId_ED();
        DataUtils.DataQueue_ q = null;

        if(type == serverType.CCLayer)
        {
            int id_AP = data.getId_AP();
            q = dataQueue_s_CC.get(id_AP).get(id);
        }
        else
        {
            q = dataQueue_s.get(id);
        }

        Thread putDataThread = new Thread(new DataUtils.PutDataThread(q, data));
        putDataThread.start();
    }

    // 主要的任务逻辑
    // 人脸识别任务
    public void faceRecognize() throws TException, IOException {
        DataUtils.DataQueue dataQueue = new DataUtils.DataQueue();
        DataUtils.DataQueue_ dataQueue_ = new DataUtils.DataQueue_(dataQueue);

        // A new thread that handle compute
        Thread computeThread = new Thread(new DataUtils.ComputeThread(this, dataQueue));
        computeThread.start();

        // This thread need to handle income data
        ServerConnectClient serverConnectClient = new ServerConnectClient(targetIP, targetPort);
        String dataFilename;
        DataUtils.initFileLists();
        while(true)
        {
            dataFilename = DataUtils.nextData();
            if(dataFilename == null)
                break;
            double rand = Math.random();
            if(rand<= divisionPercentageED)
            {
                // 本地算
                System.out.println("rand:"+rand);
                System.out.println("divisionPercentageED:"+divisionPercentageED);
                Thread putDataThread = new Thread(new DataUtils.PutDataThread(dataQueue_, new DataUtils.Data(ID, dataFilename, divisionPercentageAP)));
                putDataThread.start();
            }
            else
            {
                String data = FileUtils.readFile(dataFilename);
                DataUtils.Data dataObj = new DataUtils.Data(ID, data, divisionPercentageAP);
                dataObj.setComputeCapacityAP(computeCapacityAP);
                dataObj.setComputeCapacityCC(computeCapacityCC);
                serverConnectClient.client.executeTask(ObjectUtils. objectToString(dataObj));
            }

        }

    }


    // 通知ed开始执行任务
    public void runningTask(int cmd, String arg) throws IOException, ClassNotFoundException, TException {
        DataUtils.Data data = (DataUtils.Data)ObjectUtils.stringToObject(arg);
        String filename = DataUtils.dataDir+'/'+  DataUtils.targetDir+ '/'+DataUtils.targetFilename;
        DataUtils.saveData(filename, data.getData());
        System.out.println("filename;"+filename);

        if(type == serverType.APLayer)
        {
            sendCommandToAllBackground(cmd, arg);
        }
        else
        {
            faceRecognize();
        }

    }

    // 策略计算
    public void computeStrategy() throws MWException, IOException, TException {
        List<Double> paramsCC = getStrategyParams();
        List<Double> paramsAP = new ArrayList<>();
        List<Double> paramsED = new ArrayList<>();
        int nAP = 0;
        int nED = 0;

        for(Server AP: childs)
        {
            ++nAP;
            paramsAP.addAll(AP.getStrategyParams());
            paramsAP.add((double) AP.childs.size());

            for(Server ED: AP.childs)
            {
                ++nED;
                paramsED.addAll(ED.getStrategyParams());
            }
        }

        // 调用matlab
        MyOptimizer myOptimizer = new MyOptimizer();
        List<Double> result = Arrays.asList((Double[]) myOptimizer.Optimizer(nAP, nED, compressionRatio, paramsCC, paramsAP, paramsED));

        // 读取新策略
        int cnt = 0;
        for(Server AP:childs)
        {
            for(Server ED:AP.childs)
            {
                ED.setDivisionPercentageED(result.get(cnt++));
                ED.setTransmitSpeedED(result.get(cnt++));
                ED.setComputeCapacityAP(result.get(cnt++));
                ED.setDivisionPercentageAP(result.get(cnt++));
                ED.setTransmitSpeedAP(result.get(cnt++));
                ED.setComputeCapacityCC(result.get(cnt++));
            }
        }

        // 更新策略
        String strategy = ObjectUtils.objectToString(this);
        for(Server AP:childs)
        {
            ServerConnectClient serverConnectClient = new ServerConnectClient(AP.sourceIP, AP.sourcePort);
            serverConnectClient.client.sendCommand(UPDATE_STRATEGY, strategy);
            serverConnectClient.close();
        }
    }

    // 更新策略参数
    public void updateStrategy(int cmd, String arg) throws IOException, ClassNotFoundException, TException {
        Server upperLayer = (Server) ObjectUtils.stringToObject(arg);
        Server current = upperLayer.childs.get(ID);
        if(type == serverType.APLayer)
        {
            String strategy = ObjectUtils.objectToString(current);
            for(Server ED:childs)
            {
                ServerConnectClient serverConnectClient = new ServerConnectClient(ED.sourceIP, ED.sourcePort);
                serverConnectClient.client.sendCommand(UPDATE_STRATEGY, strategy);
                serverConnectClient.close();
            }
        }
        else
        {
            divisionPercentageED = current.divisionPercentageED;
            transmitSpeedED = current.transmitSpeedED;
            computeCapacityAP = current.computeCapacityAP;
            divisionPercentageAP = current.divisionPercentageAP;
            transmitSpeedAP = current.transmitSpeedAP;
        }
    }

    // 看看上层传过来的时什么指令
    public boolean handleCommand(int cmd, String arg) throws IOException, ClassNotFoundException, TException {
        if(cmd == UPDATE_STRATEGY)
        {
            updateStrategy(cmd, arg);

        }
        else if(cmd == RUNNING_TASK)
        {
            runningTask(cmd, arg);
        }

        return true;
    }

}

// Discard
// 还得改
class ServerBackgroundRun implements Runnable{
    private Server server;
    private String task;
    int cmd;
    String arg;
    ServerBackgroundRun(Server server, String task)
    {
        this.server = server;
        this.task = task;
    }
    ServerBackgroundRun(Server server, String task, int cmd, String arg)
    {
        this.server = server;
        this.task = task;
        this.cmd = cmd;
        this.arg = arg;
    }

    @Override
    public void run() {
        if(task.equals("run")){
            server.run();
        }
        else if(task.equals("sendCommandToAll")){
            try {
                server.sendCommandToAll(cmd, arg);
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
            } catch (InterruptedException | TTransportException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (TException e) {
                e.printStackTrace();
            }
        }
    }
}