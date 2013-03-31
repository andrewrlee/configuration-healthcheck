package uk.co.optimisticpanda.configuration.healthcheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


import com.google.common.base.Predicate;

public abstract class Rule implements Predicate<AnnotationAndInvocationResult>{

    /**Return a description that describes the violation*/
    public Description getDescription(Annotation annotation, Object instance, Method method, Object result){
        return new Description(getAnnotation(), getExpectedValue(annotation),instance.getClass(), method.getName(), result);
    }
    
    /**Given the annotation of the executing rule, provide the expected result*/
    public abstract String getExpectedValue(Annotation annotation);
    
    /**Return the type of annotation that this rule deals with*/
    public abstract Class<? extends Annotation> getAnnotation();
}
