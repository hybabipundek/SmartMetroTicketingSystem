package service;

import model.Station;
import model.Route;
import java.util.ArrayList;
import java.util.Comparator;

public class RouteService {

    private ArrayList<Route> routes;

    public RouteService() {
        routes = new ArrayList<>();
    }

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

    public void sortRoutesByDistance() {
        routes.sort(Comparator.comparingDouble(Route::getDistanceKm));
        System.out.println("Routes sorted by distance.");
    }

    public ArrayList<Route> getAllRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = (routes == null) ? new ArrayList<>() : routes;
    }
}
