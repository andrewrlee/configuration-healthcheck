package uk.co.optimisticpanda.configuration.healthcheck;

import static com.google.common.collect.Collections2.transform;
import static uk.co.optimisticpanda.configuration.healthcheck.Description.asString;

import java.util.Collection;

import com.google.common.base.Function;
public class Violation {

    private final Description description;

    public Violation(Description description) {
        this.description = description;
    }

    public Class<?> getType() {
        return description.getAnnotation();
    }

    public static String toString(Collection<Violation> violations) {
        return asString(descriptionsOf(violations));
    }

    static Collection<Description> descriptionsOf(Collection<Violation> violations) {
        return transform(violations, new Function<Violation, Description>() {
            public Description apply(Violation input) {
                return input.description;
            }
        });
    }
}
