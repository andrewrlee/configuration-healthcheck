package uk.co.optimisticpanda.configuration.healthcheck.dropwizard;

import javax.ws.rs.Path;

import uk.co.optimisticpanda.configuration.healthcheck.ConfigurationHealthCheck;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.EnableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ExcludeInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValue;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProvideInProduction;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;

@Path("/")
public class TestHealthCheckInDropwizard extends Service<Configuration>{

    @ProvideInProduction
    public String getName(){
        return null;
    }
    
    @ProductionValue("120")
    public int getValue(){
        return 123454;
    }
    
    @EnableInProduction
    public boolean shouldBeEnabled(){
        return false;
    }
    
    @DisableInProduction
    public static boolean shouldBeDisabled(){
        return true;
    }
    
    @ExcludeInProduction
    public String shouldNoBeProvided(){
        return "Some value";
    }
    
    @Override
    public void initialize(Bootstrap<Configuration> arg0) {
    }

    @Override
    public void run(Configuration config, Environment env) throws Exception {
        env.addResource(this);
        env.addHealthCheck(new ConfigurationHealthCheck(this));
    }

    public static void main(String[] args) throws Exception {
        new TestHealthCheckInDropwizard().run(new String[]{"server"});
    }
}
