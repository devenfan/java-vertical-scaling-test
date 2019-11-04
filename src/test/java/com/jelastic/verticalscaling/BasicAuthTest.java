package com.jelastic.verticalscaling;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * BasicAuthTest
 *
 * @author Deven
 * @version : BasicAuthTest, v 0.1 2019-10-30 16:33 Deven Exp$
 */
public class BasicAuthTest {

    @Test
    public void test() throws UnsupportedEncodingException {
        String key = "iflight.java.dsf.searchfrontapi:iflight.java.dsf.searchfrontapi";
        System.out.println(Base64Codec.encrypt(key.getBytes("UTF-8")));
        //aWZsaWdodC5qYXZhLmRzZi5zZWFyY2hmcm9udGFwaTppZmxpZ2h0LmphdmEuZHNmLnNlYXJjaGZyb250YXBp
        //aWZsaWdodC5qYXZhLmRzZi5zZWFyY2hmcm9udGFwaTppZmxpZ2h0LmphdmEuZHNmLnNlYXJjaGZyb250YXBp
        //curl --basic -u
    }

}
