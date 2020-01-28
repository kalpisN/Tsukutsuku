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
        Date nextTrainTime = trains.get(0).getTimeTableRows().get(0).getScheduledTime();
        Instant i = nextTrainTime.toInstant();
        LocalDateTime nextTrain = i.atZone(ZoneId.systemDefault()).toLocalDateTime();

        return nextTrain.toLocalTime().toString();
    }

}


