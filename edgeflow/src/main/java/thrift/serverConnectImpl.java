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

    // background是为了让每个任务都新建线程来执行
    // 因为每个节点有很多个子节点，若在同一个线程中执行任务等待太长
    // 用多线程可以同时给多个子节点发送信息

    @Override
    // 上层记录当前有多少子节点连接到自己
    // 子节点通过该函数连接上层节点，上层节点记录子节点的全部信息，并告诉子节点她对应的id是多少
    public int connect(String serverStr) throws IOException, ClassNotFoundException {
        BackgroundThread backgroundThread = new BackgroundThread(server, BackgroundThread.CONNECT);
        backgroundThread.setServerStr(serverStr);
        Thread thread = new Thread(backgroundThread);
        thread.start();

        System.out.println("Connected to" + n);
        return n++;
    }

    @Override
    // 把指令cmd和参数arg发送给所有子节点
    // handlecmd会根据cmd来执行不同任务
    public boolean sendCommand(int cmd, String arg) {
        BackgroundThread backgroundThread = new BackgroundThread(server, BackgroundThread.HANDLE_COMMAND);
        backgroundThread.setCmd(cmd);
        backgroundThread.setArg(arg);
        Thread thread = new Thread(backgroundThread);
        thread.start();

        return true;
    }

    @Override
    // 下层将处理完的数据发送给上层节点
    public boolean uploadFile(String arg, int id) {
        BackgroundThread backgroundThread = new BackgroundThread(server, BackgroundThread.UPLOAD);
        backgroundThread.setArg(arg);
        Thread thread = new Thread(backgroundThread);
        thread.start();
        return true;
    }

    @Override
    // 下层把原始数据发送给上层节点，让上层节点来执行任务
    public boolean executeTask(String data) throws TException {
        BackgroundThread backgroundThread = new BackgroundThread(server, BackgroundThread.EXECUTE_TASK);
        backgroundThread.setArg(data);
        Thread thread = new Thread(backgroundThread);
        thread.start();
        return false;
    }
}

// 这个类就是为了能够新建线程来执行不同的任务
class BackgroundThread implements Runnable
{
    private Server server;
    private String serverStr;
    private int funcType;

    private int cmd;
    private String arg;

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public static int CONNECT = 0;
    public static int HANDLE_COMMAND = 1;
    public static int EXECUTE_TASK = 2;
    public static int UPLOAD = 3;

    BackgroundThread(Server server, int funcType)
    {
        this.server = server;
        this.funcType = funcType;
    }

    public void setServerStr(String serverStr) {
        this.serverStr = serverStr;
    }

    @Override
    public void run() {
        if(funcType == CONNECT)
        {
            try {
                server.addChild(serverStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(funcType == HANDLE_COMMAND)
        {
            try {
                server. handleCommand(cmd, arg);
            } catch (IOException | TException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(funcType == EXECUTE_TASK)
        {
            try {
                server.executeTask(arg);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException | TException e) {
                e.printStackTrace();
            }
        }
        else if(funcType == UPLOAD)
        {
            try {
                server.uploadFile(arg, 0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}