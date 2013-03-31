configuration-healthcheck
=========================

A metrics health check that should trigger alerts when development configuration has been incorrectly applied to production environments.  


```
! Configuration Error: ERROR
!  TestHealthCheckInDropwizard#getName            [Expected: <A non-absent value>  , Actual: null      ] [Rule: ProvideInProduction] 
!  TestHealthCheckInDropwizard#getValue           [Expected: 120                   , Actual: 123454    ] [Rule: ProductionValue] 
!  TestHealthCheckInDropwizard#shouldBeEnabled    [Expected: true                  , Actual: false     ] [Rule: EnableInProduction] 
!  TestHealthCheckInDropwizard#shouldBeDisabled   [Expected: false                 , Actual: true      ] [Rule: DisableInProduction] 
!  TestHealthCheckInDropwizard#shouldNoBeProvided [Expected: <null or absent value>, Actual: Some value] [Rule: ExcludeInProduction] 
* deadlocks: OK
```
