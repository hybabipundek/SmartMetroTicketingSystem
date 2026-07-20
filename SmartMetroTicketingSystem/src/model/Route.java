package model;

public class Route {
    private String routeId;
    private String source;       
    private String destination;  
    private double distanceKm;   

    public Route(String routeId, String source, String destination, double distanceKm) {
        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double calculateDistance() {
        return distanceKm;
    }
  
    public void displayRoute() {
        System.out.println("Route ID: " + routeId);
        System.out.println("From: " + source + " -> To: " + destination);
        System.out.println("Distance: " + distanceKm + " km");
    }

    @Override
    public String toString() {
        return source + " -> " + destination + " (" + distanceKm + " km)";
    }
}