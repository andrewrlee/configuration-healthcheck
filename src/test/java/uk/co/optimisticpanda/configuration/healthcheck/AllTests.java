package uk.co.optimisticpanda.configuration.healthcheck;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProductionTest;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.EnableInProductionTest;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ExcludeInProductionTest;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValueTest;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProvideInProductionTest;

@RunWith(Suite.class)
@SuiteClasses({  //
    ConfigurationErrorReporterTest.class, //
    DisableInProductionTest.class, //
    EnableInProductionTest.class, //
    ExcludeInProductionTest.class, //
    ProductionValueTest.class, //
    ProvideInProductionTest.class,
    ConfigurationHealthCheckTest.class,
    DescriptionTest.class
    })
public class AllTests {

}
