package runner;

import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryManager;

public class OurEmbedder extends Embedder {
	private static final String CLASSNAME = OurEmbedder.class.getSimpleName();
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
		System.out.println(CLASSNAME+": Inside storyManager()");
		if (storyManager == null) {
			storyManager = createStoryManager();
		}
		return storyManager;
	}

	private StoryManager createStoryManager() {
		OurStoryManager ourStoryManager = new OurStoryManager(configuration(), stepsFactory(), embedderControls(), embedderMonitor(),
                executorService(), performableTree(), timeoutParsers());
		ourStoryManager.setUserScenario(userScenario);
		return ourStoryManager;
	}
}
