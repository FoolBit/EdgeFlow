package com.hetmec;

import org.apache.thrift.TException;
import servers.Server;
import utils.DataUtils;
import utils.FileUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class EDLayer1 {
    private static Server server;
    public static void main(String[] args) throws IOException, InterruptedException, TException {

        System.out.println(System.getProperty("java.library.path"));
        //===================读取配置信息================
        String filename = "configED1.properties";
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
