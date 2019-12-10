package com.hetmec;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import servers.Server;
import thrift.serverConnect;
import utils.DataUtils;
import utils.FileUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class APLayer {
    private static Server server;
    public static void main(String[] args) throws IOException, InterruptedException, TException {

        //===================读取配置信息================
        String filename = "configAP.properties";
        Properties properties = FileUtils.getConfig(filename);
        //===================end======================

        //================初始化================
        server = new Server();
        server.init(properties);
        DataUtils.init(properties);
        //================end=================

        //==================run=================
        server.run(true);
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            System.out.println("1.Connect");
            int choice = sc.nextInt();
            if(choice == 1)
                break;
        }
        server.connect();

    }
}
