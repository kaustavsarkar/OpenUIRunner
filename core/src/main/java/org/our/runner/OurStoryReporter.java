package org.our.runner;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.NullStoryReporter;
import org.our.configuration.OurProperties;
import org.our.configuration.ScenarioProperties;
import org.our.configuration.StoryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Reads the data files being provided by the clients while running the
 * application.
 * <p>
 * Reads the csv files before a story is being executed and injects an instance
 * of {@link ScenarioProperties} before executing a scenario and provides data
 * pertaining to that scenario.
 */
public class OurStoryReporter extends NullStoryReporter {
    private static final Logger logger =
            LoggerFactory.getLogger(OurStoryReporter.class);
    private final OurProperties ourProperties;
    private final ScenarioPropertiesProvider scenarioPropertyProvider;
    private String scenarioName;
    private String storyName;
    private StoryProperties storyProperties;
    private Set<String> scenarioTitles;

    /**
     * Object created inside {@link OurRunnerModule}.
     */
    OurStoryReporter(ScenarioPropertiesProvider scenarioPropertiesProvider,
                     OurProperties ourProperties) {
        this.scenarioPropertyProvider = scenarioPropertiesProvider;
        this.ourProperties = ourProperties;
    }

    /**
     * Reads data from csv file provided by the client while running the
     * application. Creates and memoizes an instance of {@link StoryProperties}
     * which is used further used inside {@link #beforeScenario(Scenario)} to
     * inject data pertaining to scenario being injected via {@link
     * ScenarioProperties}.
     */
    @Override
    public void beforeStory(Story story, boolean givenStory) {
        this.storyName = story.getName();
        String threadName = story.getName().replace(".", "_")
                + "-Our-Thread";
        Thread.currentThread().setName(threadName);
        logger.debug("Inside beforeStory");
        logger.debug(
                "Running inside Thread : " + Thread.currentThread().getName());
        logger.debug("Story Name : " + story.getName());
        scenarioTitles =
                story.getScenarios().stream()
                        .map(scenario -> scenario.getTitle().trim()).collect(
                        toSet());
        readScenarioData(story);
    }


    /**
     * Reads data for the scenarios present in {@link Story} instance provided
     * in the argument.
     */
    private void readScenarioData(Story story) {
        logger.info(
                "Reading scenarios for story: " +
                        storyName);

        logger.info("Scenarios collected: " + scenarioTitles);

        String dataPath = ourProperties.getDataPath();
        if (scenarioTitles.isEmpty() ||
                Strings.isNullOrEmpty(dataPath)) {
            return;
        }
        createInstanceOfStoryProperties(dataPath, story);
    }

    private void createInstanceOfStoryProperties(String dataPath,
                                                 Story story) {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        logger.info("Reading Story Data path: " + dataPath);
        StoryProperties.Builder storyProperties =
                StoryProperties.newBuilder();
        storyProperties.setStoryName(storyName)
                .setOurProperties(ourProperties);
        ImmutableSet.Builder<ScenarioProperties> scenarioProperties =
                ImmutableSet.builder();
        try (InputStream is = classLoader
                .getResourceAsStream("data/" + dataPath);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is))) {

            String line;
            int counter = 0;
            Data data = new Data();
            while ((line = reader.readLine()) != null) {
                logger.debug("Reading line: " + line);

                // If it is the first iteration, we need to collect the headers.
                if (counter == 0) {
                    data.setHeaders(line.split(","));
                    counter++;
                    continue;
                }
                String[] rowValues = line.split(",");

                // The first value in the csv shall always need to be the
                // scenario name.
                String scenarioName = rowValues[0].trim();

                logger.info("Scenarios: " + scenarioTitles);
                logger.info("Scenario: " + scenarioName);
                if (scenarioTitles.contains(scenarioName)) {
                    Scenario scenario =
                            story.getScenarios().stream()
                                    .filter(scene -> scene.getTitle().trim()
                                            .equals(scenarioName)).findFirst()
                                    .orElse(null);
                    data.setValues(rowValues);
                    String[] heads = data.getHeaders();
                    String[] values = data.getValues();

                    if (values != null && values.length != 0) {

                        for (int i = 1; i < values.length; i++) {
                            scenarioProperties
                                    .add(ScenarioProperties.newBuilder()
                                            .setScenario(scenario)
                                            .setStoryName(this.storyName)
                                            .setData(ImmutableMap
                                                    .of(heads[i], values[i]))
                                            .build());
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed with error: ", e);
        } finally {
            this.storyProperties =
                    storyProperties.setScenarios(scenarioProperties.build())
                            .build();
        }
    }

    /**
     * Create an instance of {@link ScenarioProperties} to be injected in
     * actions along with data related to the scenarios.
     */
    @Override
    public void beforeScenario(Scenario scenario) {
        scenarioName = scenario.getTitle();
        logger.debug("Inside beforeScenario " + scenarioName);

        //Read data for all scenarios in this story
        ScenarioProperties scenarioProperties =
                storyProperties.getScenarios().stream()
                        .filter(scenarioProperty -> {
                            logger.debug(scenarioProperty
                                    .getScenario().getTitle());
                            return scenarioProperty
                                    .getScenario().getTitle().trim()
                                    .equals(scenarioName);
                        }).findFirst().get();
        // TODO(kaustav): Make it accessible inside the concerned thread.
        scenarioPropertyProvider.set(scenarioProperties);
    }

    /**
     * Removes scenario name after the scenario has run.
     */
     //TODO(kaustav): Try to remove data related to the scenario done executing.
    @Override
    public void afterScenario() {
        logger.debug("Inside afterScenario " + scenarioName);
        logger.debug("Remaining scenarios :" + scenarioTitles);
        scenarioTitles.remove(scenarioName);
    }

    /**
     * Clearing story data once the story has completed.
     */
    @Override
    public void afterStory(boolean givenStory) {
        logger.debug("Inside afterStory " + this.storyName);
        storyProperties = null;
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
