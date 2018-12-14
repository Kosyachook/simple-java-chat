package com.kosyachook.serverside;

public class Recipient {
    private String name;
    private int id;

    private boolean isReceiving;

    public Recipient(String name, int id) {
        this.name = name;
        this.id = id;
        this.isReceiving = false;
    }

    public static Recipient createRecipient(String name, int id){
        return new Recipient(name, id);
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isReceiving() {
        return isReceiving;
    }

    public void setReceiving(boolean receiving) {
        isReceiving = receiving;
    }
}
