package externalsystems;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import externalsystems.template.FakeSystemTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class WordsStartingWithConsonantGenerator extends FakeSystemTemplate {

    private InterestingGivens givens;
    private CapturedInputAndOutputs captures;

    public WordsStartingWithConsonantGenerator(int runningPort, String context, InterestingGivens givens, CapturedInputAndOutputs captures) throws IOException {
        super(runningPort, context);
        this.givens = givens;
        this.captures = captures;
    }

    @Override
    public HttpHandler customHttpHandler() {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                //message that will be returned
                String response = givens.getType("words starting with consonant generator will return", String.class);
                //setting the response headers
                httpExchange.sendResponseHeaders(200, response.length());
                //getting the output stream
                OutputStream os = httpExchange.getResponseBody();
                //writing to the response stream
                os.write(response.getBytes());
                //closing the stream
                os.close();
                httpExchange.close();

                captures.add("Words starting with consonant generator response", response);
            }
        };
    }
}
