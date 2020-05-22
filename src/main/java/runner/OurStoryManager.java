package runner;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.embedder.EmbedderMonitor;
import org.jbehave.core.embedder.MetaFilter;
import org.jbehave.core.embedder.PerformableTree;
import org.jbehave.core.embedder.StoryManager;
import org.jbehave.core.embedder.StoryTimeouts.TimeoutParser;
import org.jbehave.core.failures.BatchFailures;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.steps.InjectableStepsFactory;

public class OurStoryManager extends StoryManager {

	private String userScenario;

	public OurStoryManager(Configuration configuration, InjectableStepsFactory stepsFactory,
			EmbedderControls embedderControls, EmbedderMonitor embedderMonitor, ExecutorService executorService,
			PerformableTree performableTree, TimeoutParser[] parsers) {
		super(configuration, stepsFactory, embedderControls, embedderMonitor, executorService, performableTree,
				parsers);
	}

	@Override
	public void runStoriesAsPaths(List<String> storyPaths, MetaFilter filter, BatchFailures failures) {

		/*List<Story> stories = storiesOfPaths(storyPaths).stream().filter(story -> {
			List<Scenario> scenarios = story.getScenarios();

			scenarios = scenarios.stream().filter(scenario -> {
				scenario.asMeta("");
				return scenario.getTitle().equals(userScenario);
			}).collect(Collectors.toList());
			
			
			story.getScenarios().clear();
			story.getScenarios().addAll(scenarios);
			return (scenarios != null && !scenarios.isEmpty());
		}).map(story -> {
			story.getScenarios().clear();
			story.getScenarios().addAll(scenarios);
			
			return story;
		}).collect(Collectors.toList());*/
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
