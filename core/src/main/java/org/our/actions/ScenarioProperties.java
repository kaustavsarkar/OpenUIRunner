package org.our.actions;

import org.jbehave.core.model.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScenarioProperties extends Scenario {
    private static final Logger logger =
            LoggerFactory.getLogger(ScenarioProperties.class);
    private Map<String, String> data;

    public static ScenarioProperties cloneFromParent(Scenario scenario) {
        ScenarioProperties scenarioProp = new ScenarioProperties();

        Field[] fields = scenario.getClass().getDeclaredFields();

        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            try {
                Field storyField = scenarioProp.getClass()
                        .getDeclaredField(field.getName());
                storyField.setAccessible(true);
                storyField.set(scenarioProp, field.get(scenario));
            } catch (NoSuchFieldException e) {
                logger.warn("Field not found, though present in Scenario. " +
                        field.getName());
                logger.info("Checking in Parent: " + scenario.getClass());

                try {
                    Field parentField = scenarioProp.getClass().getSuperclass()
                            .getDeclaredField(field.getName());
                    parentField.setAccessible(true);
                    parentField.set(scenarioProp, field.get(scenario));
                } catch (NoSuchFieldException ex) {
                    logger.warn(
                            "Field not found in Scenario Class either. Check if it still extends Scenario");
                } catch (IllegalAccessException ex) {
                    logger.error("Field is not accessible in Parent. " +
                            "Corresponding Field name: " + field.getName());
                }

            } catch (IllegalAccessException e) {
                logger.error("Field is not accessible in Parent. " +
                        "Corresponding Field name: " + field.getName());
            }
        });


        return scenarioProp;
    }

    public Map<String, String> getData() {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        return this.data;
    }

    @Override
    public String toString() {
        return "ScenarioProperties{" +
                "data=" + data +
                "} " + super.toString();
    }
}
