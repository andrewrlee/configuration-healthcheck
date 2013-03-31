package uk.co.optimisticpanda.configuration.healthcheck;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
    
    //test inherited methods
}
