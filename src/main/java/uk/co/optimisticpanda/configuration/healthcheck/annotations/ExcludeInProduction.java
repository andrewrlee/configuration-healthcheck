package uk.co.optimisticpanda.configuration.healthcheck.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import uk.co.optimisticpanda.configuration.healthcheck.AnnotationAndInvocationResult;
import uk.co.optimisticpanda.configuration.healthcheck.Rule;

import com.google.common.base.Optional;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcludeInProduction {
    public static class ExcludeInProductionPredicate extends Rule {
        public boolean apply(AnnotationAndInvocationResult input) {
            return input.getResult() == null || input.getResult() == Optional.absent();
        }

        @Override
        public Class<? extends Annotation> getAnnotation() {
            return ExcludeInProduction.class;
        }

        @Override
        public String getExpectedValue(Annotation annotation) {
            return "<null or absent value>";
        }
    }
}
