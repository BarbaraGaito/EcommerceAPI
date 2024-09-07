package com.uade.tpo.demo.service;

import java.util.regex.Pattern;

public class EmailValidation {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        return email != null && pattern.matcher(email).matches();
    }
    
}
