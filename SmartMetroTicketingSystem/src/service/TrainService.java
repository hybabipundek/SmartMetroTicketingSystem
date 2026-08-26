package service;

import model.Train;
import java.util.ArrayList;
import java.util.Comparator;

public class TrainService {

    private ArrayList<Train> trains;

    public TrainService() {
        trains = new ArrayList<>();
    }

    public void addTrain(Train train) {
        for (Train t : trains) {
            if (t.getTrainId().equalsIgnoreCase(train.getTrainId())) {
                System.out.println("Train ID already exists: " + train.getTrainId());
                return;
            }
        }

        trains.add(train);
        System.out.println("Train added: " + train.getTrainName());
    }

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

    public void sortTrainsByName() {
        trains.sort(Comparator.comparing(Train::getTrainName, String.CASE_INSENSITIVE_ORDER));
    }

    public ArrayList<Train> getAllTrains() {
        return trains;
    }

    public void setTrains(ArrayList<Train> trains) {
        this.trains = (trains == null) ? new ArrayList<>() : trains;
    }
}
