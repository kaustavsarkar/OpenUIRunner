package org.our.actions;

import com.google.auto.value.AutoValue;

import java.util.Set;

/**
 * Property values to be used for running a story from the list of stories being
 * provided by the client.
 * <p>
 * OUR may run multiple stories in a single execution. This class contains the
 * properties for a story being run at a particular time.
 */
@AutoValue
public abstract class StoryProperties {

    public static Builder newBuilder() {
        return new AutoValue_StoryProperties.Builder();
    }

    /**
     * Returns the name of the story being run by the OUR.
     */
    public abstract String getStoryName();

    /**
     * Returns the list of scenarios present in the story being run.
     */
    public abstract Set<ScenarioProperties> getScenarios();

    /**
     * Returns the properties which OUR is using for running.
     */
    public abstract OurProperties getOurProperties();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setStoryName(String storyName);

        public abstract Builder setScenarios(Set<ScenarioProperties> scenarios);

        public abstract Builder setOurProperties(OurProperties ourProperties);

        public abstract StoryProperties build();
    }
}
