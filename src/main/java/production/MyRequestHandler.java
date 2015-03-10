package production;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.ws.rs.client.Invocation;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static javax.ws.rs.client.ClientBuilder.newClient;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

class MyRequestHandler implements HttpHandler {

    private final String vocalsSystem = "http://localhost:9998/";
    private final String consonantsSystem = "http://localhost:9997/";
    private final String requestsAuditSystem = "http://localhost:9996/";

    private Map<String, Invocation.Builder> targets = new HashMap<String,Invocation.Builder>() {
        {
            put(vocalsSystem, newClient().target(vocalsSystem).request());
            put(consonantsSystem, newClient().target(consonantsSystem).request());
            put(requestsAuditSystem, newClient().target(requestsAuditSystem).request());
        }
    };

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        //preparing the response
        String finalResponse = gatherAndSort(vocalsSystem, consonantsSystem);

        //Sending a post(Ignoring response)
        targets.get(requestsAuditSystem).post(entity(finalResponse, TEXT_PLAIN)).readEntity(String.class);

        //setting the response headers
        httpExchange.sendResponseHeaders(200, finalResponse.length());
        //writing to the response stream
        httpExchange.getResponseBody().write(finalResponse.getBytes());
        //closing the stream
        httpExchange.close();
    }

    private String gatherAndSort(String... remoteSytemUrl) {
        return asList(asList(remoteSytemUrl).stream()
                .map(remoteUrl -> targets.get(remoteUrl).get().readEntity(String.class))
                .reduce((seed, value) -> seed + "-" + value).get().split("-"))
                .stream()
                .sorted()
                .reduce((seed1, value1) -> seed1 + " " + value1).get();
    }
}
