package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import AccountManagement.Customers;
import TravelManagement.Trip;
import TravelManagement.CoupleTours;
import TravelManagement.FamilyTours;
import TravelManagement.GeneralTours;

public class fileManipulation {
    public void getAllTrips() {
        try {
            ArrayList<Trip> AllTrips = new ArrayList<>();
            Path path = Paths.get("TravelManageSys/src/main/java/data/trips.txt");
            String val = Files.readString(path);
            String Trips[] = val.split("\\s+---\\s+");
            for (String t : Trips) {
                String[] trip = t.split(System.lineSeparator());
                if (trip[2].toLowerCase().equals("family")) {
                    AllTrips.add(new FamilyTours(trip[0], trip[1], trip[2], Double.parseDouble(trip[3]), trip[4],
                            trip[5], trip[6],
                            null, 150, null, null, null, null));
                } else if (trip[2].toLowerCase().equals("general")) {
                    AllTrips.add(new GeneralTours(trip[0], trip[1], trip[2], Double.parseDouble(trip[3]), trip[4],
                            trip[5], "",
                            null, 150, null, null, null, null));
                } else if (trip[2].toLowerCase().equals("couple")) {
                    AllTrips.add(new CoupleTours(trip[0], trip[1], trip[2], Double.parseDouble(trip[3]), trip[4],
                            trip[5], "",
                            null, 150, null, null, null, null));
                }
            }
        } catch (Exception e) {

        }


    
    }

     public static void getAllCustomers() {
        try {
            ArrayList<Customers> AllCustomers = new ArrayList<>();
            Path path = Paths.get("TravelManageSys\\TravelManageSys\\src\\main\\java\\data\\customers.txt");
            String val = Files.readString(path);
            String Customers[] = val.split("\\s+---\\s+");
            for (String c : Customers) {
                String[] customer = c.split(System.lineSeparator());
                String[] Fullname = customer[1].split(" ");
                AllCustomers.add(new Customers(customer[0],Fullname[0],Fullname[1],customer[2],customer[3],Integer.parseInt(customer[4]),customer[5],customer[6],customer[7]));


            }
        } catch (Exception e) {

        }

    }
















}
