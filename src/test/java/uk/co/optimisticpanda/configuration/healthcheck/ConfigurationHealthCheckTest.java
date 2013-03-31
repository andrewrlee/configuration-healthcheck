package uk.co.optimisticpanda.configuration.healthcheck;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.configuration.healthcheck.ConfigurationErrorReporter;
import uk.co.optimisticpanda.configuration.healthcheck.ConfigurationHealthCheck;
import uk.co.optimisticpanda.configuration.healthcheck.Description;
import uk.co.optimisticpanda.configuration.healthcheck.Violation;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValue;

import com.google.common.collect.Lists;
import com.yammer.metrics.core.HealthCheck.Result;


public class ConfigurationHealthCheckTest {

    private ConfigurationErrorReporter reporter;
    private ConfigurationHealthCheck healthCheck;

    @Before
    public void createHealthCheck(){
        reporter = mock(ConfigurationErrorReporter.class);
    }

    @Test
    public void checkReturnsFailureWithOneError(){
        Object testObject = new Object();
        healthCheck = new ConfigurationHealthCheck(reporter, testObject);
        
        Description description = new Description(ProductionValue.class, "expectedValue1", this.getClass(),"doSomething" ,"unexpectedValue1");
        Violation violation = new Violation(description) ;
        when(reporter.getErrors(testObject)).thenReturn(Lists.newArrayList(violation));
        
        assertThat(healthCheck.check()).isEqualTo(getResultFromViolations(violation));
    }
    
    private Result getResultFromViolations(Violation... violations){
        Collection<Description> descriptions = Violation.descriptionsOf(Arrays.asList(violations));
        return Result.unhealthy(Description.asString(descriptions));
    }
    
    @Test
    public void checkReturnsFailureWithTwoErrorsFromOneObject(){
        Object testObject = new Object();
        healthCheck = new ConfigurationHealthCheck(reporter, testObject);
        
        Description description1 = new Description(ProductionValue.class, "expectedValue1", this.getClass(),"doSomething" ,"unexpectedValue1");
        Description description2 = new Description(ProductionValue.class, "expectedValue2", this.getClass(),"doSomethingElse" ,"unexpectedValue2");
        Violation violation1 = new Violation(description1) ;
        Violation violation2 = new Violation(description2) ;
        when(reporter.getErrors(testObject)).thenReturn(Lists.newArrayList(violation1, violation2));
        
        assertThat(healthCheck.check()).isEqualTo(getResultFromViolations(violation1, violation2));
    }
    
    @Test
    public void checkReturnsSuccessWithTwoObjects(){
        healthCheck = new ConfigurationHealthCheck(reporter, "obj1", "obj2");
        
        when(reporter.getErrors("obj1")).thenReturn(Collections.<Violation>emptyList());
        when(reporter.getErrors("obj2")).thenReturn(Collections.<Violation>emptyList());
        
        assertThat(healthCheck.check()).isEqualTo(Result.healthy());
    }

    
    @Test
    public void checkReturnsFailureWhenErrorFromSecondObject(){
        healthCheck = new ConfigurationHealthCheck(reporter, "obj1", "obj2");
        
        Description description = new Description(ProductionValue.class, "expectedValue1", this.getClass(),"doSomething" ,"unexpectedValue1");
        Violation violation = new Violation(description) ;
        when(reporter.getErrors("obj1")).thenReturn(Collections.<Violation>emptyList());
        when(reporter.getErrors("obj2")).thenReturn(Lists.newArrayList(violation));
        
        assertThat(healthCheck.check()).isEqualTo(getResultFromViolations(violation));
    }
    
    
    @Test
    public void checkReturnsSuccessWithNoErrors(){
        healthCheck = new ConfigurationHealthCheck(reporter);
        
        assertThat(healthCheck.check()).isEqualTo(Result.healthy());
    }
    
}
