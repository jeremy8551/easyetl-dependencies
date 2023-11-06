package icu.etl.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NumbersTest {

    @Test
    public void test1() {
        assertTrue(Numbers.plus(Double.valueOf("13767.61"), Double.valueOf("603.04")) == 14370.65);
        assertTrue(Numbers.subtract(Double.valueOf("13767.61"), Double.valueOf("603.04")) == 13164.57);
        assertTrue(Numbers.multiply(Double.valueOf("13767.61"), Double.valueOf("34.5")) == 474982.545);
        assertTrue(Numbers.divide(Double.valueOf("13767.61"), Double.valueOf("2")) == 6883.805);
        assertTrue(Numbers.mod(Double.valueOf("13.0"), Double.valueOf("2")) == 1);
    }

}
