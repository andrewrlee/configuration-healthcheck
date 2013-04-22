package uk.co.optimisticpanda.configuration.healthcheck;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.ChildOfDisabledInProductionObject;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.ClassWithStaticMethodsObject;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.HasAnnotationButNotConfigRelatedObject;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.HasParametersObject;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.HasVoidReturnObject;

public class ConfigurationErrorReporterTest {

    private ConfigurationErrorReporter reporter;

    @Before
    public void createReporter() {
        reporter = new ConfigurationErrorReporter();
    }
    
    @Test
    public void checkReturnsAnEmptyListOfViolations() {
        Object annotatedInstance = new Object();
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    
    @Test(expected=IllegalStateException.class)
    public void checkAnnotatedMethodHasVoidReturn() {
        HasVoidReturnObject annotatedInstance = new HasVoidReturnObject();
        reporter.getErrors(annotatedInstance);
    }
    
    @Test(expected=IllegalStateException.class)
    public void checkAnnotatedMethodHasParameterThrowsError() {
        HasParametersObject annotatedInstance = new HasParametersObject();
        reporter.getErrors(annotatedInstance);
    }
    
    @Test
    public void checkUnrelatedAnnotationsDoNotThrowError() {
        HasAnnotationButNotConfigRelatedObject annotatedInstance = new HasAnnotationButNotConfigRelatedObject();
        reporter.getErrors(annotatedInstance);
    }
    
    @Test
    public void checkConfigurationErrorsRelatedToParentClass() {
        ChildOfDisabledInProductionObject annotatedInstance = new ChildOfDisabledInProductionObject(true);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(DisableInProduction.class);
    }
    
    @Test
    public void checkStaticMethods() {
        ClassWithStaticMethodsObject annotatedInstance = new ClassWithStaticMethodsObject();
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(DisableInProduction.class);
    }

    //test static methods
}
