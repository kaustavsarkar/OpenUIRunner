package runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbehave.core.embedder.MetaFilter;
import org.jbehave.core.embedder.StoryMapper;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.StoryMap;
import org.jbehave.core.model.StoryMaps;

public class OurStoryMapper extends StoryMapper {
	
	private String scenario;
	private Map<String, Set<Story>> map = new HashMap<>();
	public OurStoryMapper() {
	}
	
	public OurStoryMapper(String scenario) {
		this.scenario = scenario;
	}
	
	@Override
	 public void map(Story story, MetaFilter metaFilter) {
        if (metaFilter.allow(story.getMeta())) {
            boolean allowed = false;
            for (Scenario scenario : story.getScenarios()) {
                // scenario also inherits meta from story
                Meta inherited = scenario.getMeta().inheritFrom(story.getMeta());
                if (metaFilter.allow(inherited) && scenario.getTitle().equalsIgnoreCase(this.scenario)) {
                    allowed = true;
                    break;
                }
            }
            if (allowed) {
                add(metaFilter.asString(), story);
            }
        }
    }
	
    private void add(String filter, Story story) {
        storiesFor(filter).add(story);
    }

    private Set<Story> storiesFor(String filter) {
        Set<Story> stories = map.get(filter);
        if (stories == null) {
            stories = new HashSet<>();
            map.put(filter, stories);
        }
        return stories;
    }
    
    @Override
    public StoryMaps getStoryMaps() {
        List<StoryMap> maps = new ArrayList<>();
        for (String filter : map.keySet()) {
            maps.add(getStoryMap(filter));
        }
        return new StoryMaps(maps);
    }
}