package app;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class UI {

    public void run() {


        Scanner reader = new Scanner(System.in);
        TrainsList tl = new TrainsList();
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        List<Station> stations = StationsListHelper.readJSONDataAndListPassangerTrafficStationsToList(baseurl);



        System.out.println("Welcome!");

        for (;;) {

            System.out.println("1. Search next train");
            System.out.println("2. Search all trains between chosen stations");
            System.out.println("3. Search all stops and stopping times for specific train");
            System.out.println("0. Exit");
            String userInput = reader.nextLine().trim();



            if ("0".equals(userInput)) {
                break;
            }

            else if ("1".equals(userInput)) {
                SearchNextTrain(stations, reader);
            }

            else if ("2".equals(userInput)) {
                allTrainsBetweenStations(reader, stations);
            }

            else if ("3".equals((userInput))) {
                searchAllStops(reader, stations);
            }
        }

    }

    private void searchAllStops(Scanner reader, List<Station> stations) {
        String departureStation = askDepartureStation(reader);
        String arrivalStation = askArrivalStation(reader);
        TrainsList tl = createTrainsList(departureStation, arrivalStation, stations);

            int i = 0;
        while (i < tl.getTrains().size()) {
            System.out.println(tl.getTrains().get(i));
            System.out.print("Leaves from " + departureStation + " at " + tl.getTrains().get(i).getTimeTableRows().get(0).getScheduledTime().toLocalTime() + ", ");
            int index = (tl.getTrains().get(i).getTimeTableRows().size()) - 1;
            System.out.print("arrives to " + arrivalStation + " at " + tl.getTrains().get(i).getTimeTableRows().get(index).getScheduledTime().toLocalTime());
            System.out.println("");
            i++;
        }

        int trainNumber = askTrainNumber(reader);
        List<TimeTableRow> rows = new ArrayList<>();
        for (Train train : tl.getTrains()) {
            if (train.getTrainNumber() == trainNumber) {
                //System.out.println(train.getTimeTableRows()); // kerää tietyn junan aikataulurivit listaan ja lähettää listan metodille stoppingtime
                rows = train.getTimeTableRows();
            }
        }
        System.out.println(stoppingTime(rows)); // palauttaa minuuttipysähdyksen 

    }

    private void printTrainsList(TrainsList tl) {
    }


    private int askTrainNumber(Scanner reader) {
        System.out.println("Type train number: ");
        int trainNumber = Integer.valueOf(reader.nextLine());

        return trainNumber;
    }

    private TrainsList createTrainsList(String departureStation, String arrivalStation, List<Station> stations) {
        TrainsList tl = new TrainsList();
        String departureStationShortCode= StationsListHelper.convertStationNameToShortCode(departureStation,stations);
        String arrivalStationShortCode = StationsListHelper.convertStationNameToShortCode(arrivalStation,stations);
        tl.setTrains(readTrainJSONData(departureStationShortCode, arrivalStationShortCode));

        return tl;
    }

    private void SearchNextTrain(List<Station> stations, Scanner reader) {
        String departureStation = askDepartureStation(reader);
        String arrivalStation = askArrivalStation(reader);
        TrainsList tl = createTrainsList(departureStation, arrivalStation, stations);
        System.out.println("Next train directly from " + departureStation+ " to " + arrivalStation + " leaves today at: " + tl.nextTrain());
        System.out.println("");
    }

    static List<Train> readTrainJSONData(String departureStation, String arrivalStation) {
        String between = departureStation + "/" + arrivalStation;
        List<Train> trains = null;
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        try {
            URL url = new URL(URI.create(String.format("%s/live-trains/station/" + between, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Train.class);
            //List<Train>
            trains = mapper.readValue(url, listType);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return trains;
    }

    private static String askDepartureStation(Scanner reader) {
        System.out.print("Type departure station: ");
        String departureStation = reader.nextLine();
        return departureStation;
    }

    private static String askArrivalStation(Scanner reader) {
        System.out.print("Type destination: ");
        String arrivalStation = reader.nextLine();
        return arrivalStation;
    }

    private void allTrainsBetweenStations(Scanner reader, List<Station> stations) {
        String departureStation = askDepartureStation(reader);
        String arrivalStation = askArrivalStation(reader);
        TrainsList tl = createTrainsList(departureStation, arrivalStation, stations);

        for (Train train : tl.getTrains()) {
            System.out.println(train);
            System.out.print("Leaves from " + departureStation + " at " + train.getTimeTableRows().get(0).getScheduledTime().toLocalTime() + ", ");
            int index = (train.getTimeTableRows().size()) - 1;
            System.out.print("arrives to " + arrivalStation + " at " + train.getTimeTableRows().get(index).getScheduledTime().toLocalTime());
            System.out.println("");
        }
    }

    private long stoppingTime(List<TimeTableRow> rows){
        int i = 0;

        LocalDateTime arrival = rows.get(1).getScheduledTime();
        LocalDateTime departure = rows.get(2).getScheduledTime();

        Duration stoppingtime = Duration.between(arrival,departure);

        return stoppingtime.toMinutes();
    }
}