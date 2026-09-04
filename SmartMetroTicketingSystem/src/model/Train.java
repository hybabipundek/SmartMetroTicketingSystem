package model;

/**
 * Model class representing a metro train with its ID, name, and capacity.
 */
public class Train {
    private String trainId;
    private String trainName;
    private int capacity;   

// Initializes the Train object.
    public Train(String trainId, String trainName, int capacity) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.capacity = capacity;
    }

// Returns the trainid value.
    public String getTrainId() {
        return trainId;
    }

// Updates the trainid value.
    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

// Returns the trainname value.
    public String getTrainName() {
        return trainName;
    }

// Updates the trainname value.
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

// Returns the capacity value.
    public int getCapacity() {
        return capacity;
    }

// Updates the capacity value.
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    
// Displays Train information in the console.
    public void displayTrain() {
        System.out.println("Train ID: " + trainId);
        System.out.println("Train Name: " + trainName);
        System.out.println("Capacity: " + capacity);
    }

    @Override
    public String toString() {
        return trainName + " (" + trainId + "), Capacity: " + capacity;
    }
}