package com.kea;

//This is an object that represents a task that can be passed as a parameter
// to a second thread we are starting. This runnable task will make our program run our CountdownTask
// simultaneously with the main thread, this way we can have the input scanner open in case the user
// needs to stop the process
public class CountDownTask implements Runnable{
    int secs = 10;

    //task constructor that takes the total of seconds we want our CountDown to last
    public CountDownTask(int x) {
        secs = x;
    }

    @Override
    public void run() {
        try {
            //what we want our task to run when we call it,
            //it will iterate "secs" amount of times with an interval of 1 second per iteration
            while (secs > 0) {
                System.out.print("\r"+secs);
                Thread.sleep(1000); //1000 millis = 1 second
                secs--;
            }
            System.out.println("\r "); //this just clears up the line in the console
        } catch (Exception e) {
         }
    }
}