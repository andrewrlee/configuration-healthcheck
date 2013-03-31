package uk.co.optimisticpanda.configuration.healthcheck.annotations;

import uk.co.optimisticpanda.configuration.healthcheck.annotations.DisableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.EnableInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ExcludeInProduction;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProductionValue;
import uk.co.optimisticpanda.configuration.healthcheck.annotations.ProvideInProduction;

public class TestInstances {

    public static class PresentInProductionObject {

        private Object value;

        public PresentInProductionObject(Object value) {
            this.value = value;
        }

        @ProvideInProduction
        public Object getter() {
            return value;
        }
    }
    public static class EnabledInProductionObject {
        
        private Object value;
        
        public EnabledInProductionObject(Object value) {
            this.value = value;
        }
        
        @EnableInProduction
        public Object getter() {
            return value;
        }
    }
    
    public static class DisabledInProductionObject {
        
        private Object value;
        
        public DisabledInProductionObject(Object value) {
            this.value = value;
        }
        
        @DisableInProduction
        public Object getter() {
            return value;
        }
    }

    public static class NotPresentInProductionObject {
        
        private Object value;
        
        public NotPresentInProductionObject(Object value) {
            this.value = value;
        }
        
        @ExcludeInProduction
        public Object getter() {
            return value;
        }
    }
    
    public static class HasParametersObject {
        @ExcludeInProduction
        public Object getter(String shouldNotHaveAParam) {
            return null;
        }
    }

    public static class HasVoidReturnObject {
        @ExcludeInProduction
        public void getter() {
        }
    }
    public static class HasAnnotationButNotConfigRelatedObject {
        @Deprecated
        public void getter() {
        }
    }
    
    public static class ValueInProductionObject{
        public static final String VALUE = "this is my value";
        private final Object value;
        
        public ValueInProductionObject(Object value){
            this.value = value;
        }
        
        @ProductionValue(VALUE)
        public Object getter() {
            return value;
        }
    }
    public static class ValueInProductionIntObject{
        private final Object value;
        
        public ValueInProductionIntObject(Object value){
            this.value = value;
        }
        
        @ProductionValue("400")
        public Object getter() {
            return value;
        }
    }
}
