import socket
import sys
import threading
import time
import os

# establish a scoket connection to share information with others
ClientMultiSocket = socket.socket()
host = '127.0.0.1'
port = 3000
inUse = False
queueData = []
votes=0 
threadCount=0
accessed=False
myConnectionNumber = None
dir_path = os.path.dirname(os.path.realpath(__file__))
resource = dir_path+"/counter.txt"
counter= 0
voters = {}

time.sleep(2)

# checks for the arguments passed while the file is run
if len(sys.argv) != 2:
	raise ValueError('Process terminated. Please start the process using $python Client.py [pid]')

print('Waiting for connection response')
# connect the clients to the socket which will open a stream for everyone to share data
try:
    ClientMultiSocket.connect((host, port))
except socket.error as e:
    print(str(e))

pid = sys.argv[1]
# priority = sys.argv[2]
# register the process that has joined the socket connection
regMsg = "REG\t" + str(pid) + "\n" 
ClientMultiSocket.send(regMsg.encode())

# thread to receive response from all the active processes
def queueReader(connection, pid):
    global votes
    global resource
    global counter
    global accessed
    while True:
        if len(queueData) > 0:
            line = queueData.pop(0)
            if len(line) > 0:
                
                # print(line)
                decodedData = line.split("\t")
                if decodedData[0] == "ThreadCount":
                    threadCount = int(decodedData[1])

                elif decodedData[0] == "connectionNumber":
                    myConnectionNumber = int(decodedData[1])
                    inputMsg = "REQ\t" + str(resource) + "\t" + str(pid) + "." + str(myConnectionNumber) + "\n"
                    connection.send(str.encode(inputMsg))
                    print("Requesting acces to shared resource")

                elif decodedData[0] == "REQ":
                    # eg: REQ File PID Clock
                    senderPID = int(decodedData[2].split(".")[0]) 
                    if accessed == True:
                        connection.send(str.encode("OK\t" + str(senderPID)))
                    else: 
                        reqClock = int(decodedData[2].split(".")[1])
                        if reqClock <= myConnectionNumber:
                            connection.send(str.encode("OK\t" + str(senderPID) + "\t" + str(pid) + "\tOK Receiver Sender"))
                        else:
                            queueData.append(line)
                            print("Request Queued: Process:" + str(senderPID))
                
                elif decodedData[0] == "OK":
                    votes += 1
                    if votes == threadCount:
                        print("Access granted")

                        time.sleep(3)

                        with open ( resource , 'r') as f:
                            chk_data = f.read().strip()

                        counter = int(chk_data.split(" ")[0])
                        with open(resource, "w") as f:
                            f.write(str(counter+1) + " " + str(pid) + "\n" )

                        time.sleep(3)
                        print("File released")

                        accessed = True
        
                time.sleep(2)
    
def receiver(connection, pid):
    while True:
        res = connection.recv(1024)
        lin = res.decode('utf-8').split("\n")
        for l in lin:
            queueData.append(l)
# thread to send data to all the active processes (broadcast request message)


#Created the Threads for handling communication
t1 = threading.Thread(target=receiver, args=(ClientMultiSocket, pid))  
t2 = threading.Thread(target=queueReader, args=(ClientMultiSocket,pid))
 
#Started the threads
t1.start()
t2.start()
 

#Joined the threads
t1.join()
t2.join()
 
ClientMultiSocket.close()