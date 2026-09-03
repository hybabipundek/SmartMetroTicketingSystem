package service;

import model.Station;
import java.util.ArrayList;
import java.util.Comparator;

public class StationService {

    private ArrayList<Station> stations;

    public StationService() {
        stations = new ArrayList<>();
    }

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

    public Station searchStation(String name) {
        if (name == null) return null;
        for (Station s : stations) {
            if (s.getName().trim().equalsIgnoreCase(name.trim())) return s;
        }
        return null;
    }

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

    public void sortStationsByName() {
        stations.sort(Comparator.comparing(Station::getName, String.CASE_INSENSITIVE_ORDER));
        System.out.println("Stations sorted by name.");
    }

    public ArrayList<Station> getAllStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = (stations == null) ? new ArrayList<>() : stations;
    }
}
