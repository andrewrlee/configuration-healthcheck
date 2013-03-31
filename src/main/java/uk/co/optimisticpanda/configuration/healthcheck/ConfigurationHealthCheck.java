package uk.co.optimisticpanda.configuration.healthcheck;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.yammer.metrics.core.HealthCheck;

public class ConfigurationHealthCheck extends HealthCheck {

    private final ConfigurationErrorReporter reporter;
    private final Object[] objectsToCheck;

    public ConfigurationHealthCheck(ConfigurationErrorReporter reporter, Object... objectsToCheck) {
        super("Configuration Error");
        this.reporter = reporter;
        this.objectsToCheck = objectsToCheck;
    }

    public ConfigurationHealthCheck(Object... objectsToCheck) {
        this(new ConfigurationErrorReporter(), objectsToCheck);
    }

    public Result check() {
        ArrayList<Violation> violations = Lists.newArrayList();
        for (Object object : objectsToCheck) {
            violations.addAll(reporter.getErrors(object));
        }
        if (!violations.isEmpty()) {
            return Result.unhealthy(Violation.toString(violations));
        }
        return Result.healthy();
    }

}
