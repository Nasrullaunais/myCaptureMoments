package com.eventBooking.services;


import com.eventBooking.models.Photographer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PhotographerService {
    private static final String PHOTOGRAPHER_FILE = "data/photographers.txt";

    public List<Photographer> getAllPhotographers() {
        List<Photographer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PHOTOGRAPHER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Photographer.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading photographers.txt: " + e.getMessage());
        }
        return list;
    }

    public boolean addPhotographer(Photographer photographer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PHOTOGRAPHER_FILE, true))) {
            writer.write(photographer.toFileString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to photographers.txt: " + e.getMessage());
            return false;
        }
    }

    public boolean removePhotographer(String name) {
        List<Photographer> updatedList = new ArrayList<>();
        boolean removed = false;
        for (Photographer p : getAllPhotographers()) {
            if (!p.getName().equalsIgnoreCase(name)) {
                updatedList.add(p);
            } else {
                removed = true;
            }
        }
        if (removed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PHOTOGRAPHER_FILE))) {
                for (Photographer p : updatedList) {
                    writer.write(p.toFileString());
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                System.out.println("Error updating photographers.txt: " + e.getMessage());
            }
        }
        return false;
    }

    public List<Photographer> getSortedByRating() {
        List<Photographer> list = getAllPhotographers();
        // Bubble Sort by rating descending
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).getRating() < list.get(j + 1).getRating()) {
                    Photographer temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }
}
