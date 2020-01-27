package app;

import java.util.ArrayList;
import java.util.List;

public class TrainsList extends ArrayList {
    private List<Train> trains;

    public TrainsList(){
        trains = new ArrayList<>();
    }

    public void addTrain(Train train){
        trains.add(train);
    }
}
