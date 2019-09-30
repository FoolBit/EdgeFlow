package com.hetmec;

import servers.Server;

public class ClientLayer {
    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        server.connect();
        System.out.println(server.getID());
    }
}
