package model;

/**
 * Model class representing a route between two metro stations and its distance.
 */
public class Route {
    private String routeId;
    private Station source;
    private Station destination;
    private double distanceKm;

// Initializes the Route object.
    public Route(String routeId, Station source, Station destination, double distanceKm) {
        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }

// Returns the routeid value.
    public String getRouteId() {
        return routeId;
    }

// Updates the routeid value.
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

// Returns the source value.
    public Station getSource() {
        return source;
    }

// Updates the source value.
    public void setSource(Station source) {
        this.source = source;
    }

// Returns the destination value.
    public Station getDestination() {
        return destination;
    }

// Updates the destination value.
    public void setDestination(Station destination) {
        this.destination = destination;
    }

// Returns the distancekm value.
    public double getDistanceKm() {
        return distanceKm;
    }

// Updates the distancekm value.
    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

// Returns the stored distance of the route.
    public double calculateDistance() {
        return distanceKm;
    }

// Displays the route information in the console.
    public void displayRoute() {
        System.out.println("========== Route ==========");
        System.out.println("Route ID    : " + routeId);
        System.out.println("Source      : " + source.getName());
        System.out.println("Destination : " + destination.getName());
        System.out.println("Distance    : " + distanceKm + " km");
        System.out.println("===========================");
    }

    @Override
    public String toString() {
        return source.getName() + " -> " + destination.getName()
                + " (" + distanceKm + " km)";
    }
}
