package service;

import model.Station;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Service class responsible for storing, managing, searching, viewing, and sorting metro stations.
 */
public class StationService {

    private ArrayList<Station> stations;

// Initializes the StationService object.
    public StationService() {
        stations = new ArrayList<>();
    }

// Adds a station to the station collection.
    public void addStation(Station station) {
        if (station == null) {
            System.out.println("Invalid station.");
            return;
        }

        for (Station s : stations) {
            if (s.getStationId().equalsIgnoreCase(station.getStationId())) {
                System.out.println("Station ID already exists: " + station.getStationId());
                return;
            }
            if (s.getName().trim().equalsIgnoreCase(station.getName().trim())) {
                System.out.println("Station name already exists: " + station.getName());
                return;
            }
        }

        stations.add(station);
        System.out.println("Station added: " + station.getName());
    }

// Searches for a station by name and displays the matching station.
    public Station searchStation(String name) {
        if (name == null) return null;
        for (Station s : stations) {
            if (s.getName().trim().equalsIgnoreCase(name.trim())) return s;
        }
        return null;
    }

// Displays all stored stations.
    public void viewStations() {
        if (stations.isEmpty()) {
            System.out.println("No stations available.");
        } else {
            System.out.println("--- Station List ---");
            for (Station s : stations) {
                s.displayInfo();
                System.out.println("-------------------");
            }
        }
    }

// Sorts stations alphabetically by name.
    public void sortStationsByName() {
        stations.sort(Comparator.comparing(Station::getName, String.CASE_INSENSITIVE_ORDER));
  
        System.out.println("\nStations sorted by name.");
    }

// Returns the station collection used by the system.
    public ArrayList<Station> getAllStations() {
        return stations;
    }

// Updates the station collection with loaded data.
    public void setStations(ArrayList<Station> stations) {
        this.stations = (stations == null) ? new ArrayList<>() : stations;
    }
}
