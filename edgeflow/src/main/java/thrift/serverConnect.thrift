namespace java Thrift

service serverConnect{
    i32 connect(1:string server)
    bool sendCommand(1:string cmd)
    bool uploadFile(1:string data 2:i32 id)
}