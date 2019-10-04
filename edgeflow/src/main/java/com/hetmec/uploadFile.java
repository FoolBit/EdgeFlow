package com.hetmec;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import thrift.serverConnect;
import utils.FileUtils;

public class uploadFile {
    public static void main(String[] args)
    {
        String targetIP = args[0];
        int targetPort = Integer.parseInt(args[1]);
        String filename = args[2];
        int id = Integer.parseInt(args[3]);
        String data = FileUtils.readFile(filename);


        int TIMEOUT = 30000;
        try (TTransport transport = new TSocket(targetIP, targetPort, TIMEOUT)) {
            TProtocol protocol = new TBinaryProtocol(transport);
            serverConnect.Client client = new serverConnect.Client(protocol);
            transport.open();

            boolean result = client.uploadFile(data, id);
            System.out.println("Thrify client result =: " + result);
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
