package service;

import model.Train;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Service class responsible for storing, managing, viewing, and sorting metro trains.
 */
public class TrainService {

    private ArrayList<Train> trains;

// Initializes the TrainService object.
    public TrainService() {
        trains = new ArrayList<>();
    }

// Adds a train to the train collection.
    public void addTrain(Train train) {
        if (train == null) {
            System.out.println("Invalid train.");
            return;
        }
        for (Train t : trains) {
            if (t.getTrainId().equalsIgnoreCase(train.getTrainId())) {
                System.out.println("Train ID already exists: " + train.getTrainId());
                return;
            }
        }
        trains.add(train);
        System.out.println("Train added: " + train.getTrainName());
    }

// Displays all stored trains.
    public void viewTrains() {
        if (trains.isEmpty()) {
            System.out.println("No trains available.");
        } else {
            System.out.println("--- Train List ---");
            for (Train t : trains) {
                t.displayTrain();
                System.out.println("-------------------");
            }
        }
    }

// Sorts trains alphabetically by train name.
    public void sortTrainsByName() {
        trains.sort(Comparator.comparing(Train::getTrainName, String.CASE_INSENSITIVE_ORDER));
        System.out.println("Trains sorted by name.");
    }

// Returns the train collection used by the system.
    public ArrayList<Train> getAllTrains() {
        return trains;
    }

// Updates the train collection with loaded data.
    public void setTrains(ArrayList<Train> trains) {
        this.trains = (trains == null) ? new ArrayList<>() : trains;
    }
}
