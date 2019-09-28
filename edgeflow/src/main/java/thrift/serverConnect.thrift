namespace java Thrift

service serverConnect{
    bool connect(1:string server)
    bool sendCommand(1:string cmd)
}