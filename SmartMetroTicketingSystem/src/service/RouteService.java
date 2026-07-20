package service;

import model.Route;
import java.util.ArrayList;

public class RouteService {
    private ArrayList<Route> routes;

    public RouteService() {
        routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        routes.add(route);
        System.out.println("Route added: " + route.getSource() + " -> " + route.getDestination());
    }

    public Route findRoute(String source, String destination) {
        for (Route r : routes) {
            if (r.getSource().equalsIgnoreCase(source) && r.getDestination().equalsIgnoreCase(destination)) {
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

    public ArrayList<Route> getAllRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
}