package uk.co.optimisticpanda.configuration.healthcheck.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import uk.co.optimisticpanda.configuration.healthcheck.AnnotationAndInvocationResult;
import uk.co.optimisticpanda.configuration.healthcheck.Rule;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DisableInProduction {
    public static class DisableInProductionPredicate extends Rule {
        public boolean apply(AnnotationAndInvocationResult input) {
            return input.getResult() != null && input.getResult() instanceof Boolean && Boolean.FALSE.equals(input.getResult());
        }

        @Override
        public Class<? extends Annotation> getAnnotation() {
            return DisableInProduction.class;
        }

        @Override
        public String getExpectedValue(Annotation annotation) {
            return "false";
        }
    }
}
