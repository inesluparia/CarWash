package com.kea;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Station {
    private String name;
    private Scanner input;
    private Card currentCard = null;
    private ArrayList<WashType> washTypes;
    private LocalTime now;
    int adminPin = 1234;
    private Statistic statistics;
    private final String optionsMenu = "What would you like to do next?\n\n1. Wash your car" +
            "\n2. Check Wash-Cards balance\n3. Recharge your Wash-Card\n4. Log out";


    public Station(String name) {
        washTypes = new ArrayList<>();
        washTypes.add(new WashType(1, "Economy", 50, 10));
        washTypes.add(new WashType(2, "Standard", 80, 15));
        washTypes.add(new WashType(3, "DeLuxe", 120, 20));
        input = new Scanner(System.in);
        this.name = name;
        now = LocalTime.now();
        statistics = new Statistic();
    }


    public void start(Card card) throws FileNotFoundException {
        currentCard = card;
        System.out.println("\n*** Hello " + card.getName() + "! ***");
        showOptionsMenu();
    }

    public void showOptionsMenu() throws FileNotFoundException {
        System.out.println(optionsMenu);
        System.out.print("\nPlease select an option: ");
        int selection = input.nextInt();
        if (selection > 4 || selection < 1) {
            System.out.println("Please choose a valid number");
            input.reset();
            showOptionsMenu();
        } else {
            switch (selection) {
                case 1:
                    washCar();
                    break;
                case 2:
                    checkBalance();
                    break;
                case 3:
                    rechargeCard();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    EjectCard();
                    break;
            }
        }
    }

    private void EjectCard() {
        System.out.println("Dont forget your WashCard\nHave a nice day :)");
    }

    public void washCar() throws FileNotFoundException {
        //print wash options and prompt
        System.out.println("\nHere's a list of our wash types:\n");
        for (WashType type : washTypes) {
            System.out.println(type.toString());
        }
        System.out.print("Please select an option: ");
        int typeSelection = input.nextInt();

        //calculate cost
        double cost = washTypes.get(typeSelection - 1).price;
        //early bird discount
        if (now.isBefore(LocalTime.parse("14:00"))) {
            cost *= 0.8;
            System.out.println("\nYou arrived before 2pm so you get an early bird discount");
            System.out.println("You get an extra 20%!");
        }
        //fdm discount
        if (currentCard.getHasFDMcard()) {
            System.out.println("\nLooks like you have an FDM card so you get an extra 20%!");
            cost *= 0.8;
        }

        System.out.println("\n"+washTypes.get(typeSelection-1).name+": "+String.format("%.2f", cost)+" kr.");
        //check if balance is enough
        if (currentCard.getBalance() >= cost) {
            System.out.println();
            WashCountDown(washTypes.get(typeSelection - 1).lengthInSeconds);
            //try{Thread.sleep(washTypes.get(typeSelection - 1).lengthInSeconds*1000);}
            //catch (Exception e){}
            System.out.println("\n*** Your car has now been washed! ***\n");
            //charge the card, sets new balance and writes to file the new balance
            currentCard.charge(cost);
            System.out.println("Your Wash Card balance is now " + currentCard.printBalance() + " kr.");
            printReceipt();
            try {
                //creates the file called washcard
                PrintStream output = new PrintStream("WashCard.txt");
                output.println(currentCard.getBalance() + " " + currentCard.getName());
            } catch (IOException e) {
            }
            // edit statistics
            statistics.totalSales += cost;
            statistics.totalTransactions++;
        } else {
            System.out.println(washTypes.get(typeSelection-1).name+": ");
            System.out.print(String.format("%.2f", cost));
            System.out.println("Wash card has insufficient balance");
        }
        showOptionsMenu();

    }

    public void printReceipt() {
        System.out.println("\nDo you wish to print a receipt?\n1. Yes\n2. No\n");
        if (input.nextInt() == 1)
            System.out.println("Your receipt is now printed\n");
    }

    public void checkBalance() throws FileNotFoundException {
        System.out.println("\nYour wash card balance is " + currentCard.printBalance() + " kr.\n");
        showOptionsMenu();
    }

    public void startAdmin(int pin) throws FileNotFoundException {
        if (adminPin == pin) {
            System.out.println("\nYou are now logged in as an admin user");
            showAdminOptionsMenu();
        } else {
            System.out.println("Invalid pin code");
        }
    }


    //recharges washcard, and writes new balance to file
    public void rechargeCard() throws FileNotFoundException {
        System.out.print("Insert credit card and enter pin: ");
        String creditCardPin = input.next();
        if (creditCardPin.length() == 4) {
            System.out.print("Insert wanted amount of money: ");
            double BalanceAmountToInsert = input.nextDouble();
            if (currentCard.getBalance() + BalanceAmountToInsert > 1000) {
                System.out.println("There can maximum be 1000 on your wash card, try again");
                System.out.println("you currently have: " + currentCard.printBalance()+" kr.");
                rechargeCard();

            } else {
                currentCard.rechargeCard(BalanceAmountToInsert);
                System.out.println("\nYou now have a balance of " + currentCard.printBalance()+" kr.");
                printReceipt();
                try {
                    FileWriter fileWriter = new FileWriter("WashCard.txt");
                    fileWriter.write(currentCard.getBalance() + " " + currentCard.getName());
                    fileWriter.close();
                } catch (IOException e) {
                }
                showOptionsMenu();
            }
        } else {
            System.out.println("The pin code is not valid, try again");
            rechargeCard();

        }
    }

    public void showAdminOptionsMenu() throws FileNotFoundException {
        System.out.println("\nWhat would you like to do next?\n1.Get statistics\n2.Edit wash types\n3.Log out\n");
        System.out.print("Select an option: ");
        int selection2 = input.nextInt();

        if (selection2 > 3 || selection2 < 1) {
            System.out.println("Please choose a valid number");
            input.reset();
            showAdminOptionsMenu();
        } else {

            switch (selection2) {
                case 1:
                    getStatistics();
                    break;
                case 2:
                    editWashTypes();
                    break;
                case 3:
                    System.out.println("You are now logged out");
                    break;
            }
        }
    }

    private void editWashTypes() throws FileNotFoundException {
        System.out.println("Press correlating number to change wash type price");
        System.out.println("1: Economy \n2: Standard\n3: DeLux");
        int chosenWashTypeToEdit = input.nextInt();
        if (chosenWashTypeToEdit == 1) {
            System.out.println("What price do you want to change economy to");
            washTypes.get(0).setPrice(input.nextInt());
            System.out.println(washTypes);
        } else if (chosenWashTypeToEdit == 2) {
            System.out.println("What price do you want to change standard to");
            washTypes.get(1).setPrice(input.nextInt());
            System.out.println(washTypes);
        } else if (chosenWashTypeToEdit == 3) {
            System.out.println("What price do you want to change DeLux to");
            washTypes.get(2).setPrice(input.nextInt());
            System.out.println(washTypes);
        } else {
            System.out.println("Try again, you have to press a number between 1-3");
            editWashTypes();
        }
        showAdminOptionsMenu();
    }

    private void getStatistics() throws FileNotFoundException {
        System.out.println("" + statistics);
        PrintStream output = new PrintStream(new File("Statistics.txt"));
        output.println("Hello Admin! these are the statistics from today");
        output.println("" + statistics);
        showAdminOptionsMenu();
    }

    //this will print a countdown and interrupt it if user presses "x"
    public void WashCountDown(int seconds) {
        System.out.println("\nYour car will be washed in " + seconds + " seconds");
        System.out.println("Press 1 to interrupt or 2 once the washing process is finished to continue.");
        //creates a new thread that prints the countdown that lasts the total of seconds passed in
        CountDownTask task = new CountDownTask(seconds);
        Thread thread = new Thread(task);
        thread.start();
        //checks if user needs to interrupt the process
        if (input.nextInt()==1) {
            try {
                thread.interrupt();
                System.out.println("\nCar wash interrupted " + task.secs + " seconds before completion");
                System.out.println("Press any key and enter to resume it");
            } catch (Exception ex) {
            }
            input.next();
            try {
                while (task.secs > 0) {
                    System.out.print("\r"+task.secs);
                    Thread.sleep(1000); //1000 millis = 1 second
                    task.secs--;
                }
                System.out.println("\r "); //this just clears up the line in the console
            } catch (Exception e) {
            }
        }
    }
}

