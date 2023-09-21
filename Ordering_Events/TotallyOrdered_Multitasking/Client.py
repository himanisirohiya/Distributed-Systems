import socket
import sys
import threading
import time

ClientMultiSocket = socket.socket()
host = '127.0.0.1'
port = 3000

if len(sys.argv) != 3:
	raise ValueError('Process terminated. Please start the process using $python Client.py [pid] [priority]')

print('Waiting for connection response')
try:
    ClientMultiSocket.connect((host, port))
except socket.error as e:
    print(str(e))

pid = sys.argv[1]
priority = sys.argv[2]

regMsg = "REG\t" + str(pid) + "\t" + str(priority) + "\n"
ClientMultiSocket.send(regMsg.encode())

def receiver(connection, pid):
    while True:
        res = connection.recv(1024)
        lines = res.decode('utf-8').split("\n")
        for line in lines:
            if( len(line) > 0 ):
                print(line)
                decodedData = line.split("\t")
                if decodedData[0] == "MSG":
                    if( str(pid) == "2" ):
                        time.sleep(10)
                    ackMsg = "ACK\t" + str(decodedData[1]) + "." + str(pid) + "-Ack\t" + str(decodedData[2]) + "\n"
                    connection.send(str.encode(ackMsg))

def sender(connection):
    clock = 1
    while True:
        Input = input()
        inputMsg = "MSG\t" + str(pid) + "." + str(clock) + "\t" + str(Input) + "\n"
        connection.send(str.encode(inputMsg))
        clock += 1

res = ClientMultiSocket.recv(1024)			# Server Connected

#Created the Threads
t1 = threading.Thread(target=receiver, args=(ClientMultiSocket, pid))
t2 = threading.Thread(target=sender, args=(ClientMultiSocket,))
 
#Started the threads
t1.start()
t2.start()
 
#Joined the threads
t1.join()
t2.join()
 
ClientMultiSocket.close()