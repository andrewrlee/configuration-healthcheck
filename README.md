configuration-healthcheck
=========================

This project provides a [Metrics](https://github.com/codahale/metrics) health check that triggers alerts when development configuration has been incorrectly applied to production environments.  

It is often common to have different configuration for production and development systems. It maybe that integration with external systems are disabled or that you allow additional hooks to aid automated testing. 

It is vital that this development functionality is not present in production and configuration should generally be managed in a way that this is not possible. Despite best endeavors sometimes mistakes slip through.  

This project is available from the central maven repository:

```xml
  <dependency>
    <groupId>uk.co.optimisticpanda</groupId>
    <artifactId>configuration-healthcheck</artifactId>
    <version>0.0.3</version>
  </dependency>
```


The ConfigurationHealthCheck takes an array of objects that can be passed or injected into it's constructor. When the health check is called, the objects are scanned in turn for accessor methods with annotations that specify what state the system should be in production. These methods are then invoked and the result is compared with the expected value as specified by the annotation.     

If any of these methods return unexpected results then the healthcheck fails and will return detailed information about why this is the case, as displayed below. 

There are 5 provided annotations. These annotations can be specified on any public 0-arg accessor. It also has support for static methods and methods returning Guava Optional values.

```java
    //This method should return a true value  (primitive or boolean). 
    @EnableInProduction
    public boolean shouldBeEnabled(){
        return false;
    }

    //This method should return a false value (primitive or boolean). 
    @DisableInProduction
    public boolean shouldBeDisabled(){
        return true;
    }

    //This method should return a result in production that isn't null or Optional.absent() 
    @ProvideInProduction
    public String getName(){
        return null;
    }

    //This method should return a result in production that is either null or Optional.absent() 
    @ExcludeInProduction
    public String shouldNoBeProvided(){
        return "Some value";
    }
    
    //This method should return a result in production that has a toString that matches the provided value 
    @ProductionValue("120")
    public int getValue(){
        return 123454;
    }
```

Given the configuration object as described above, the health check invocation would look a bit like this:
```
! Configuration Error: ERROR
!  TestHealthCheckInDropwizard#getName            [Expected: <A non-absent value>  , Actual: null      ] [Rule: ProvideInProduction] 
!  TestHealthCheckInDropwizard#getValue           [Expected: 120                   , Actual: 123454    ] [Rule: ProductionValue] 
!  TestHealthCheckInDropwizard#shouldBeEnabled    [Expected: true                  , Actual: false     ] [Rule: EnableInProduction] 
!  TestHealthCheckInDropwizard#shouldBeDisabled   [Expected: false                 , Actual: true      ] [Rule: DisableInProduction] 
!  TestHealthCheckInDropwizard#shouldNoBeProvided [Expected: <null or absent value>, Actual: Some value] [Rule: ExcludeInProduction] 
* deadlocks: OK
```

You can also specify custom annotations and their associated validation rules by creating an instance of a ConfigurationErrorReporter and providing it to the ConfigurationHealthCheck instance. 
   



  
