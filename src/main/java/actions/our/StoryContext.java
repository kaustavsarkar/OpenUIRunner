package actions.our;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoryContext {
    private static final String CLASSNAME = StoryContext.class.getSimpleName();
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
        System.out.println(CLASSNAME + ": Extracting Scenario Data for '" + title + "'");
        if (getStory().getScenarios() == null || getStory().getScenarios().isEmpty()) {
            System.out.println(CLASSNAME + ": There is no scenario present hence returning");
            return;
        }
        ScenarioProperties scenario = getStory().getScenario(title.trim());
        System.out.println(CLASSNAME + ": Is Scenario Present " + scenario);
        scenarioData = scenario.getData();
        System.out.println(CLASSNAME + " Data Extracted: " + scenarioData);
    }

    public static String getData(String dataKey) {
        System.out.println(CLASSNAME + ": Getting Scenario Data: " + scenarioData);
        if (scenarioData == null || scenarioData.isEmpty()) {
            return null;
        }
        return scenarioData.get(dataKey);
    }

    /**
     *
     */
    public static void readScenarioData() {

        System.out.println(CLASSNAME + ": Reading Scenarios for Story : " + getStory().getStoryName());

        List<String> scenarios = storyContext
                .get()
                .getScenarios()
                .parallelStream()
                .map(scenario -> scenario.getTitle().trim())
                .collect(Collectors.toList());

        System.out.println(CLASSNAME + ": Scenarios collected : " + scenarios);

        if (scenarios == null || scenarios.isEmpty() ||
                getStory().getDataPath() == null || getStory().getDataPath().isEmpty()) {
            return;
        }

        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        System.out.println(CLASSNAME + ": Reading Story Data Path " + getStory().getDataPath());
        try (InputStream is = classLoader
                .getResourceAsStream("data/" + getStory().getDataPath());
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is))) {

            String line;
            int counter = 0;
            Data data = new Data();
            while ((line = reader.readLine()) != null) {

                System.out.println(CLASSNAME + ": Read Line " + line);

                if (counter == 0) {
                    data.setHeaders(line.split(","));
                    counter++;
                    continue;
                }
                String[] values = line.split(",");

                String scenarioName = values[0].trim();

                System.out.println(CLASSNAME + ": Scenarios " + scenarios);
                System.out.println(CLASSNAME + ": Scenario " + scenarioName);
                if (scenarios.contains(scenarioName)) {
                    data.setValues(values);
                    String[] heads = data.getHeaders();
                    String[] vals = data.getValues();

                    System.out.println(
                            CLASSNAME + ": Values: " + Arrays.toString(vals));

                    if (vals != null && vals.length != 0) {

                        for (int i = 1; i < vals.length; i++) {
                            getStory()
                                    .getScenario(scenarioName)
                                    .getData()
                                    .put(heads[i], vals[i]);
                            //scenData.put(heads[i], vals[i]);
                        }
                    }
                }


                //storyData.put(scenarioName, scenData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void destroyScenarioData() {
        scenarioData = new HashMap<>();
    }

    public static void destroyStoryData() {
        // storyData = new HashMap<>();
        System.out.println(CLASSNAME + ": Clearing Scenarios for : " + getStory().getStoryName());

        boolean removedAll = getStory().getScenarios().removeIf(scenario -> true);

        System.out.println(CLASSNAME + ": ClearedAll Scenarios: " + removedAll);
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
