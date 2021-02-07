package org.our.runner.stepfactory;

import com.google.inject.Injector;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.steps.ScanningStepsFactory;
import org.jbehave.core.steps.Steps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomStepFactory extends ScanningStepsFactory {
    private static final Logger logger =
            LoggerFactory.getLogger(CustomStepFactory.class);
    private final Injector childInjector;

    public CustomStepFactory(Configuration configuration,
                             Class<? extends Steps> root,
                             Injector injector) {
        super(configuration, root);
        this.childInjector = injector;
    }

    @Override
    public Object createInstanceOfType(Class<?> type) {
        logger.info("Creating instance of " + type);
        try {
            return childInjector.getInstance(type);
        } catch (Exception e) {
            logger.error("Failed while creating instance of Actions", e);
            logger.error(childInjector.getAllBindings().toString());
            throw new StepsInstanceNotFound(type, this);
        }
    }
}
