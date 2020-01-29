package app;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;


import java.io.File;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
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
        System.out.println("Welcome!");

        menu(reader, stations);
    }



    private void menu(Scanner reader, List<Station> stations) {
        for ( ; ; ) {
            System.out.println("");
            System.out.print("1. Search next train between chosen stations\n" + "2. Search all trains between chosen stations\n"
                    + "3. Search all stops and stopping time for specific train\n" + "0. Exit");
            System.out.println("");

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
                searchAllStops(reader, stations);
            }
            return true;
    }


    private void searchAllStops(Scanner reader, List<Station> stations) {
        String departureStation = askDepartureStation(reader);
        String arrivalStation = askArrivalStation(reader);
        TrainsList tl = createTrainsList(departureStation, arrivalStation, stations);

        tl.printTrains(tl, departureStation, arrivalStation); // calls method from TrainsList-class which prints out all the trains from between stations given as parametres

        int trainNumber = askTrainNumber(reader);
        List<TimeTableRow> rows = new ArrayList<>();
        for (Train train : tl.getTrains()) {
            if (train.getTrainNumber() == trainNumber) {
                rows = train.getTimeTableRows();
            }
        }
        System.out.println(stoppingTimeAtStations(rows, stations));

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
        System.out.println("");
        System.out.println("Next train directly from " + departureStation+ " to " + arrivalStation + " leaves today at: " + tl.nextTrain());
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
            tl.printTrains(tl, departureStation, arrivalStation);
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private StringBuilder stoppingTimeAtStations(List<TimeTableRow> rows, List<Station> stations){
        StringBuilder stationAndTime = new StringBuilder();
        int i = 1;
        Duration stoppingtime = null;
        List<StringBuilder> stoppingstations = new ArrayList<>();
        String stoppingstation = null;
        while (i < rows.size()-1) { // tässä tulee moka, lista käydään läpi liian monta kertaa koska rivejä käsitellään parina
            stoppingstation  = StationsListHelper.convertStationShortCodeToStationName(rows.get(i).getStationShortCode(), stations);
            LocalDateTime arrival = rows.get(i).getScheduledTime();
            LocalDateTime departure = rows.get(i+1).getScheduledTime();
            stoppingtime = Duration.between(arrival, departure);
            long stime = stoppingtime.toMinutes();
            String time = String.valueOf(stime);
            int t = Integer.valueOf(time);
            if (rows.get(i).getTrainStopping()) {
                stationAndTime.append(stoppingstation).append(", stops for ").append(time).append(" minutes\n");
                stoppingstations.add(stationAndTime);
            }
            i = i+2;
        }
        return stationAndTime;
    }
}