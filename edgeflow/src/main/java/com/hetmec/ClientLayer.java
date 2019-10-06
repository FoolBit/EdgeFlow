package com.hetmec;

import org.apache.thrift.TException;
import servers.Server;

import java.io.IOException;

public class ClientLayer {
    private static Server server;
    public static void main(String[] args) throws IOException, InterruptedException, TException {
        String sourceIP = "127.0.0.1";
        int sourcePort = 8001;
        String targetIP = "127.0.0.1";
        int targetPort = 8000;
        String devName = "en0";

        server = new Server();
        server.init(sourceIP,sourcePort,targetIP,targetPort,devName);
        server.setType(Server.serverType.EDLayer);
        server.run(true);
        server.connect();
    }
}
