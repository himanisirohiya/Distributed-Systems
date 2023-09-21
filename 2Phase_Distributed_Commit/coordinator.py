import socket
from _thread import *
import time
import json
# Constants
MIN_NODES = 3
TIMEOUT = 10             # timeout in seconds
# Other contants
host = '127.0.0.1'
port = 3000

# Dev
ServerSideSocket = socket.socket()
clientMap = {}
timeoutFlag = True
votes = 0
currentState = "INIT"

try:
    ServerSideSocket.bind((host, port))
except socket.error as e:
    print(str(e))

print('START 2PC Coordinator')
ServerSideSocket.listen(MIN_NODES)

def log(msg):
    print(msg)

def broadcast(data):
    global clientMap
    for clientPid in clientMap:
        # if( clientMap[clientPid]["deleted"] == False ):
        clientMap[clientPid]["connection"].send( str.encode( data ) )

def nodeStart(connection):
    global clientMap
    global votes
    global timeoutFlag
    try:  
        while True:
            data = connection.recv(1024)
            lines = data.decode('utf-8').split("\n")
            for line in lines:
                if( len(line) > 0 ):
                    decodedData = line.split("\t")
                    if decodedData[0] == "CONNECT":
                        pid = decodedData[1]
                        clientMap[pid] = {
                            "pid": pid,
                            "connection": connection,
                            "deleted": False
                        }
                        connection.send(str.encode('CONNECTED\n'))
                        print("Should init now")
                    
                    if decodedData[0] == "VOTE_COMMIT":
                        votes += 1
                        if votes >= MIN_NODES:
                            timeoutFlag = False
                            log("GLOBAL_COMMIT")
                            broadcast('GLOBAL_COMMIT\n')
                    
                    if decodedData[0] == "VOTE_ABORT":
                        broadcast('GLOBAL_ABORT\n')

                    if decodedData[0] == "DISCONNECT":
                        pid = decodedData[1]
                        clientMap[pid]["deleted"] = True
    except:
        # print("Error occured")
        # ServerSideSocket.close()
        return


def timeoutCoordinator():
    time.sleep(TIMEOUT)
    if timeoutFlag == True:
        log("GLOBAL_ABORT")
        broadcast("GLOBAL_ABORT\n")

        
        # Closing all connections and then server
        for clientPid in clientMap:
            clientMap[ clientPid ]["connection"].close()
            
        time.sleep(5)
        # ServerSideSocket.close()


def startContributor():
    global currentState
    log( "Input any key to start the contributer" )
    input()
    start_new_thread(timeoutCoordinator, ())
    currentState = "VOTE_REQUEST"
    log("VOTE_REQUEST")
    # time.sleep(15)
    broadcast("VOTE_REQUEST\n")


start_new_thread(startContributor, ())

while True:
    Client, address = ServerSideSocket.accept()
    print('Connected to: ' + address[0] + ':' + str(address[1]))
    start_new_thread(nodeStart, (Client, ))


