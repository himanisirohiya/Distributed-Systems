Assignment 1:
Achieve Totally ordered multicast

Steps to execute:
1. Run Server file(used only for broadcasting of messages)
    a. Server with no priority for first message received
        Run python Server.py or
        Run python3 Server.py
    
    b. Server for first message received with priority 1
    Run python Server_startwithpriority.py or
    Run python3 Server_startwithpriority.py
    
2. Open different terminals to run different processes. The socket can take up to 5 process connections
Client.py file is run with arguments to run as different processes.
Format: python filename.py processID priority
    a. To run Process 1
        Run python Client.py 1 1 or
        Run python3 Client.py 1 1 

    b. To run Process 2
        Run python Client.py 2 2 or
        Run python3 Client.py 2 2 
    
    c. To run Process 3
        Run python Client.py 3 3 or
        Run python3 Client.py 3 3

Simlilarly 5 processes can be run

3. After starting the processes enter a message on the terminal window

4. The output gives messages in total order multicast
