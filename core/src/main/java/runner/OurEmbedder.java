package runner;

import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OurEmbedder extends Embedder {
    private static final Logger logger =
            LoggerFactory.getLogger(OurEmbedder.class);
    private String userScenario;

    public OurEmbedder() {
        super();
    }

    public OurEmbedder(String userScenario) {
        super();
        this.userScenario = userScenario;
    }

    @Override
    public StoryManager storyManager() {
        logger.debug("Inside storyManager()");
        if (storyManager == null) {
            storyManager = createStoryManager();
        }
        return storyManager;
    }

    private StoryManager createStoryManager() {
        OurStoryManager ourStoryManager =
                new OurStoryManager(configuration(), stepsFactory(),
                        embedderControls(), embedderMonitor(),
                        executorService(), performableTree(), timeoutParsers());
        ourStoryManager.setUserScenario(userScenario);
        return ourStoryManager;
    }
}
