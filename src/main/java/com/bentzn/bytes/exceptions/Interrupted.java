package com.bentzn.bytes.exceptions;

public class Interrupted {

    static void testInterrupted() throws InterruptedException {
        Thread thr = new Thread() {
            public void run() {
                try {
                    System.out.println("Starting thread...");
                    System.out.println("    interrupted: " + isInterrupted());
                    System.out.println("    alive:       " + isAlive());
                    
                    // Now we interrupt the thread
                    interrupt();
                    
                    // ...but the thread actually continues!
                    System.out.println("After interrupt");
                    System.out.println("    interrupted: " + isInterrupted());
                    System.out.println("    alive:       " + isAlive());

                    // ...until it reaches somewhere, that _can_ be interrupted
                    sleep(50);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("After interrupt");
                    System.out.println("    interrupted: " + isInterrupted());
                    System.out.println("    alive:       " + isAlive());
                }
                
                try {
                    // The interrupted status is reset after the InterruptedException was thrown
                    sleep(50);
                }
                catch (InterruptedException e) {
                    System.out.println("Un-expected InterruptedException");
                }
                
                System.out.println("Thread finished");
            }
        };
        
        thr.start();
        thr.join();
        
        System.out.println("After join");
        System.out.println("    interrupted: " + thr.isInterrupted());
        System.out.println("    alive:       " + thr.isAlive());

    }
    
    
    public static void main(String[] args) throws InterruptedException {
        testInterrupted();
    }
}

