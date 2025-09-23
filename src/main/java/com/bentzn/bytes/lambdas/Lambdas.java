package com.bentzn.bytes.lambdas;

import static com.bentzn.bytes.lambdas.Lambdas.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.function.Consumer;

public class Lambdas {

    static NumberFormat format = new DecimalFormat("###.0");

    public static void main(String[] args) {
        // Elegant, simple and compact solution
        List.of(1, 2, "A").forEach(System.out::println);

        
        // An IllegalArgumentException (unchecked) is not a problem 
        try {
            DecFormatter decFormatter = new DecFormatter();
            List.of(1, 2, "A").stream().forEach(decFormatter::accept);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


        List<String> lstStr = List.of("1", "2", "A");
        DecParser decParser = new DecParser();

        // A checked Exception becomes clunky
        lstStr.stream().forEach(e -> {
            try {
                decParser.parse(e);
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        });


        // The for-each-loop is suddenly simpler
        // And so is the stacktrace...
        for(String str : lstStr) {
            try {
                decParser.parse(str);
            }
            catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }
}

class DecFormatter implements Consumer<Object> {

    @Override
    public void accept(Object t) {
        System.out.println(format.format(t));
    }
}

class DecParser {


    public void parse(String t) throws ParseException {
        System.out.println(format.parse(t));
    }
}
