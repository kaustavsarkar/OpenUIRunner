package actions.rest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class CatFactAction extends Steps {
    private static final Logger logger =
            LoggerFactory.getLogger(CatFactAction.class);
    private final WebDriver driver;
    public CatFactAction(WebDriver driver) {
        this.driver = driver;
    }

    @Given("Url is present")
    public void checkUrl() {
        assertNotNull("https://cat-fact.herokuapp.com/facts");
    }

    @When("I make a rest call")
    public void hitUrl() throws IOException {
        HttpGet request = new HttpGet("https://cat-fact.herokuapp.com/facts");
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {
            assertNotNull(response);
            Header[] headers = response.getAllHeaders();
            assertNotNull(headers);
            HttpEntity entity = response.getEntity();
        } catch (IOException e) {
            logger.error("Error while making rest call.", e);
        }
    }
}