package externalsystems;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.sun.net.httpserver.HttpHandler;
import externalsystems.template.FakeSystemTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import static java.lang.String.format;

public class RequestsHistoryRepository extends FakeSystemTemplate {

    private CapturedInputAndOutputs captures;

    public RequestsHistoryRepository(int runningPort, String context, CapturedInputAndOutputs captures) throws IOException {
        super(runningPort, context);
        this.captures = captures;
    }



    @Override
    public HttpHandler customHttpHandler() {
        return httpExchange -> {
            Scanner scanner = new Scanner(httpExchange.getRequestBody());
            String auditMessage = "";
            while(scanner.hasNext()) {
                auditMessage = auditMessage + scanner.next() + " ";
            }
            scanner.close();
            captures.add("the audited message", format("the following message was audited: '%s'", auditMessage.trim()));
            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.close();
        };
    }
}
