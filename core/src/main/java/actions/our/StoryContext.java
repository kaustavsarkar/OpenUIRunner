package actions.our;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoryContext {
    private static final Logger logger =
            LoggerFactory.getLogger(StoryContext.class);
    private static ThreadLocal<StoryProperties> storyContext;
    //private static Map<String, Map<String, String>> storyData = new HashMap<>();
    private static Map<String, String> scenarioData = new HashMap<>();

    public static void createStoryContext() {
        if (storyContext == null) {
            storyContext = new ThreadLocal<>();
        }
    }

    public static StoryProperties getStory() {
        return storyContext.get();
    }

    public static void addStory(StoryProperties story) {
        storyContext.set(story);
    }

    /**
     * This is executed before scenario
     *
     * @param title
     */
    public static void extractData(String title) {
        logger.info("Extracting Scenario Data for '" + title + "'");
        if (getStory().getScenarios() == null ||
                getStory().getScenarios().isEmpty()) {
            logger.warn("There is no scenario present hence returning");
            return;
        }
        ScenarioProperties scenario = getStory().getScenario(title.trim());
        logger.info("Scenario received: " + scenario);
        scenarioData = scenario.getData();
        logger.info("Data for scenario: " + scenarioData);
    }

    public static String getData(String dataKey) {
        logger.info("Getting scenario data: " + scenarioData);
        if (scenarioData == null || scenarioData.isEmpty()) {
            return null;
        }
        return scenarioData.get(dataKey);
    }

    /**
     *
     */
    public static void readScenarioData() {

        logger.info(
                "Reading scenarios for story: " + getStory().getStoryName());

        List<String> scenarios = storyContext
                .get()
                .getScenarios()
                .parallelStream()
                .map(scenario -> scenario.getTitle().trim())
                .collect(Collectors.toList());

        logger.info("Scenarios collected: " + scenarios);

        if (scenarios.isEmpty() || getStory().getDataPath() == null ||
                getStory().getDataPath().isEmpty()) {
            return;
        }

        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        logger.info("Reading Story Data path: " + getStory().getDataPath());
        try (InputStream is = classLoader
                .getResourceAsStream("data/" + getStory().getDataPath());
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is))) {

            String line;
            int counter = 0;
            Data data = new Data();
            while ((line = reader.readLine()) != null) {
                logger.debug("Reading line: " + line);

                if (counter == 0) {
                    data.setHeaders(line.split(","));
                    counter++;
                    continue;
                }
                String[] values = line.split(",");

                String scenarioName = values[0].trim();

                logger.info("Scenarios: " + scenarios);
                logger.info("Scenario: " + scenarioName);
                if (scenarios.contains(scenarioName)) {
                    data.setValues(values);
                    String[] heads = data.getHeaders();
                    String[] vals = data.getValues();

                    if (vals != null && vals.length != 0) {

                        for (int i = 1; i < vals.length; i++) {
                            getStory()
                                    .getScenario(scenarioName)
                                    .getData()
                                    .put(heads[i], vals[i]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed with error: ", e);
        }

    }

    public static void destroyScenarioData() {
        scenarioData = new HashMap<>();
    }

    public static void destroyStoryData() {
        logger.info("Clearing scenarios for: " + getStory().getStoryName());
        boolean removedAll =
                getStory().getScenarios().removeIf(scenario -> true);
        logger.info("Cleared all scenarios: " + removedAll);
    }

    private static class Data {
        private String[] headers;
        private String[] values;

        public String[] getHeaders() {
            return headers;
        }

        public void setHeaders(String[] headers) {
            this.headers = headers;
        }

        public String[] getValues() {
            return values;
        }

        public void setValues(String[] values) {
            this.values = values;
        }

    }
}
