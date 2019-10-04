namespace java Thrift

service serverConnect{
    i32 connect(1:i32 id 2:string sourceIP 3:i32 sourcePort)
    bool sendCommand(1:string cmd)
    bool uploadFile(1:string data 2:i32 id)
}