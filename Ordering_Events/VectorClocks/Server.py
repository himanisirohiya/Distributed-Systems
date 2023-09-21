import socket
import os
from _thread import *

ServerSideSocket = socket.socket()
host = '127.0.0.1'
port = 3000
ThreadCount = 0
Iterator = 0
N = 5
clientMap = {}
queue = []

try:
    ServerSideSocket.bind((host, port))
except socket.error as e:
    print(str(e))

print('Socket is listening..')
ServerSideSocket.listen(N)

def multi_threaded_client(connection):
    global Iterator
    global clientMap
    global queue
    connection.send(str.encode('Server is working\n'))
    while True:
        data = connection.recv(2048)
        lines = data.decode('utf-8').split("\n")
        for line in lines:
            if( len(line) > 0 ):
                decodedData = line.split("\t")

                if decodedData[0] == "REG":
                    pid = decodedData[1]
                    clientMap[pid] = {
                        "pid": pid,
                        "connection": connection
                    }
                    response = 'Server connected: ' + data.decode('utf-8')
                    connection.send(str.encode(response))

                elif decodedData[0] == "MSG":
                    # broadcasting
                    # Then read sender_pid.
                    if( Iterator == 0 ):
                        for individualConnection in clientMap:
                            clientMap[ individualConnection ]["connection"].send( data )
                        Iterator = ThreadCount
                    else:
                        sender_pid = decodedData[1].split(".")[0]
                        queue.append( {
                            "data": data
                        })
                    
                
                elif decodedData[0] == "ACK":
                    Iterator -= 1
                    # broadcasting
                    for individualConnection in clientMap:
                        clientMap[ individualConnection ]["connection"].send( data )

                    if Iterator == 0 and len(queue) > 0:
                        qData = queue.pop(0)
                        for individualConnection in clientMap:
                            clientMap[ individualConnection ]["connection"].send( qData["data"] )
                        Iterator = ThreadCount

    connection.close()


while True:
    Client, address = ServerSideSocket.accept()
    print('Connected to: ' + address[0] + ':' + str(address[1]))
    start_new_thread(multi_threaded_client, (Client, ))
    ThreadCount += 1
    print('Thread Number: ' + str(ThreadCount))
ServerSideSocket.close()


# Ref: https://laptrinhx.com/python-multithreading-example-create-socket-server-with-multiple-clients-4022956390/#:~:text=Python%20Multithreading%20Example%3A%20Create%20Socket%20Server%20with%20Multiple,...%206%20Client-Side%20Multithreading%20Full%20Code%20Example%20