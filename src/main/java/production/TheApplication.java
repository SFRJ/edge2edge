package production;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TheApplication {

    private HttpServer httpServer;

    public TheApplication(int port, String context) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext(context, new MyRequestHandler());
        httpServer.start();
    }

    public static void main(String[] args) throws IOException {
    }

    public void stopServer() {
        httpServer.stop(0);
    }
}
