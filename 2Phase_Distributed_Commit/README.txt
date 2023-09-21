Assignment 2:
2 Phase Commit Protocol

Steps to execute:
1. Run Co-ordiantor file
    a. With no failures
        Run python coordinator.py or
        Run python3 coordinator.py
    
    b. With failure induced before participants vote i.e. INIT state of participants
        Run python coordinatorFail.py or
        Run python3 coordinatorFail.py

2. Open different terminals to run different processes. The socket can take up to 5 process connections
participant.py file is run with arguments to run as different processes.
Format: python filename.py processID
    a. To run Process 1
        Run python participant.py 1 or
        Run python3 participant.py 1  

    b. To run Process 2
        Run python participant.py 2 or
        Run python3 participant.py 2  
    
    c. To run Process 3
        Run python participant.py 3 or
        Run python3 participant.py 3

Note: The failre is induced for participant 2 in the code. To run without failures or to check 
for a scenario where participant does not fail do use process ID as 2

3. After starting all the participants on the coordinator terminal window enter any key to start the process

4. Once a VOTE_REQUEST is sent out to all the participants respond with a Yes or No at the participants terminal 

4. The output gives the broadcasted messages in a consistent manner
