package uk.co.optimisticpanda.configuration.healthcheck;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public class Description {
    private final Class<? extends Annotation> annotation;
    private final String result;
    private final String expectedValue;
    private final String methodLocation;

    public Description(Class<? extends Annotation> annotation, String expectedValue, Class<? extends Object> clazz, String methodName, Object result) {
        this.annotation = annotation;
        this.expectedValue = String.valueOf(expectedValue);
        this.methodLocation = clazz.getSimpleName() + "#" + methodName;
        this.result = String.valueOf(result);
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    static int getLongestMethodLocation(Collection<Description> descriptions){
        Description max = Collections.max(descriptions, new Comparator<Description>() {
            public int compare(Description o1, Description o2) {
                return Ints.compare(o1.methodLocation.length(), o2.methodLocation.length());
            }
        });
        return max.methodLocation.length();
    }
    
    static int getLongestExpectedValue(Collection<Description> descriptions){
        Description max = Collections.max(descriptions, new Comparator<Description>() {
            public int compare(Description o1, Description o2) {
                return Ints.compare(o1.expectedValue.length(), o2.expectedValue.length());
            }
        });
        return max.expectedValue.length();
    }
    
    static int getLongestResult(Collection<Description> descriptions){
        Description max = Collections.max(descriptions, new Comparator<Description>() {
            public int compare(Description o1, Description o2) {
                return Ints.compare(o1.result.length(), o2.result.length());
            }
        });
        return max.result.length();
    }

    public String describe(int longestLocation, int longestExpectedValue, int longestResult) {
        String location = String.format("%-" + longestLocation +"s " , methodLocation);
        String expected = String.format("[Expected: %-" + longestExpectedValue  +"s, Actual: %-" +longestResult+ "s] " , expectedValue, result);
        String rule = String.format("[Rule: %s] " , annotation.getSimpleName() );
        return location + expected +  rule; 
    }
    
    public static String asString(Collection<Description> descriptions) {
        int longestExpectedValue = getLongestExpectedValue(descriptions);
        int longestMethodLocation = getLongestMethodLocation(descriptions);
        int longestResult = getLongestResult(descriptions);
        
        List<String> values = Lists.newArrayList();
        for (Description description : descriptions) {
            values.add(description.describe(longestMethodLocation, longestExpectedValue, longestResult));
        }
        
        return Joiner.on("\n!  ").join(values);
    }
}