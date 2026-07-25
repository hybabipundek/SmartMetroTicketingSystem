package model;

public class Route {
    private String routeId;
    private Station source;       
    private Station destination;  
    private double distanceKm;   

    public Route(String routeId, Station source, Station destination, double distanceKm) {
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

    public Station getSource() {
        return source;
    }

    public void setSource(Station source) {
        this.source = source;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
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
	System.out.println("========== Route ==========");
        System.out.println("Route ID    : " + routeId);
        System.out.println("Source      : " + source.getName());
        System.out.println("Destination : " + destination.getName());
	
	System.out.println("===========================");
    }

    @Override
    public String toString() {
        return source.getName() + " -> " + destination.getName() + " (" + distanceKm + " km)";
    }
}