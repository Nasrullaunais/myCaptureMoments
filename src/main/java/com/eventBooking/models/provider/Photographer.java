package com.eventBooking.models.provider;

public class Photographer extends Provider{
    private String photoResolution;


    public Photographer(String name, String specialty, int rating, String photoResolution) {
        super(name, specialty, rating);
        this.photoResolution = photoResolution;
    }

    public String getPhotoResolution() { return photoResolution; }

    public void setPhotoResolution(String photoResolution) { this.photoResolution = photoResolution; }

    public String toFileString() {
        return super.toFileString() + "," + photoResolution;
    }

    public static Photographer fromFileString(String line) {
        String[] parts = line.split(",");
        String photoResolution = parts.length > 3 ? parts[3] : "HD"; // Default to HD if not specified
        return new Photographer(parts[0], parts[1], Integer.parseInt(parts[2]), photoResolution);
    }

}
