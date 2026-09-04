package service;

import model.Station;
import model.Route;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Service class responsible for storing, managing, searching, viewing, and sorting metro routes.
 */
public class RouteService {

    private ArrayList<Route> routes;

// Initializes the RouteService object.
    public RouteService() {
        routes = new ArrayList<>();
    }

// Adds a route to the route collection after route validation.
    public void addRoute(Route route) {
        if (route == null || route.getSource() == null || route.getDestination() == null) {
            System.out.println("Invalid route.");
            return;
        }

        if (route.getSource().getStationId().equalsIgnoreCase(route.getDestination().getStationId())) {
            System.out.println("Source and destination cannot be the same station.");
            return;
        }

        if (!Double.isFinite(route.getDistanceKm()) || route.getDistanceKm() <= 0) {
            System.out.println("Route distance must be greater than 0 km.");
            return;
        }

        for (Route r : routes) {
            if (r.getRouteId().equalsIgnoreCase(route.getRouteId())) {
                System.out.println("Route ID already exists: " + route.getRouteId());
                return;
            }
        }

        routes.add(route);
        System.out.println("Route added: " + route.getSource().getName() + " -> " + route.getDestination().getName());
    }

// Finds a route matching the specified source and destination stations.
    public Route findRoute(Station source, Station destination) {
        if (source == null || destination == null) return null;
        for (Route r : routes) {
            if (r.getSource().getStationId().equalsIgnoreCase(source.getStationId())
                    && r.getDestination().getStationId().equalsIgnoreCase(destination.getStationId())) {
                return r;
            }
        }
        return null;
    }

// Displays all stored routes.
    public void viewRoutes() {
        if (routes.isEmpty()) {
            System.out.println("No routes available.");
        } else {
            System.out.println("--- Route List ---");
            for (Route r : routes) {
                r.displayRoute();
                System.out.println("-------------------");
            }
        }
    }

// Sorts routes according to their distance.
    public void sortRoutesByDistance() {
        routes.sort(Comparator.comparingDouble(Route::getDistanceKm));
        System.out.println("Routes sorted by distance.");
    }

// Returns the route collection used by the system.
    public ArrayList<Route> getAllRoutes() {
        return routes;
    }

// Updates the route collection with loaded data.
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = (routes == null) ? new ArrayList<>() : routes;
    }
}
