package uk.co.optimisticpanda.configuration.healthcheck.annotations;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.configuration.healthcheck.ConfigurationErrorReporter;
import uk.co.optimisticpanda.configuration.healthcheck.Violation;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.DisabledInProductionObject;

public class DisableInProductionTest {

    private ConfigurationErrorReporter reporter;

    @Before
    public void createReporter() {
        reporter = new ConfigurationErrorReporter();
    }
    
    @Test
    public void disabledInProductionDisabledPrimitiveHasNoError() {
        DisabledInProductionObject annotatedInstance = new DisabledInProductionObject(false);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    @Test
    public void disabledInProductionDisabledPrimitiveWrapperHasNoError() {
        DisabledInProductionObject annotatedInstance = new DisabledInProductionObject(Boolean.FALSE);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    
    @Test
    public void disabledInProductionNullHasError() {
        DisabledInProductionObject annotatedInstance = new DisabledInProductionObject(null);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(DisableInProduction.class);
    }
    
    @Test
    public void disabledInProductionEnabledPrimitiveHasError() {
        DisabledInProductionObject annotatedInstance = new DisabledInProductionObject(true);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(DisableInProduction.class);
    }
    
    @Test
    public void disabledInProductionEnabledWrapperHasError() {
        DisabledInProductionObject annotatedInstance = new DisabledInProductionObject(Boolean.TRUE);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(DisableInProduction.class);
    }

    @Test
    public void disabledInProductionNotBooleanHasError() {
        DisabledInProductionObject annotatedInstance = new DisabledInProductionObject("sampleString");
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(DisableInProduction.class);
    }
    
}
