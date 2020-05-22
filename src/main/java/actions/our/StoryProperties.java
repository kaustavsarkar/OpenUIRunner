package actions.our;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class StoryProperties extends OurProperties {
    private static final String CLASSNAME = StoryProperties.class.getSimpleName();
    private String storyName;
    private Set<ScenarioProperties> scenarios;

    public static StoryProperties buildFromOurProps(StoryProperties story, OurProperties ourProperties) {
        //StoryProperties story = new StoryProperties();

        Field[] fields = ourProperties.getClass().getDeclaredFields();
        System.out.println(CLASSNAME + ": Our Property Fields size " + fields.length);
        Arrays.stream(fields)
                .filter(field -> !field.getName().equals("CLASSNAME"))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        Field storyField = story.getClass().getDeclaredField(field.getName());
                        System.out.println(CLASSNAME + ": Story Field " + storyField.getName());
                        storyField.setAccessible(true);
                        storyField.set(story, field.get(ourProperties));
                        System.out.println(CLASSNAME + ": Story Field Val " + storyField.get(story));
                    } catch (NoSuchFieldException e) {
                        System.err.println(CLASSNAME + ": Field not found, though present in OurProperties. " + field.getName());
                        System.out.println(CLASSNAME + ": Checking in parent: " + ourProperties.getClass().getSimpleName());
                        try {
                            Field parentField = story.getClass().getSuperclass().getDeclaredField(field.getName());
                            parentField.setAccessible(true);
                            parentField.set(story, field.get(ourProperties));
                        } catch (NoSuchFieldException ex) {
                            System.err.println(CLASSNAME + ": Field not found in OurProperties either. Strange!! Check OurProperties should not extends anything but Object" + field.getName());
                        } catch (IllegalAccessException ex) {
                            System.err.println(CLASSNAME + ": Field is not accessible in Parent. Corresponding Field name = " + field.getName());
                        }
                    } catch (IllegalAccessException e) {
                        System.err.println(CLASSNAME + ": Field is not accessible. Corresponding Parent Field name = " + field.getName());
                    }
                });

        System.out.println(CLASSNAME + ": Copied Story : " + story);
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

        System.out.println(CLASSNAME + ": Scenarios : " + scenarios.stream().map(sc -> sc.getTitle()).collect(Collectors.toList()));
        System.out.println(CLASSNAME+": Scenario Title "+scenarioTitle);

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
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
