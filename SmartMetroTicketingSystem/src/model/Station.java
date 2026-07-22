package model;

public class Station {

	private String stationId;
	private String name;
	private String location;

	public Station(String stationId, String name, String location) {
    	this.stationId = stationId;
        this.name = name;
        this.location = location;
    }

	public String getStationId() {
		return stationId;
    }

	public void setStationId(String stationId) {
		this.stationId = stationId;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getLocation() {
		return location;
    }

	public void setLocation(String location) {
    	this.location = location;
    }

	public void displayInfo() {
    	System.out.println("Station ID: " + stationId);
    	System.out.println("Name: " + name);
    	System.out.println("Location: " + location);
	}


    @Override
	public String toString() {
    	return name + " (" + stationId + ")";
    }
}