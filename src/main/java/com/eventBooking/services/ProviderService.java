package com.eventBooking.services;


import com.eventBooking.models.provider.Photographer;
import com.eventBooking.models.provider.Provider;
import com.eventBooking.models.provider.Videographer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {
    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);
    private static final String PHOTOGRAPHER_FILE = "data/photographers.txt";
    private static final String VIDEOGRAPHER_FILE = "data/videographers.txt";

    public List<Photographer> getAllPhotographers() {
        List<Photographer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PHOTOGRAPHER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Photographer.fromFileString(line));
            }
        } catch (IOException e) {
            logger.error("Error reading photographers.txt: {}", e.getMessage());
        }
        return list;
    }

    public List<Videographer> getAllVideographers() {
        List<Videographer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(VIDEOGRAPHER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Videographer.fromFileString(line));
            }
        } catch (IOException e) {
            logger.error("Error reading videographers.txt: {}", e.getMessage());
        }
        return list;
    }

    public List<Provider> getAllProviders() {
        List<Provider> list = new ArrayList<>();
        list.addAll(getAllPhotographers());
        list.addAll(getAllVideographers());
        return list;
    }

    public void addPhotographer(Provider photographer) {
        addProvider(photographer, PHOTOGRAPHER_FILE);
    }
    public void addVideographer(Provider videographer) {
        addProvider(videographer, VIDEOGRAPHER_FILE);
    }

    private void addProvider(Provider provider, String providerFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(providerFile, true))) {
            writer.write(provider.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to photographers.txt: " + e.getMessage());
        }
    }

    public boolean removeProvider(String name) {
        // First try to remove as photographer
        if (removePhotographer(name)) {
            return true;
        }
        // If not found as photographer, try as videographer
        return removeVideographer(name);
    }

    private boolean removePhotographer(String name) {
        return removeProviderFromFile(name, getAllPhotographers(), PHOTOGRAPHER_FILE);
    }

    private boolean removeVideographer(String name) {
        return removeProviderFromFile(name, getAllVideographers(), VIDEOGRAPHER_FILE);
    }

    private <T extends Provider> boolean removeProviderFromFile(String name, List<T> providers, String filename) {
        List<T> updatedList = new ArrayList<>();
        boolean removed = false;

        // Filter out the provider to be removed
        for (T provider : providers) {
            if (!provider.getName().equalsIgnoreCase(name)) {
                updatedList.add(provider);
            } else {
                removed = true;
            }
        }

        // If found and removed, write back to file
        if (removed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Provider p : updatedList) {
                    writer.write(p.toFileString());
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                logger.error("Error updating {}: {}", filename, e.getMessage());
            }
        }
        return false;
    }

    public boolean updateProvider(String name, Provider updatedProvider) {
        // Check if it's a photographer
        boolean isPhotographer = false;
        for (Photographer p : getAllPhotographers()) {
            if (p.getName().equalsIgnoreCase(name)) {
                isPhotographer = true;
                break;
            }
        }

        if (isPhotographer) {
            return updateProviderInFile(name, updatedProvider, getAllPhotographers(), PHOTOGRAPHER_FILE);
        } else {
            return updateProviderInFile(name, updatedProvider, getAllVideographers(), VIDEOGRAPHER_FILE);
        }
    }

    private <T extends Provider> boolean updateProviderInFile(String name, Provider updatedProvider, List<T> providers, String filename) {
        List<Provider> updatedList = new ArrayList<>();
        boolean updated = false;

        // Replace the provider with the updated one
        for (Provider provider : providers) {
            if (provider.getName().equalsIgnoreCase(name)) {
                updatedList.add(updatedProvider);
                updated = true;
            } else {
                updatedList.add(provider);
            }
        }

        // If found and updated, write back to file
        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Provider p : updatedList) {
                    writer.write(p.toFileString());
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                logger.error("Error updating {}: {}", filename, e.getMessage());
            }
        }
        return false;
    }


    public List<Photographer> getPhotographersSortedByRating() {
        List<Photographer> list = new ArrayList<>(getAllPhotographers()); // Create a copy to avoid modifying original

        // Bubble Sort (Descending Order by Rating)
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).getRating() < list.get(j + 1).getRating()) {
                    // Swap photographers
                    Photographer temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }

    public List<Videographer> getVideographersSortedByRating() {
        List<Videographer> list = new ArrayList<>(getAllVideographers()); // Create a copy to avoid modifying original

        // Bubble Sort (Descending Order by Rating)
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).getRating() < list.get(j + 1).getRating()) {
                    // Swap videographers
                    Videographer temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
        return list;
    }
}
