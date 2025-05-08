package com.eventBooking.models.pricing;

public class Package {
    private String name;
    private int price;
    private int duration;


    public Package(String name, int price, int duration) {
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getDuration() { return duration; }

    public String toFileString() {
        return name + "," + price + "," + duration;
    }

    public static Package fromFileString(String line) {
        String[] parts = line.split(",");
        return new Package(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}
