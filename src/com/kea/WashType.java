package com.kea;

public class WashType {
    int id;
    int price;
    String name;
    int lengthInSeconds;

    public WashType(int id, String name, int price,int lengthInSeconds) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.lengthInSeconds = lengthInSeconds;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return id+". "+name+" , Price "+price+" kr.";

    }
}
