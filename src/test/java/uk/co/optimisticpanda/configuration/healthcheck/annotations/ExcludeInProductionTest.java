package uk.co.optimisticpanda.configuration.healthcheck.annotations;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.configuration.healthcheck.ConfigurationErrorReporter;
import uk.co.optimisticpanda.configuration.healthcheck.Violation;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ExcludeInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.NotPresentInProductionObject;

import com.google.common.base.Optional;

public class ExcludeInProductionTest {

    private ConfigurationErrorReporter reporter;

    @Before
    public void createReporter() {
        reporter = new ConfigurationErrorReporter();
    }
    
    @Test
    public void notPresentInProductionReturningNullHasNoError() {
        NotPresentInProductionObject annotatedInstance = new NotPresentInProductionObject(null);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }

    @Test
    public void notPresentInProductionReturningAbsentHasNoError() {
        NotPresentInProductionObject annotatedInstance = new NotPresentInProductionObject(Optional.absent());
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    
    @Test
    public void notPresentInProductionNotNullHasError() {
        NotPresentInProductionObject annotatedInstance = new NotPresentInProductionObject("sampleString");
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(ExcludeInProduction.class);
    }

    @Test
    public void notPresentInProductionPresentHasError() {
        NotPresentInProductionObject annotatedInstance = new NotPresentInProductionObject(Optional.of("sampleString"));
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(ExcludeInProduction.class);
    }
    
}
