package org.jerkar.examples.capitalizer;

import org.junit.Assert;
import org.junit.Test;

public class CapitalizerTest {

    @Test
    public void test() {
        String sample = "hello world";
        Assert.assertEquals("Hello World", Capitalizer.capitalize(sample));
    }

}
