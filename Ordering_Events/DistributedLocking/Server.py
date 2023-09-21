import socket
import os
from _thread import *
import math

ServerSideSocket = socket.socket()
host = '127.0.0.1'
port = 3000
ThreadCount = 0
Iterator = 1
N = 5
clientMap = {}
queue = []
votes=0
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
    global resource
    connection.send(str.encode('Server is working\n'))
    while True:
        data = connection.recv(2048)
        lines = data.decode('utf-8').split("\n")
        for line in lines:
            if( len(line) > 0 ):
                # print(line)
                decodedData = line.split("\t")

                if decodedData[0] == "REG":
                    pid = decodedData[1]
                    # priority = decodedData[2]
                    clientMap[pid] = {
                        "pid": pid,
                        "connectionNumber": Iterator,
                        "connection": connection,
                        "inUse" : False
                    }
                    response = 'Server connected: ' + data.decode('utf-8') + "\n"
                    connection.send(str.encode(response))
                    connection.send(str.encode("connectionNumber\t" + str(Iterator) + "\n"))
                    data = "ThreadCount\t" + str(ThreadCount)+ "\n"
                    for individualConnection in clientMap:
                        clientMap[ individualConnection ]["connection"].send( str.encode(data) )
                    Iterator += 1
                    for requestEntry in queue:
                        connection.send( requestEntry )
                    

                elif decodedData[0] == "REQ":
                    # broadcasting
                    # resource = decodedData[1]
                    clientMap[pid]["inUse"] = True
                    for individualConnection in clientMap:
                        clientMap[ individualConnection ]["connection"].send( data )
                    queue.append(data)

                    
                elif decodedData[0] == "OK":
                    individualConnection = decodedData[1]
                    clientMap[ individualConnection ]["connection"].send( data )

                    

    connection.close()


while True:
    Client, address = ServerSideSocket.accept()
    print('Connected to: ' + address[0] + ':' + str(address[1]))
    start_new_thread(multi_threaded_client, (Client, ))
    ThreadCount += 1
    # print('Thread Number: ' + str(ThreadCount))
ServerSideSocket.close()


# Ref: https://laptrinhx.com/python-multithreading-example-create-socket-server-with-multiple-clients-4022956390/#:~:text=Python%20Multithreading%20Example%3A%20Create%20Socket%20Server%20with%20Multiple,...%206%20Client-Side%20Multithreading%20Full%20Code%20Example%20