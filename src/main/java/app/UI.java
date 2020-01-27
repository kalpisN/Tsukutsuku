package app;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UI {


    public void run() {
        System.out.println("Kerro lähtöasema:");
        String departureStation = "HKI";

        System.out.println("Kerro määräsema: ");
        String arrivalStation = "ROI";

        readTrainJSONData(departureStation, arrivalStation);
    }

        private static void readTrainJSONData(String departureStation, String arrivalStation) {
        String between = departureStation + "/" + arrivalStation;
            System.out.println(between);
        String baseurl = "https://rata.digitraffic.fi/api/v1";
            try {
                URL url = new URL(URI.create(String.format("%s/live-trains/station/" + between, baseurl)).toASCIIString());
                ObjectMapper mapper = new ObjectMapper();
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(TrainsList.class, Train.class);
                List<Train> trains = mapper.readValue(url, listType);  // pelkkä List.class ei riitä tyypiksi


                System.out.println(trains.get(0).getTrainNumber());

                System.out.println(trains.get(0).getTimeTableRows().get(0).getScheduledTime());
                System.out.println(trains.get(0).getTimeTableRows().get(0).getStationShortCode());
                System.out.println(trains.get(0).getTimeTableRows().get(0).getType());
                

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
}

