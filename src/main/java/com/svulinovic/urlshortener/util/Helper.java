package com.svulinovic.urlshortener.util;

import org.springframework.validation.FieldError;

import java.util.List;

public class Helper {

    public static String errorsToString(List<FieldError> errors){
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (FieldError error : errors ) {
            sb.append(delim).append(error.getField() + " " + error.getDefaultMessage());
            delim = ", ";
        }
        return "(" + sb.toString() + ")";
    }

}
