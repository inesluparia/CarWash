package com.kea;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Station Østerbro = new Station("Østerbro");

        Card someonesCard = new Card(500,"Ines",true);

        Østerbro.start(someonesCard);

        Østerbro.startAdmin(1234);
    }
}


