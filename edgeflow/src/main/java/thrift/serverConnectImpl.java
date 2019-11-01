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
        BackgroundThread backgroundThread = new BackgroundThread(server, BackgroundThread.CONNECT);
        backgroundThread.setServerStr(serverStr);
        Thread thread = new Thread(backgroundThread);
        thread.start();

        System.out.println("Connected to" + n);
        return n++;
    }

    @Override
    public boolean sendCommand(int cmd, String arg) {
        BackgroundThread backgroundThread = new BackgroundThread(server, BackgroundThread.HANDLE_COMMAND);
        backgroundThread.setCmd(cmd);
        backgroundThread.setArg(arg);
        Thread thread = new Thread(backgroundThread);
        thread.start();

        return true;
    }

    @Override
    public boolean uploadFile(String arg, int id) {
        BackgroundThread backgroundThread = new BackgroundThread(server, BackgroundThread.UPLOAD);
        backgroundThread.setArg(arg);
        Thread thread = new Thread(backgroundThread);
        thread.start();
        return true;
    }

    @Override
    public boolean executeTask(String data) throws TException {
        BackgroundThread backgroundThread = new BackgroundThread(server, BackgroundThread.EXECUTE_TASK);
        backgroundThread.setArg(data);
        Thread thread = new Thread(backgroundThread);
        thread.start();
        return false;
    }
}

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