package com.hetmec;

import servers.Server;

public class ServerLayer {
    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        server.run();
    }
}
