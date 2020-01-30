package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class WagonListHelper {

    public static List<Wagon> readTrainJSONDataAndListTrainCompositions(int trainNumber) {
        Train trains = new Train();
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        try {
            URL url = new URL(URI.create(String.format("%s/compositions/" + LocalDate.now() + "/" + trainNumber, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
           //CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Train.class);

            trains = mapper.readValue(url, Train.class);

            System.out.println(trains);
            System.out.println(trains.getWagons());

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }



        return trains.getWagons();

    }
}
