package runner;

import actions.our.ScenarioProperties;
import actions.our.StoryContext;
import actions.our.StoryProperties;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.NullStoryReporter;

/**
 *
 */
public class OurStoryReporter extends NullStoryReporter {
    private static final String CLASSNAME = OurStoryReporter.class.getSimpleName();
    //StoryReporter delegate;
    private String scenarioName;
    private String storyName;

    @Override
    public void beforeScenario(Scenario scenario) {
        this.scenarioName = scenario.getTitle();
        System.out.println(CLASSNAME + ": Inside beforeScenario " + this.scenarioName);

        //Read data for all scenarios in this story
        StoryContext.readScenarioData();
        String title = scenario.getTitle();
        StoryContext.extractData(title);

        //delegate.beforeScenario(scenario);
    }

    @Override
    public void beforeStory(Story story, boolean givenStory) {
        this.storyName = story.getName();
        String threadName = story.getName().replace(".", "_") + "-Our-Thread";
        Thread.currentThread().setName(threadName);
        System.out.println(CLASSNAME + ": Inside beforeStory");
        System.out.println(CLASSNAME + ": Running inside Thread : " + Thread.currentThread().getName());
        System.out.println(CLASSNAME + ": Story Name : " + story.getName());
        StoryContext.createStoryContext();
        StoryProperties storyProps = createStory(story);
        StoryContext.addStory(storyProps);

        //delegate.beforeStory(story, givenStory);
    }

    private StoryProperties createStory(Story story) {
        StoryProperties storyProperties = new StoryProperties();
        storyProperties.setStoryName(story.getName());
        story.getScenarios().forEach(scenario -> {
            storyProperties
                    .getScenarios()
                    .add(ScenarioProperties.cloneFromParent(scenario));
        });

        return storyProperties;
    }

    @Override
    public void afterScenario() {
        System.out.println(CLASSNAME + ": Inside afterScenario "+this.scenarioName);
        StoryContext.destroyScenarioData();
        //delegate.afterScenario();
    }

    @Override
    public void afterStory(boolean givenStory) {
        System.out.println(CLASSNAME + ": Inside afterStory "+this.storyName);
        StoryContext.destroyStoryData();
        //delegate.afterStory(givenStory);
    }
}
