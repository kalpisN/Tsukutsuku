package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class StationsListHelper {

    public static List<Station> readJSONDataAndListPassangerTrafficStationsToList(String baseurl){
        List<Station> stations=null;
        List<Station> passengerTrafficStations = new ArrayList();
        try {
            URL url = new URL (URI.create(String.format("%s/metadata/stations", baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Station.class);
            stations = mapper.readValue(url, listType);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        stations.stream()
                .filter(station -> station.getPassengerTraffic())
                .forEach(station -> passengerTrafficStations.add(station));
        return passengerTrafficStations;
    }

    public static String convertStationNameToShortCode(String stationName, List<Station> stations){//tätä voi hienosäätää niin että parametri-Stringin ei tarvitse olla loppuun asti kirjoitettu
        String shortCode=null;                                                                      //voi myös muuttaa niin että käsittelee jos annettu nimi on virheellinen, eli tulee virheilmoitus
        for(Station station: stations) {
            if (stationName.trim().equalsIgnoreCase(station.getStationName())) {
                shortCode = station.getStationShortCode();
            }
        }
        return shortCode;
    }
    public static String convertStationShortCodeToStationName(String shortCode, List<Station> stations){
        String stationName = null;
        for(Station station: stations){
            if(shortCode.trim().equalsIgnoreCase(station.getStationShortCode())){
                stationName = station.getStationName();
            }
        }
        return stationName;
    }
}
