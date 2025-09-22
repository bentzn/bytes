package com.bentzn.bytes.exceptions;

public class Interrupted {

    static void testInterrupted() throws InterruptedException {
        Thread thr = new Thread() {
            public void run() {
                try {
                    System.out.println("Starting thread...");
                    
                    // Now we interrupt the thread
                    interrupt();
                    
                    // ...but the thread actually continues!
                    System.out.println("After interrupt: " + isInterrupted());
                    
                    // ...until it reaches somewhere, that _can_ be interrupted
                    sleep(50);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        thr.start();
        thr.join();
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        testInterrupted();
    }
}

