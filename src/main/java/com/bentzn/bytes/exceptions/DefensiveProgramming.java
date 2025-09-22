package com.bentzn.bytes.exceptions;

public class DefensiveProgramming {

    public static String getSomething(String param) {
        if (param == null)
            return null; // Not a good idea

        return param + "_extra";
    }
    
    
    public static void main(String[] args) {
        System.out.println(getSomething("ABCD"));
        System.out.println(getSomething(null));
    }
}
