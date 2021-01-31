package org.our.configuration;

import com.google.auto.value.AutoValue;
import org.jbehave.core.model.Scenario;

import java.util.Collections;
import java.util.Map;

@AutoValue
public abstract class ScenarioProperties {

    public abstract Map<String, String> getData();

    public abstract Scenario getScenario();

    public abstract String getStoryName();

    public static Builder newBuilder() {
        return new AutoValue_ScenarioProperties.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setScenario(Scenario scenario);

        abstract Map<String, String> getData();

        public abstract Builder setData(Map<String, String> data);

        public abstract Builder setStoryName(String storyName);

        abstract ScenarioProperties autoBuild();

        public ScenarioProperties build() {
            Map<String, String> immutableData =
                    Collections.unmodifiableMap(getData());
            setData(immutableData);
            return autoBuild();
        }
    }
}
