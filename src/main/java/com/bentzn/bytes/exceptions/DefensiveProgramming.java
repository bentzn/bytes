package com.bentzn.bytes.exceptions;

public class DefensiveProgramming {

    public static String getSomething(String param) {
        if (param == null)
            return null; // Not a good idea

        return param + "_extra";
    }
}
