package uk.co.optimisticpanda.configuration.healthcheck.annotations;

import static org.fest.assertions.Assertions.assertThat;
import static uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.ValueInProductionObject.VALUE;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.configuration.healthcheck.ConfigurationErrorReporter;
import uk.co.optimisticpanda.configuration.healthcheck.Violation;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValue;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.ValueInProductionIntObject;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.TestInstances.ValueInProductionObject;
public class ProductionValueTest {

    private ConfigurationErrorReporter reporter;

    @Before
    public void createReporter() {
        reporter = new ConfigurationErrorReporter();
    }
    
    @Test
    public void valueInProductionStringIsNullHasError() {
        ValueInProductionObject annotatedInstance = new ValueInProductionObject(null);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(ProductionValue.class);
    }
    
    @Test
    public void valueInProductionIsNotCorrectHasError() {
        ValueInProductionObject annotatedInstance = new ValueInProductionObject("not same as value in prod");
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(ProductionValue.class);
    }
    
    @Test
    public void valueInProductionIsCorrectForNonStringTypes() {
        ValueInProductionIntObject annotatedInstance = new ValueInProductionIntObject(400);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    
    @Test
    public void valueInProductionIsIncorrectForNonStringTypes() {
        ValueInProductionIntObject annotatedInstance = new ValueInProductionIntObject(399);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).hasSize(1);
        assertThat(violations.get(0).getType()).isEqualTo(ProductionValue.class);
    }
    
    @Test
    public void valueInProductionIsCorrectHasNoError() {
        ValueInProductionObject annotatedInstance = new ValueInProductionObject(VALUE);
        List<Violation> violations = reporter.getErrors(annotatedInstance);
        assertThat(violations).isEmpty();
    }
    
}