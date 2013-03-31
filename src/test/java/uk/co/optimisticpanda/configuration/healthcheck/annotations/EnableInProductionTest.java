package uk.co.optimisticpanda.configuration.healthcheck.annotations;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.configuration.healthcheck.ConfigurationErrorReporter;
import uk.co.optimisticpanda.configuration.healthcheck.Violation;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.EnableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.EnabledInProductionObject;

public class EnableInProductionTest {

    private ConfigurationErrorReporter reporter;

    @Before
    public void createReporter() {
        reporter = new ConfigurationErrorReporter();
    }
    
    @Test
    public void enabledInProductionEnabledPrimitiveHasNoError() {
        EnabledInProductionObject annotatedInstance = new EnabledInProductionObject(true);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    
    @Test
    public void enabledInProductionEnabledWrapperHasNoError() {
        EnabledInProductionObject annotatedInstance = new EnabledInProductionObject(Boolean.TRUE);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    
    @Test
    public void enabledInProductionNullHasError() {
        EnabledInProductionObject annotatedInstance = new EnabledInProductionObject(null);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(EnableInProduction.class);
    }
    
    @Test
    public void enabledInProductionDisabledPrimitiveHasError() {
        EnabledInProductionObject annotatedInstance = new EnabledInProductionObject(false);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(EnableInProduction.class);
    }
    
    @Test
    public void enabledInProductionDisabledWrapperHasError() {
        EnabledInProductionObject annotatedInstance = new EnabledInProductionObject(Boolean.FALSE);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(EnableInProduction.class);
    }
    
    @Test
    public void enabledInProductionNotBooleanHasError() {
        EnabledInProductionObject annotatedInstance = new EnabledInProductionObject("sampleString");
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(EnableInProduction.class);
    }
    
}
