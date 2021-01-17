package runner;

import actions.our.ScenarioProperties;
import actions.our.StoryContext;
import actions.our.StoryProperties;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.NullStoryReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class OurStoryReporter extends NullStoryReporter {
    private static final Logger logger =
            LoggerFactory.getLogger(OurStoryReporter.class);
    private String scenarioName;
    private String storyName;

    @Override
    public void beforeScenario(Scenario scenario) {
        this.scenarioName = scenario.getTitle();
        logger.debug("Inside beforeScenario " + this.scenarioName);

        //Read data for all scenarios in this story
        StoryContext.readScenarioData();
        String title = scenario.getTitle();
        StoryContext.extractData(title);

    }

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
        StoryContext.createStoryContext();
        StoryProperties storyProps = createStory(story);
        StoryContext.addStory(storyProps);

    }

    private StoryProperties createStory(Story story) {
        StoryProperties storyProperties = new StoryProperties();
        storyProperties.setStoryName(story.getName());
        story.getScenarios().forEach(scenario -> storyProperties
                .getScenarios()
                .add(ScenarioProperties.cloneFromParent(scenario)));

        return storyProperties;
    }

    @Override
    public void afterScenario() {
        logger.debug("Inside afterScenario " + this.scenarioName);
        StoryContext.destroyScenarioData();
    }

    @Override
    public void afterStory(boolean givenStory) {
        logger.debug("Inside afterStory " + this.storyName);
        StoryContext.destroyStoryData();
    }
}
