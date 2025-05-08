package com.eventBooking.models.provider;

public class Videographer extends Provider {
    private String style;
    private String videoResolution;

    public Videographer(String name, String specialty, int rating, String style, String videoResolution) {
        super(name, specialty, rating);
        this.style = style;
        this.videoResolution = videoResolution;
    }

    public String getStyle() { return style; }
    public String getVideoResolution() { return videoResolution; }

    public String toFileString() {
        return super.toFileString() + "," + style + "," + videoResolution;
    }

    public static Videographer fromFileString(String line) {
        String[] parts = line.split(",");
        String style = parts.length > 3 ? parts[3] : "Standard"; // Default to Standard if not specified
        String videoResolution = parts.length > 4 ? parts[4] : "4K"; // Default to 4K if not specified
        return new Videographer(parts[0], parts[1], Integer.parseInt(parts[2]), style, videoResolution);
    }

}
