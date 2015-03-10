package acceptancetests;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import externalsystems.RequestsHistoryRepository;
import externalsystems.WordsStartingWithConsonantGenerator;
import externalsystems.WordsStartingWithVocalGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import production.TheApplication;
import static javax.ws.rs.client.ClientBuilder.newClient;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpecRunner.class)
public class ClientGetsAllWordsOrderedAlphabeticallyTest extends TestState {

    //External systems
    private RequestsHistoryRepository history;
    private WordsStartingWithConsonantGenerator consonants;
    private WordsStartingWithVocalGenerator vocals;
    //Application under test
    private TheApplication applicationUnderTest;

    @Before
    public void init() throws Exception {
        vocals = new WordsStartingWithVocalGenerator(9998, "/", interestingGivens, capturedInputAndOutputs);
        consonants = new WordsStartingWithConsonantGenerator(9997, "/", interestingGivens, capturedInputAndOutputs);
        history = new RequestsHistoryRepository(9996, "/", capturedInputAndOutputs);
        applicationUnderTest = new TheApplication(9999, "/");
    }


    @After
    public void cleanUp() throws Exception {
        vocals.stopServer();
        consonants.stopServer();
        history.stopServer();
        applicationUnderTest.stopServer();
    }

    @Test
    public void shouldReceiveWordsOrderedAlphabetically() throws Exception {
        given(theVocalsGeneratorHas("apple-early-uboat"));
        and(theConsonantGeneratorHas("car-frog"));
        when(aClientSendsARequestToTheApplication());
        then(theApplicatationResponse(), is("apple car early frog uboat"));
        then(theAuditedMessage(), is("the following message was audited: 'apple car early frog uboat'"));
    }

    private GivensBuilder theVocalsGeneratorHas(String words) {
        return givens -> {
            givens.add("words starting with vocal generator will return",words);
            return givens;
        };
    }

    private GivensBuilder theConsonantGeneratorHas(String words) {
        return givens -> {
            givens.add("words starting with consonant generator will return",words);
            return givens;
        };
    }

    private ActionUnderTest aClientSendsARequestToTheApplication() {
        return (givens, captures) -> {
            String url = "http://localhost:9999/";
            WebTarget target = newClient().target(url);
            Invocation.Builder invocationBuilder = target.request();
            Response response = invocationBuilder.get();
            captures.add("response", response.readEntity(String.class));
            return captures;
        };
    }

    private StateExtractor<String> theApplicatationResponse() {
        return captures -> captures.getType("response", String.class);
    }

    private StateExtractor<String> theAuditedMessage() {
        return captures -> {
            return captures.getType("the audited message", String.class);
        };
    }

}
