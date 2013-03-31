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
public @interface ProductionValue {
    String value();

    public static class ProductionValuePredicate extends Rule {
        public boolean apply(AnnotationAndInvocationResult input) {
            ProductionValue annotation = (ProductionValue) input.getAnnotation();
            return input != null && String.valueOf(input.getResult()).equals(annotation.value());
        }

        @Override
        public Class<? extends Annotation> getAnnotation() {
            return ProductionValue.class;
        }

        @Override
        public String getExpectedValue(Annotation annotation) {
            return ((ProductionValue)annotation).value();
        }
    }
}
