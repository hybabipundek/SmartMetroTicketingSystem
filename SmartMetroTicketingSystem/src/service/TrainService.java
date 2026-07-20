package service;

import model.Train;
import java.util.ArrayList;

public class TrainService {
    private ArrayList<Train> trains;

    public TrainService() {
        trains = new ArrayList<>();
    }

    public void addTrain(Train train) {
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

    public ArrayList<Train> getAllTrains() {
        return trains;
    }

    public void setTrains(ArrayList<Train> trains) {
        this.trains = trains;
    }
}