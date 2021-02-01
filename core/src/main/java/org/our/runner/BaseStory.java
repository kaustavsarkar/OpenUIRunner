package org.our.runner;

import com.google.inject.Inject;
import org.jbehave.core.ConfigurableEmbedder;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.our.configuration.OurProperties;
import org.our.runner.stepfactory.CustomStepFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BaseStory extends ConfigurableEmbedder {
    private static final Logger logger =
            LoggerFactory.getLogger(BaseStory.class);
    private final OurProperties ourProperties;
    private final CustomStepFactory customStepFactory;
    protected Configuration configuration;

    @Inject
    BaseStory(OurProperties ourProperties,
              Configuration configuration,
              CustomStepFactory customStepFactory) {
        logger.debug("entered constructor");
        this.ourProperties = ourProperties;
        this.configuration = configuration;
        this.customStepFactory = customStepFactory;

        logger.info("Properties: " + this.ourProperties.toString());
    }

    @Override
    public Configuration configuration() {
        logger.debug("entered configuration");
        return configuration;
    }

    @Override
    public void run() {
        logger.debug("Thread Name: " +
                Thread.currentThread().getName());
        logger.debug("entered run()");

        Embedder embedder = configuredEmbedder();
        try {
            logger.info("Include Tags : " +
                    ourProperties.getIncludeTags());
            embedder.useMetaFilters(
                    ourProperties.getIncludeTags());
            embedder.useEmbedderControls(new EmbedderControls()
                    .doIgnoreFailureInStories(true).useThreads(1)
                    .doIgnoreFailureInView(true)
                    .doFailOnStoryTimeout(true)
                    //TODO(kaustav): add it to configuration.
                    .useStoryTimeouts("300"));
            List<String> storyPath = storyPaths();
            logger.info("Story PAth: " + storyPath);
            embedder.runStoriesAsPaths(storyPath);
        } finally {
            embedder.generateCrossReference();
            embedder.generateSurefireReport();
            embedder.generateReportsView();
        }
    }

    protected List<String> storyPaths() {
        logger.debug("entered storyPaths()");
        String codeLocation = CodeLocations
                .codeLocationFromPath(Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResource("")
                        .getPath())
                .getFile();

        logger.info("Using Code Location: " + codeLocation);

        List<String> storyList = new ArrayList<>();
        String storyFiles = ourProperties.getStoryFile();

        if (storyFiles != null && !storyFiles.isEmpty()) {

            String[] storyFileArr = storyFiles.split(",");

            for (String storyFile : storyFileArr) {
                storyList.add("**/" + storyFile);
            }
        } else {
            storyList.add("**/**.story");
        }

        logger.info("Story List" + storyList);
        return new StoryFinder().findPaths(codeLocation, storyList, null);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        logger.debug("entered stepsFactory()");
        return customStepFactory;
    }
}
