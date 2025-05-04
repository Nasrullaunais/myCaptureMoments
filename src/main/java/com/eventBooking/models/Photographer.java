package com.eventBooking.models;


public class Photographer {
    private String name;
    private String specialty;
    private int rating; // Set by admin

    public Photographer(String name, String specialty, int rating) {
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
    }

    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public int getRating() { return rating; }

    public String toFileString() {
        return name + "," + specialty + "," + rating;
    }

    public static Photographer fromFileString(String line) {
        String[] parts = line.split(",");
        return new Photographer(parts[0], parts[1], Integer.parseInt(parts[2]));
    }


}
