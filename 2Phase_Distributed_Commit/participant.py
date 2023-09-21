import socket
from _thread import *
import time
import sys

# Constants
MIN_NODES = 5
TIMEOUT = 10         # timeout in seconds
# Other contants
host = '127.0.0.1'
port = 3000

# Dev
ClientMultiSocket = socket.socket()
clientMap = {}
timeoutFlag = True
# ClientMultiSocket.settimeout(TIMEOUT)
if len(sys.argv) != 2:
	raise ValueError('Process terminated. Please start the process using $python participant.py [pid]')

try:
    ClientMultiSocket.connect((host, port))
except socket.error as e:
    print(str(e))

pid = sys.argv[1]
print("PID=", pid)
def log(msg):
    print(msg)

def timeoutNode():
    global ClientMultiSocket
    time.sleep(TIMEOUT)
    if timeoutFlag == True:
        log("VOTE_ABORT")
        ClientMultiSocket.send(str.encode("VOTE_ABORT\t" + str(pid) + "\n"))
        if pid == '2':
            log("GLOBAL_ABORT")
        # ClientMultiSocket.send(str.encode("DISCONNECT\t" + str(pid) + "\n"))
        # Closing connection to the server
        # ClientMultiSocket.close()
        exit

def startNode():
    global ClientMultiSocket
    global pid
    global timeoutFlag
    try:
        print("CONNECT\t"+ str(pid) +"\n")
        ClientMultiSocket.send(str.encode("CONNECT\t"+ pid +"\n"))
        while True:
            res = ClientMultiSocket.recv(1024)
            if res:
                lines = res.decode('utf-8').split("\n")
                for line in lines:
                    if( len(line) > 0 ):
                        decodedData = line.split("\t")
                        if decodedData[0] == "CONNECTED":
                            log("INIT")
                        if decodedData[0] == "VOTE_REQUEST":
                            log("VOTE_REQUEST")
                            start_new_thread(timeoutNode, ())
                            if pid == '2':
                                time.sleep(11)
                            ClientMultiSocket.send(str.encode("VOTE_COMMIT\t" + str(pid) + "\n"))

                        if decodedData[0] == "GLOBAL_COMMIT":
                            timeoutFlag = False
                            log("GLOBAL_COMMIT")
                            
                        if decodedData[0] == "GLOBAL_ABORT":
                            log("GLOBAL_ABORT")
                            # ClientMultiSocket.send(str.encode("DISCONNECT\t" + str(pid) + "\n"))
                            ClientMultiSocket.close()
                            # exit
            else:
                log("VOTE_ABORT")
                ClientMultiSocket.send(str.encode("VOTE_ABORT\t" + str(pid) + "\n"))
    except:
        print("Error occured in network aborting")
        ClientMultiSocket.close()

startNode()

