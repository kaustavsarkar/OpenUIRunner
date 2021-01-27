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
    private final Injector injector;

    public CustomStepFactory(Configuration configuration,
                             Class<? extends Steps> root,
                             Injector injector) {
        super(configuration, root);
        this.injector = injector;
    }

    @Override
    public Object createInstanceOfType(Class<?> type) {
        Object instance;
        try {
            instance = injector.getInstance(type);
        } catch (Exception e) {
            logger.error("Failed while creating instance of Actions", e);
            throw new StepsInstanceNotFound(type, this);
        }
        return instance;
    }
}
