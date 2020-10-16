package com.kea;

public class Statistic {
    int totalTransactions = 0;
    int totalSales = 0;

    @Override
    public String toString(){
        return "\nPrinting statistics...\nTotal Wash Types: "+totalTransactions+"\nTotal sales: "+totalSales+"kr.";
    }
}
