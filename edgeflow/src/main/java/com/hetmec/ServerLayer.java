package com.hetmec;

import com.mathworks.toolbox.javabuilder.MWException;
import org.apache.thrift.TException;
import servers.Server;
import utils.DataUtils;
import utils.FileUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class ServerLayer {
    private static Server server;
    public static void main(String[] args) throws IOException, InterruptedException, TException, MWException {
        //===================读取配置信息================
        String filename = "config.properties";
        Properties properties = FileUtils.getConfig(filename);
        //===================end======================

        //================初始化================
        server = new Server();
        server.init(properties);
        DataUtils.init(properties);
        //===================end=================

        //================run================
        server.run(true);

        runMenu();
    }

    static void runMenu() throws TException, MWException, IOException {
        String menu = "1.Print state\n2.Compute Strategy\n3.Start Task\n4.Quit\n";
        Scanner sc = new Scanner(System.in);

        int choice;
        while(true){
            System.out.println(menu);
            choice = sc.nextInt();
            if(choice == 1){

            }
            else if(choice == 2){
                server.computeStrategy();
            }
            else if(choice == 3){
                server.sendStartCmd();
            }
            else if(choice == 4)
            {
                break;
            }
        }
    }
}
