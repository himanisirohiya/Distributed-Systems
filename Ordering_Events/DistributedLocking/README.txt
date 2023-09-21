Assignment 1:
Achieve Causally ordered Vector clock

Steps to execute:
1. Run Server file(used only for broadcasting of messages)
    Run python Server.py or
    Run python3 Server.py

2. Open different terminals to run different processes. The socket can take up to 5 process connections
Client.py file is run with arguments to run as different processes.
Format: python filename.py processID
    a. To run Process 1
        Run python Client.py 1 or
        Run python3 Client.py 1  

    b. To run Process 2
        Run python Client.py 2 or
        Run python3 Client.py 2  
    
    c. To run Process 3
        Run python Client.py 3 or
        Run python3 Client.py 3

Simlilarly 5 processes can be run

3. After a process starts it send request to access a shared resource

4. The output shows the process which acquire the lock, release the lock and queued requests
