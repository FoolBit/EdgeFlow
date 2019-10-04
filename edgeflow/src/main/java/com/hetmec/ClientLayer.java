package com.hetmec;

import servers.Server;

import java.io.IOException;

public class ClientLayer {
    private static Server server;
    public static void main(String[] args) throws IOException, InterruptedException {
        String sourceIP = "127.0.0.1";
        int sourcePort = 8000;
        String targetIP = "127.0.0.1";
        int targetPort = 8001;
        String devName = "en0";

        server = new Server();
        server.init(sourceIP,sourcePort,targetIP,targetPort,devName);
        server.run(true);
    }
}
