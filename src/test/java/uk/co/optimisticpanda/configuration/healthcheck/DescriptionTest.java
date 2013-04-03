package uk.co.optimisticpanda.configuration.healthcheck;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class DescriptionTest {

    @Test
    public void checkLongestMethodLocation(){
        Description d1 = withMethod(String.class, "length");
        Description d2 = withMethod(String.class, "longerLength");
        int longestMethodLocation = Description.getLongestMethodLocation(Arrays.asList(d1,d2));
        assertEquals("String#longerLength".length(), longestMethodLocation);
    }
    
    @Test
    public void checkLongestResult(){
        Description d1 = withResult("result");
        Description d2 = withResult("longerResult");
        int longestResult = Description.getLongestResult((Arrays.asList(d1,d2)));
        assertEquals("longerResult".length(), longestResult);
    }
    
    @Test
    public void checkLongestExpectedValue(){
        Description d1 = withExpectedValue("value");
        Description d2 = withExpectedValue("longerValue");
        int longestExpectedValue = Description.getLongestExpectedValue(Arrays.asList(d1,d2));
        assertEquals("longerValue".length(), longestExpectedValue);
    }
    
    @Test
    public void checkNullExpectedValueHandledSafely(){
        Description d1 = withExpectedValue(null);
        int longestExpectedValue = Description.getLongestExpectedValue(Arrays.asList(d1));
        assertEquals("null".length(), longestExpectedValue);
    }

    @Test
    public void checkNullResultHandledSafely(){
        Description d1 = withResult(null);
        int longestResult = Description.getLongestResult(Arrays.asList(d1));
        assertEquals("null".length(), longestResult);
    }
    
    private Description withMethod(Class<?> clazz, String methodName){
        return new Description(Override.class, null, clazz, methodName, null);
    }
    
    private Description withResult(Object result){
        return new Description(Override.class, null, String.class, "", result);
    }
    
    private Description withExpectedValue(String value){
        return new Description(Override.class, value, String.class, "", "result");
    }
    
}
