package org.our.runner;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.*;
import org.jbehave.core.embedder.StoryTimeouts.TimeoutParser;
import org.jbehave.core.failures.BatchFailures;
import org.jbehave.core.model.Story;
import org.jbehave.core.steps.InjectableStepsFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class OurStoryManager extends StoryManager {

    private String userScenario;

    public OurStoryManager(Configuration configuration,
                           InjectableStepsFactory stepsFactory,
                           EmbedderControls embedderControls,
                           EmbedderMonitor embedderMonitor,
                           ExecutorService executorService,
                           PerformableTree performableTree,
                           TimeoutParser[] parsers) {
        super(configuration, stepsFactory, embedderControls, embedderMonitor,
                executorService, performableTree,
                parsers);
    }

    @Override
    public void runStoriesAsPaths(List<String> storyPaths, MetaFilter filter,
                                  BatchFailures failures) {
        List<Story> stories = storiesOfPaths(storyPaths);
        runStories(stories, filter, failures);
    }

    public String getUserScenario() {
        return userScenario;
    }

    public void setUserScenario(String userScenario) {
        this.userScenario = userScenario;
    }

}
