package com.kosyachook.serverside;

public class Recipient {
    private String name;
    private int id;

    public Recipient(String name, int id) {
        this.name = name;
        this.id = id;
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
}
