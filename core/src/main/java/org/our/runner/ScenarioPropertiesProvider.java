package org.our.runner;

import com.google.inject.Provider;
import org.our.configuration.ScenarioProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ScenarioPropertiesProvider
        implements Provider<ScenarioProperties> {
    private static final Logger logger =
            LoggerFactory.getLogger(ScenarioPropertiesProvider.class);
    private ScenarioProperties scenarioProperties;

    @Override
    public ScenarioProperties get() {
        logger.debug(this.toString());
        logger.debug("returning scenario property" + scenarioProperties);
        return scenarioProperties;
    }

    public void set(ScenarioProperties scenarioProperties) {
        logger.debug(this.toString());
        logger.debug("Setting scenario properties: "+ scenarioProperties);
        this.scenarioProperties = scenarioProperties;
    }
}
