package com.example.valuepaljava.regexTest;

import org.junit.Test;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
public class regexTest {

    @Test
    public void testRegex(){
        String phrase = "Everett";
        String regex = "^[a-zA-Z]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phrase);
        assertTrue(matcher.find());

    }

}
