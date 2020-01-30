package app;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;


import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class UI {

    public void run() {

        Scanner reader = new Scanner(System.in);
        TrainsList tl = new TrainsList();
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        List<Station> stations = StationsListHelper.readJSONDataAndListPassangerTrafficStationsToList(baseurl);
        System.out.println("Welcome to a 24-hour-journey-planner for spontaneous people who know where they want to travel!");

        menu(reader, stations);
    }



    private void menu(Scanner reader, List<Station> stations) {
        for ( ; ; ) {
            System.out.println("");
            System.out.println("1. Search next train between chosen stations\n"
                            + "2. Search all trains between chosen stations\n"
                            + "3. Search all stops and stopping time for specific train\n"
                            + "4. List all passenger traffic stations\n"
                            + "0. Exit\n");

            if(menuUserInput(reader, stations).equals(false)) {
                break;
            }
        }
    }

    private Boolean menuUserInput(Scanner reader, List<Station> stations) {

        String userInput = reader.nextLine().trim();
            if ("0".equals(userInput) || "0.".equals(userInput)) {
                return false;
            } else if ("1".equals(userInput) || "1.".equals(userInput)) {
                SearchNextTrain(stations, reader);
            } else if ("2".equals(userInput) || "2.".equals(userInput)) {
                allTrainsBetweenStations(reader, stations);
            } else if ("3".equals(userInput) || "3.".equals(userInput)) {
                searchAllStopsBetweenStations(reader, stations);

            }
            else if("4".equals(userInput) || "4.".equals(userInput)) {
                printAllStation(stations);
            }
            else {
                System.out.println(String.format("Unknown command: '%s'", userInput));
                System.out.println();
            }
            return true;
    }

    private void printAllStation(List<Station> stations) {
        for (Station station: stations) {
            System.out.println(station.getStationName());
        }

            System.out.println();
    }

    private int askTrainNumber(Scanner reader) {
        System.out.println("Type train number: ");
        int trainNumber = Integer.valueOf(reader.nextLine());

        return trainNumber;
    }

    private static String askDepartureStation(Scanner reader) {
        System.out.println("Type 'X' to return to main menu\n\n"
                            + "Type departure station: ");
        String departureStation = reader.nextLine();

        return departureStation;
    }

    private static String askArrivalStation(Scanner reader) {
        System.out.println("Type destination: ");
        String arrivalStation = reader.nextLine();

        return arrivalStation;
    }

    private void suggestProperStation(String typedStation,List<Station> stations) {
        List<String> suggestedStations = new ArrayList<>();
        for(Station station: stations) {
            if (station.stationName.toUpperCase().contains(typedStation.trim().toUpperCase())) {
                suggestedStations.add(station.stationName);
            }
        }
        if (suggestedStations.size()!=0){
            System.out.print("Suggested stations: ");
            for (String stationName: suggestedStations){
                System.out.print(stationName + ", ");
            }
            System.out.println();
        }
    }

    private TrainsList createTrainsList(String departureStation, String arrivalStation, List<Station> stations) throws Exception {
        TrainsList tl = new TrainsList();
        String departureStationShortCode = StationsListHelper.convertStationNameToShortCode(departureStation, stations);
        if (departureStationShortCode == null){
            System.out.println(String.format("INCORRECT DEPARTURE STATION: '%s'", departureStation));
            suggestProperStation(departureStation, stations);
        }
        String arrivalStationShortCode = StationsListHelper.convertStationNameToShortCode(arrivalStation,stations);
        if (arrivalStationShortCode == null){
            System.out.println(String.format("INCORRECT ARRIVAL STATION: '%s'", arrivalStation));
            suggestProperStation(arrivalStation,stations);
        }
        tl.setTrains(readTrainJSONData(departureStationShortCode, arrivalStationShortCode));
        return tl;
    }

    static List<Train> readTrainJSONData(String departureStation, String arrivalStation) {
        String between = departureStation + "/" + arrivalStation;
        List<Train> trains = null;
        String baseurl = "https://rata.digitraffic.fi/api/v1";
        try {
            URL url = new URL(URI.create(String.format("%s/live-trains/station/" + between, baseurl)).toASCIIString());
            ObjectMapper mapper = new ObjectMapper();
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Train.class);

            trains = mapper.readValue(url, listType);
        } catch (Exception ex) {
            System.err.println("No connections were found between chosen departure and arrival stations");
        }
        return trains;
    }

    private void SearchNextTrain(List<Station> stations, Scanner reader) {
        String departureStation = null;
        String arrivalStation = null;
        TrainsList tl = null;
        for(;;) {
            departureStation = askDepartureStation(reader);
            if (departureStation.equalsIgnoreCase("X")) {
                break;
            } arrivalStation = askArrivalStation(reader);
            if (arrivalStation.equals("Tylypahka") || arrivalStation.equals("Hogwarts")) {
                System.err.println("\nAll trains to Hogwarts leaves from London King's Cross railway station platform 9 3/4.");
                break;
            }
            if (arrivalStation.equalsIgnoreCase("X")){
                break;
            }
            try{
                tl = createTrainsList(departureStation, arrivalStation, stations);
                System.out.println("Next train directly from " + departureStation+ " to " + arrivalStation + " leaves today at: " + tl.nextTrain());
                System.out.println("");
                break;
            }catch (Exception e){
                System.out.println();
            }
        }
    }

    private void allTrainsBetweenStations(Scanner reader, List<Station> stations) {
        String departureStation = null;
        String arrivalStation = null;
        TrainsList tl = null;
        for (; ; ) {
            departureStation = askDepartureStation(reader);
            if (departureStation.equalsIgnoreCase("X")) {
                break;
            }
            arrivalStation = askArrivalStation(reader);
            if (arrivalStation.equalsIgnoreCase("X")) {
                break;
            }
            try {
                tl = createTrainsList(departureStation, arrivalStation, stations);
                tl.printTrains(tl, departureStation, arrivalStation);
                break;

            } catch (Exception e) {
                System.out.println();
            }
        }
    }

    private void searchAllStopsBetweenStations(Scanner reader, List<Station> stations) {
        TrainsList tl = null;
        String departureStation = null;
        String arrivalStation = null;

        for (; ; ) {
            departureStation = askDepartureStation(reader);
            String departureShortcode = StationsListHelper.convertStationNameToShortCode(departureStation, stations);
            if (departureStation.equalsIgnoreCase("X")) {
                break;

            }
            arrivalStation = askArrivalStation(reader);
            String arrivalShortCode = StationsListHelper.convertStationNameToShortCode(arrivalStation,stations);
            if (arrivalStation.equalsIgnoreCase("X")) {
                break;
            }

            try {
                tl = createTrainsList(departureStation, arrivalStation, stations);

                tl.printTrains(tl, departureStation, arrivalStation); // calls method from TrainsList-class which prints out all the trains from between stations given as parametres

                int trainNumber = askTrainNumber(reader);
                List<TimeTableRow> rows = new ArrayList<>();
                for (Train train : tl.getTrains()) {
                    if (train.getTrainNumber() == trainNumber) {
                        rows = train.getTimeTableRows();
                    }
                }
                List<TimeTableRow> filteredRows = new ArrayList<>();
                int wantedBeginning = 0;
                int wantedEnd = 0;

                for(int i = 0; i<rows.size(); i++){
                    if (rows.get(i).getStationShortCode().equals(departureShortcode)){
                        wantedBeginning = i;
                    }
                    if (rows.get(i).getStationShortCode().equals(arrivalShortCode)){
                        wantedEnd = i;
                    }
                }
                filteredRows= rows.subList(wantedBeginning,wantedEnd);


                System.out.println(filteredRows.get(0).getScheduledTime().toLocalTime() + " " + departureStation);
                System.out.print(stoppingTimeAtStations(filteredRows, stations));
                System.out.println(rows.get(filteredRows.size()-1).getScheduledTime().toLocalTime() + " " + arrivalStation);

                break;

            } catch (Exception e) {
            }
        }
    }

    private StringBuilder stoppingTimeAtStations(List<TimeTableRow> rows, List<Station> stations){
        StringBuilder stationAndTime = new StringBuilder();
        int i = 1;
        Duration stoppingtime = null;
        List<StringBuilder> stoppingstations = new ArrayList<>();
        String stoppingstation = null;
        while (i < rows.size()-1) {
            i++;
            stoppingstation  = StationsListHelper.convertStationShortCodeToStationName(rows.get(i).getStationShortCode(), stations);
            LocalDateTime arrival = rows.get(i).getScheduledTime();
            LocalDateTime departure = rows.get(i+1).getScheduledTime();
            stoppingtime = Duration.between(arrival, departure);
            long stime = stoppingtime.toMinutes();
            String time = String.valueOf(stime);
            int t = Integer.valueOf(time);
            if (rows.get(i).getTrainStopping()) {
                if (!(stoppingstation == null)) {
                    stationAndTime.append(arrival.toLocalTime()).append(" ").append(stoppingstation).append(", stops for ").append(time);
                    if (Integer.valueOf(time) ==1){
                        stationAndTime.append(" minute\n");
                    }else {
                        stationAndTime.append(" minutes\n");
                    }
                    stoppingstations.add(stationAndTime);
                }
            }
            i++;
        }
        return stationAndTime;
    }

/*    public List<Wagon> getWagonList(int trainNumber) {
        List<Wagon> wagons = new ArrayList<>();
        wagons = WagonListHelper.readTrainJSONDataAndListTrainCompositions(trainNumber);

        return wagons;
    }

    public void getCatering(int trainNumber) {
      *//*  List<Wagon> wagons = new ArrayList<>();
        wagons = getWagonList(trainNumber);*//*

        for (Wagon wagon : getWagonList(trainNumber)) {
            if (wagon.getCatering() == true) {
                if (wagon.getWagonType().equals("Sm6")) {
                    getRestaurantMenu("AllegroRavintolavaunuHinnasto.pdf");
                }
                getRestaurantMenu("RavintolavaunuHinnasto.pdf");
            }
        }
        System.out.println("No catering in the train!");
    }

    public void getRestaurantMenu(String file) {
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(file));
            if (!document.isEncrypted()) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                System.out.println("Text:" + text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}