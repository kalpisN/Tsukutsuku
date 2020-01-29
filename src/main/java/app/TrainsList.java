package app;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainsList {
    private List<Train> trains = new ArrayList<>();

    public List<Train> getTrains() {
        return trains;
    }

    public void setTrains(List<Train> trains) {

        this.trains = trains;
    }
    public void addTrain(Train train){
        trains.add(train);
    }

    public String nextTrain(){

        return trains.get(0).getTimeTableRows().get(0).getScheduledTime().toLocalTime().toString();
    }

    public void printTrains(TrainsList tl, String departureStation, String arrivalStation) {
        for (Train train : getTrains()) {
            System.out.print(train + ", ");
            System.out.print("Leaves from " + departureStation + " at " + train.getTimeTableRows().get(0).getScheduledTime().toLocalTime() + ", ");
            int index = (train.getTimeTableRows().size()) - 1;
            System.out.print("arrives to " + arrivalStation + " at " + train.getTimeTableRows().get(index).getScheduledTime().toLocalTime());
            System.out.println("");
        }

    }
    public void printTimeTableRows(TrainsList tl, int trainNumber) {
        for (Train train : tl.getTrains()) {
            if (train.getTrainNumber() == trainNumber) {
                System.out.println(train.getTimeTableRows());
            }
        }
    }
}
