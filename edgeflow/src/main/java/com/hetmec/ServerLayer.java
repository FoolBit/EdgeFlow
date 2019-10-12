package com.hetmec;

import servers.Server;

import java.io.IOException;
import java.util.Scanner;

public class ServerLayer {
    private static Server server;
    public static void main(String[] args) throws IOException, InterruptedException {
        String sourceIP = "192.168.1.100";
       // String sourceIP = "127.0.0.1";
        int sourcePort = 8000;
        String targetIP = "127.0.0.1";
        int targetPort = 8000;
        String devName = "wlp5s0";

        server = new Server();
        server.setType(Server.serverType.CCLayer);
        server.init(sourceIP,sourcePort,targetIP,targetPort,devName);
        server.run(true);

        runMenu();
    }

    static void runMenu()
    {
        String menu = "1.Print state\n2.Begin test\n3.Quit";
        Scanner sc = new Scanner(System.in);

        int choice;
        while(true){
            System.out.println(menu);
            choice = sc.nextInt();
            if(choice == 1){

            }
            else if(choice == 2){
                server.sendCommandToAllBackground();
            }
            else if(choice == 3){
                break;
            }
        }
    }
}
