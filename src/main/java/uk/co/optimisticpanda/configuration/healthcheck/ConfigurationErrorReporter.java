package uk.co.optimisticpanda.configuration.healthcheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProduction.DisableInProductionPredicate;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.EnableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.EnableInProduction.EnableInProductionPredicate;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ExcludeInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ExcludeInProduction.ExcludeInProductionPredicate;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValue;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValue.ProductionValuePredicate;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProvideInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProvideInProduction.ProvideInProductionPredicate;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ConfigurationErrorReporter {

    private Map<Class<? extends Annotation>, Rule> map = Maps.newHashMap();

    public ConfigurationErrorReporter() {
        map.put(ProvideInProduction.class, new ProvideInProductionPredicate());
        map.put(ExcludeInProduction.class, new ExcludeInProductionPredicate());
        map.put(EnableInProduction.class, new EnableInProductionPredicate());
        map.put(DisableInProduction.class, new DisableInProductionPredicate());
        map.put(ProductionValue.class, new ProductionValuePredicate());
    }

    public ConfigurationErrorReporter(Map<Class<? extends Annotation>, Rule> rules) {
        this.map = rules;
    }

    public List<Violation> getErrors(Object annotatedInstance) {
        List<Violation> violations = Lists.newArrayList();
        for (Method method : gatherMethods(annotatedInstance.getClass())) {
            for (Annotation annotation : method.getAnnotations()) {
                Optional<Rule> rule = getRule(annotation);
                if (rule.isPresent()) {
                    checkMethod(method);
                    checkResult(annotation, violations, rule.get(), annotatedInstance, method);
                }
            }
        }
        return violations;
    }

    private Optional<Rule> getRule(Annotation annotation) {
        return Optional.<Rule> fromNullable(map.get(annotation.annotationType()));
    }
    
    private void checkResult(Annotation annotation, List<Violation> results, Rule rule, Object instance, Method method) {
        try {
            Object result = method.invoke(instance);
            if (!rule.apply(new AnnotationAndInvocationResult(annotation, result))) {
                Description description = rule.getDescription(annotation, instance, method, result);
                results.add(new Violation(description));
            }
        } catch (Exception e) {
            throw Throwables.propagate(e);
        } 
    }

    private void checkMethod(Method method) {
        if (method.getParameterTypes().length > 0 || method.getReturnType().equals(Void.TYPE)) {
            throw new IllegalStateException("Method named " + method.getName() + " must be a standard getter.");
        }
    }

    private List<Method> gatherMethods(Class<?> clazz) {
        List<Method> result = Lists.newArrayList();
        recursivlyFindMethods(clazz, result);
        return result;
    }
    
    private void recursivlyFindMethods(Class<?> clazz, List<Method> result) {
        result.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        if(clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class){
            recursivlyFindMethods(clazz.getSuperclass(), result);
        }
    }
    
}
