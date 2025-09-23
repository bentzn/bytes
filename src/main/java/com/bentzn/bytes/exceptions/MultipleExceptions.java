package com.bentzn.bytes.exceptions;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class MultipleExceptions {
    public static void main(String[] args) {
        try {
            new MultipleExceptions2().storeSomething("request 123");
        }
        catch (IOException e) {
            // No need to alert the user
        }
        catch (DataFormatException e) {
            // Alert the user that it was not possible to store Something
        }
    }
}

class MultipleExceptions2 {

    private String getFromServer(String request) throws DataFormatException {
        // gets something from a server - can throw a DataFormatException
        return "result from server";
    }



    private void persistSomething(String something) throws IOException {
        // will persist to disk
        // Let's imagine this operation is not essential (caching)
    }



    String storeSomething(String result) throws IOException, DataFormatException {
        String result2 = getFromServer(result);
        persistSomething(result2);
        return result2;
    }
}

