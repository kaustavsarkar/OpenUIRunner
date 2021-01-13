package actions.our;

import org.jbehave.core.model.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class StoryProperties extends OurProperties {
    private static final Logger logger =
            LoggerFactory.getLogger(StoryProperties.class);
    private String storyName;
    private Set<ScenarioProperties> scenarios;

    public static StoryProperties buildFromOurProps(StoryProperties story,
                                                    OurProperties ourProperties) {
        Field[] fields = ourProperties.getClass().getDeclaredFields();
        logger.info("OurProperty fields size: " + fields.length);
        Arrays.stream(fields)
                .filter(field -> !field.getName().equals("CLASSNAME"))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        Field storyField = story.getClass()
                                .getDeclaredField(field.getName());
                        logger.info("Story field: " + storyField.getName());
                        storyField.setAccessible(true);
                        storyField.set(story, field.get(ourProperties));
                        logger.info(
                                "Story field value: " + storyField.get(story));
                    } catch (NoSuchFieldException e) {
                        logger.warn("Field not found, though present in " +
                                "OurProperties. " + field.getName(), e);
                        logger.info("Checking in parent: " +
                                ourProperties.getClass().getSimpleName());
                        try {
                            Field parentField = story.getClass().getSuperclass()
                                    .getDeclaredField(field.getName());
                            parentField.setAccessible(true);
                            parentField.set(story, field.get(ourProperties));
                        } catch (NoSuchFieldException ex) {
                            logger.info("Field not found in OurProperties " +
                                    "either. Strange!! Check OurProperties " +
                                    "should not extends anything but Object" + field.getName());
                        } catch (IllegalAccessException ex) {
                            logger.error("Field is not accessible in Parent. " +
                                    "Corresponding Field name: " +
                                    field.getName(), e);
                        }
                    } catch (IllegalAccessException e) {
                        logger.error("Field is not accessible. Corresponding " +
                                "parent field name: " + field.getName(), e);
                    }
                });

        logger.info("Copied story: " + story);
        return story;
    }

    public String getStoryName() {
        return this.storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public Set<ScenarioProperties> getScenarios() {
        if (this.scenarios == null) {
            this.scenarios = new HashSet<>();
        }
        return this.scenarios;
    }

    public ScenarioProperties getScenario(String scenarioTitle) {
        logger.info("Scenario Title: " + scenarioTitle);
        return this.scenarios
                .parallelStream()
                .filter(scenario -> scenario.getTitle().equals(scenarioTitle))
                .findFirst()
                .get();
    }

    @Override
    public String toString() {
        return "StoryProperties{" +
                "storyName='" + storyName + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", scenarios=" + scenarios +
                ", envName='" + envName + '\'' +
                ", envURL='" + envURL + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", storyFile='" + storyFile + '\'' +
                ", tags='" + tags + '\'' +
                ", browser='" + browser + '\'' +
                ", driverName='" + driverName + '\'' +
                '}';
    }
}
