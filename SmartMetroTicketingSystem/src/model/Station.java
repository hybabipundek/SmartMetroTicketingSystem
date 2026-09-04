package model;

/**
 * Model class representing a metro station with its ID, name, and location.
 */
public class Station {

	private String stationId;
	private String name;
	private String location;

// Initializes the Station object.
	public Station(String stationId, String name, String location) {
    	this.stationId = stationId;
        this.name = name;
        this.location = location;
    }

// Returns the stationid value.
	public String getStationId() {
		return stationId;
    }

// Updates the stationid value.
	public void setStationId(String stationId) {
		this.stationId = stationId;
    }

// Returns the name value.
	public String getName() {
    	return name;
    }

// Updates the name value.
	public void setName(String name) {
    	this.name = name;
    }

// Returns the location value.
	public String getLocation() {
		return location;
    }

// Updates the location value.
	public void setLocation(String location) {
    	this.location = location;
    }

// Displays the station information in the console.
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