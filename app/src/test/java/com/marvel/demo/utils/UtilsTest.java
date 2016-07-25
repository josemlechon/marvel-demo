package com.marvel.demo.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * Created by jose m lechon on 25/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class UtilsTest {

    @Test
    public void md5() throws Exception {

        String toMD5 = "12345";
        String md5Result = Utils.md5(toMD5);

        String md5Original = "827ccb0eea8a706c4c34a16891f84e7b";

        assertThat("MD5 does not work", md5Original, is(equalTo(md5Result)));
    }


    @Test
    public void removeNonDecimalValid() throws Exception {

        String test = "1,023.32 &euro;";
        String valid = "1,023.32";

        String result = Utils.removeNonDecimal(test);

        Assert.assertTrue(valid.equals(result));

    }

    @Test
    public void convertDoubleValid() throws Exception {

        Double valueValid = 10.53d;
        String test = "10.53 &euro;";

        Double result = Utils.convertDouble(test);

        assertThat("The Double cannot be 10.53", valueValid, is(equalTo(result)));
    }

    @Test
    public void convertBigDecimalValid() throws Exception {


        BigDecimal valueValid = new BigDecimal("10.53");
        String test = "10.53 &euro;";

        BigDecimal result = Utils.convertBigDecimal(test);

        assertThat("The big decimal cannot be 10.53", valueValid, is(equalTo(result)));
    }


}