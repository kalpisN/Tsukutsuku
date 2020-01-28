package app;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)


public class Station implements Comparable<Station>{
    String stationName;
    String stationShortCode;
    Boolean passengerTraffic;

    public String getStationName() {
        return stationName;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public Boolean getPassengerTraffic() {
        return passengerTraffic;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationName='" + stationName + '\'' +
                ", stationShortCode='" + stationShortCode + '\'' +
                '}';
    }

    @Override
    public int compareTo(Station station) {
        return this.stationName.compareTo(station.getStationName());
    }
}
