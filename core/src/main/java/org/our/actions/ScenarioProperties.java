package org.our.actions;

import com.google.auto.value.AutoValue;
import org.jbehave.core.model.Scenario;

import java.util.Collections;
import java.util.Map;

@AutoValue
public abstract class ScenarioProperties {

    public abstract Map<String, String> getData();

    public abstract Scenario getScenario();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setScenario(Scenario scenario);

        abstract Map<String, String> getData();

        public abstract Builder setData(Map<String, String> data);

        abstract ScenarioProperties autoBuild();

        public ScenarioProperties build() {
            Map<String, String> immutableData =
                    Collections.unmodifiableMap(getData());
            setData(immutableData);
            return autoBuild();
        }
    }
}
