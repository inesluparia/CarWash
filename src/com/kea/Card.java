package com.kea;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;


public class Card {
    private double balance;
    private boolean hasFDMcard;
    private String name;

    public Card() {
    }

    public Card(double balance, String name, boolean hasFDMcard) {
        this.balance = balance;
        this.hasFDMcard = hasFDMcard;
        this.name = name;
        createCardFile();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean getHasFDMcard() {
        return hasFDMcard;
    }

    public double getBalance() {
        return balance;
    }

    public String printBalance() {
        return String.format("%.2f", balance);
    }

    public String getName() {
        return name;
    }

    public void charge(double cost) {
        balance -= cost;
    }

    public void rechargeCard(double amount) {
        balance += amount;
    }

    //method for creating washcard with balance and name, in a file
    public void createCardFile() {
        //uses try to catch the exception
        try {
            //creates the file called washcard
            PrintStream output = new PrintStream(new File("WashCard.txt"));
            output.println(String.format("%.2f", balance) + " " + name);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException e) {
            System.out.println("an error has occurred.");
            e.printStackTrace();
        }
    }

    public void createCardFromFile(File myObj) throws FileNotFoundException {
        Scanner myReader = new Scanner(myObj);
        if (myReader.hasNextDouble()) {
            Double washCardBalance = myReader.nextDouble();
            setBalance(washCardBalance);
        }
        if (myReader.hasNext()) {
            String washCardName = myReader.next();
            setName(washCardName);
        }
    }
}