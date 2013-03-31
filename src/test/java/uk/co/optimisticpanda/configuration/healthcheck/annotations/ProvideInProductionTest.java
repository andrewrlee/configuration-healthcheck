package uk.co.optimisticpanda.configuration.healthcheck.annotations;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.configuration.healthcheck.ConfigurationErrorReporter;
import uk.co.optimisticpanda.configuration.healthcheck.Violation;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProvideInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.PresentInProductionObject;

import com.google.common.base.Optional;

public class ProvideInProductionTest {

    private ConfigurationErrorReporter reporter;

    @Before
    public void createReporter() {
        reporter = new ConfigurationErrorReporter();
    }

    @Test
    public void presentInProductionReturningNullHasError() {
        PresentInProductionObject annotatedInstance = new PresentInProductionObject(null);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(ProvideInProduction.class);
    }
    
    @Test
    public void presentInProductionReturningAbsentHasError() {
        PresentInProductionObject annotatedInstance = new PresentInProductionObject(Optional.absent());
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(ProvideInProduction.class);
    }
    
    @Test
    public void presentInProductionNotNullHasNoError() {
        PresentInProductionObject annotatedInstance = new PresentInProductionObject("sampleString");
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    
    @Test
    public void presentInProductionPresentHasNoError() {
        PresentInProductionObject annotatedInstance = new PresentInProductionObject(Optional.of("sampleString"));
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }

    
}
