package externalsystems.template;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class FakeSystemTemplate {

    //An embedded Http server
    private HttpServer server;

    //The port where the server runs
    private final int RUNNING_PORT;
    //The server will use the context to find the handler(See createContext() below)
    private final String CONTEXT;

    public FakeSystemTemplate(int runningPort, String context) throws IOException {
        this.RUNNING_PORT = runningPort;
    //The server is a service
        this.CONTEXT = context;
    //A socket address is the combination of an IP address/hostname and a port number.(In other words, how to find the server)
        InetSocketAddress socket = new InetSocketAddress(RUNNING_PORT);
    //Creating the server
        server = HttpServer.create(socket, 0);
    //Creating a context. Create context needs a context path and a handler. This is basically the wiring between the server
    //and the handler that will handle the requests ant the responses.
        server.createContext(CONTEXT, customHttpHandler());
    //Starting the server
        server.start();
    }

    //Just a template method. The specifics of how the handling will be done is up to each of the subclasses.
    public abstract HttpHandler customHttpHandler();

    //Stops the server
    public void stopServer() {
        server.stop(0);
    }
}
