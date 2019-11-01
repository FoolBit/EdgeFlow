namespace java Thrift

service serverConnect{
    i32 connect(1:string server)
    bool sendCommand(1:i32 cmd 2:string arg)
    bool uploadFile(1:string data 2:i32 id)
    bool executeTask(1:string data)
}