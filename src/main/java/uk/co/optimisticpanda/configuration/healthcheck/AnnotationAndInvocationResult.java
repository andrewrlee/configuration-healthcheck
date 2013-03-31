package uk.co.optimisticpanda.configuration.healthcheck;

import java.lang.annotation.Annotation;

public class AnnotationAndInvocationResult {
    private final Object result;
    private final Annotation annotation;

    public AnnotationAndInvocationResult(Annotation annotation, Object result) {
        this.result = result;
        this.annotation = annotation;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Object getResult() {
        return result;
    }
}