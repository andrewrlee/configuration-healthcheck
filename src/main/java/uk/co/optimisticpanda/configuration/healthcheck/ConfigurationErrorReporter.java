package uk.co.optimisticpanda.configuration.healthcheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.EnableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ExcludeInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValue;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProvideInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProduction.DisableInProductionPredicate;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.EnableInProduction.EnableInProductionPredicate;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ExcludeInProduction.ExcludeInProductionPredicate;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValue.ProductionValuePredicate;
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
        for (Method method : annotatedInstance.getClass().getDeclaredMethods()) {
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
        } catch (IllegalArgumentException e) {
            throw Throwables.propagate(e);
        } catch (IllegalAccessException e) {
            throw Throwables.propagate(e);
        } catch (InvocationTargetException e) {
            throw Throwables.propagate(e);
        }
    }

    private void checkMethod(Method method) {
        if (method.getParameterTypes().length > 0) {
            throw new IllegalStateException("Method named " + method.getName() + " has parameters. Must be a getter.");
        }
        if (method.getReturnType().equals(Void.TYPE)) {
            throw new IllegalStateException("Method named " + method.getName() + " has void return type. Must be a getter.");
        }
    }
}
