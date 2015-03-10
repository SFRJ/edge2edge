# edge2edge
Acceptance testing a module with a live specification framework(yatspec)


In this example the application under test depends on interactions with 3 other systems.
Here I will be simulating:
- A client sending a response to the application under test
- The application sending GET requests to 2 systems to gather information
- Gather the responses, process them and answer to the client.
- Send also the response via POST to a 3rd system

![ScreenShot](https://raw.githubusercontent.com/SFRJ/edge2edge/master/diagram.png)

Notes:    
I created a fake server for every system that the application under tests talks to, this way
the acceptance test is isolated and tests just the application from edge to edge.

It's very important to not confuse edge to edge with a system integration test. 
This is an acceptance test for an application, it describes the criteria for acceptance for the application
under test, and not for the whole system. 

System integration testing is slow, expensive and hard to mantain. 
You can build a full distributed system the ATDD way following this approach, there is no need for an integration
test.


