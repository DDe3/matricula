package com.ing_software.utils;


import java.util.regex.Pattern;

public class Patterns {

    public static final Pattern emailPattern = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
    public static final Pattern passwordPattern = Pattern.compile(
            "(?=.*[A-Z])" +  //At least one upper case character (A-Z)
                    "(?=.*[a-z])" +     //At least one lower case character (a-z)
                    "(?=.*\\d)" +   //At least one digit (0-9)
                    ".*" +
                    "[a-zA-Z\\d]$");   // Password should not end with a special character
    public static final Pattern telefonoPattern = Pattern.compile("^\\d{9}$");
    public static final Pattern cdiPattern = Pattern.compile("^\\d{10}$");

    public static final Pattern paraleloPattern = Pattern.compile("^[A-Za-z]{1}");

    public static final Pattern aulaPattern = Pattern.compile("^[A-Za-z]{1}+-[0-9]{1,2}");

}
