/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AccountManagement;

import data.fileManipulation;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.asu.main.TravelManageSys;

import TravelManagement.BookedTravels;
import TravelManagement.Trip;

/**
 *
 * @author bmood
 */
public class CustomerBooking {
    private ArrayList<Trip> tripsList = new ArrayList<>();
    ArrayList<BookedTravels> CustomerBookedTrips = new ArrayList<>();
    String[] CustomerTravelHistory;

    // Filter Search Preferences
    private double price_start;
    private double price_end;
    private String search_text;
    private String start_date;
    private String end_date;

    public void mainCustomer() {
        ArrayList<Trip> FeaturedTrips = new ArrayList<>();
        tripsList = fileManipulation.getAllTrips();
        FeaturedTrips = tripsList.stream()
                .limit(3)
                .collect(Collectors.toCollection(ArrayList::new));
        char Ans;
        Trip ChosenTrip = null;
        Scanner input = new Scanner(System.in);
        Trip.displayTrips(FeaturedTrips);
        System.out.println("\nA. Search for a trip(s)");
        System.out.println("B. Go Back");
        System.out.print("Choice: ");
        Ans = Character.toLowerCase(input.next().charAt(0));
        switch (Ans) {
            case 'a':
                SearchTrips(Ans, ChosenTrip);
                break;
            case 'b':
                // Go Back.
                break;
            default:
                ErrorMessage("Wrong Input.. Try again!", 2000);
                break;
        }
        input.close();
    }

    private void SearchTrips(char Ans, Trip ChosenTrip) {
        Scanner input = new Scanner(System.in);
        System.out.println("\nExample: TripName/StartDate/EndDate...");
        System.out.print("Search for a trip: ");
        ArrayList<Trip> filteredTrips = this.getFilteredTrips(input.nextLine());
        Trip.displaySearchTrips(filteredTrips);
        System.out.println("\nA. Search More Trips.");
        System.out.println("B. Book a Trip.");
        System.out.println("C. Show Trip details.");
        System.out.println("D. Go Back.");
        Ans = Character.toLowerCase(input.next().charAt(0));
        switch (Ans) {
            case 'a':
                SearchTrips(Ans, ChosenTrip);
                break;
            case 'b':
                System.out.println("\nWhich trip do you want to book?\t(Use the ID)");
                System.out.print("Trip ID: ");
                ChosenTrip = getTrip(input.next());
                if (ChosenTrip == null)
                    ErrorMessage("No Trips Found!", 2000);
                addBookingTrip(ChosenTrip);
                // Call Ticket Functions!
                System.out.println("You successfully booked " + ChosenTrip.getTitle() + " Trip");
                break;
            case 'c':
                ShowTripDetails(ChosenTrip, Ans);
                break;
            case 'd':
                mainCustomer();            
                break;
            default:
                ErrorMessage("Wrong Input.. Try again!", 2000);
                break;
        }

    }

    private void ShowTripDetails(Trip ChosenTrip, char Ans) {
        Scanner input = new Scanner(System.in);
        System.out.println("\nWhich trip do you want to book?\t(Use the ID)");
        System.out.print("Trip ID: ");
        ChosenTrip = getTrip(input.next());
        if (ChosenTrip == null)
            ErrorMessage("No Trips Found!", 2000);
        Trip.displayTripDetails(ChosenTrip);
        System.out.println("A. Book " + ChosenTrip.getTitle() + " trip");
        System.out.println("B. Go Back");
        System.out.print("Choice: ");
        Ans = Character.toLowerCase(input.next().charAt(0));
        switch (Ans) {
            case 'a':
                addBookingTrip(ChosenTrip);
                // Call Ticket Functions!
                System.out.println("You successfully booked " + ChosenTrip.getTitle() + " Trip");
                break;
            case 'b':
                mainCustomer();
                break;
            default:
                ErrorMessage("Wrong Input.. Try again!", 2000);
                break;
        }
        input.close();
    }

    private void addBookingTrip(Trip ChosenTrip) {
        CustomerBookedTrips
                .add(new BookedTravels(ChosenTrip.getTripId(), ChosenTrip.getTitle(), null, null, 0, null));
    }

    private void ErrorMessage(String message, int timeout) {
        try {
            System.out.println(message);
            Thread.sleep(timeout);
            // System.out.print("\033[H\033[2J");
            // System.out.flush();
            mainCustomer();
        } catch (Exception e) {
            System.out.println("Thread error sleeping.");
            // e.printStackTrace();
        }
    }

    private Trip getTrip(String id) {
        for (Trip bookedTravels : tripsList) {
            if (bookedTravels.getTripId().equals(id)) {
                return bookedTravels;
            }
        }
        return null;
    }

    public ArrayList<Trip> getFilteredTrips(String search_filter) {
        ArrayList<Trip> filteredTrips = new ArrayList<>();
        String[] Filters = search_filter.split("/");
        // Split Filters.
        setFilters(Filters);

        // Iterate through the trips and filter based on the search_text
        for (Trip trip : tripsList) {
            try {
                if (tripSearch(trip, search_text, start_date, end_date, price_start, price_end)) {
                    filteredTrips.add(trip);
                }
            } catch (ParseException e) {
                System.out.println(
                        "Error in filtering..\nCaused by Invalid Date Parsing.\nProper Date Format: dd-mm-yyyy\n\n");
                mainCustomer();
            }
        }
        if (filteredTrips.isEmpty())
            ErrorMessage("\nNo Trip Found with these preferences..", 3000);
        return filteredTrips;
        // Checkout no cancellation - Save in a file.
        // Booking Save in runtime variables (Array List) - Not save in a file.
    }

    private void setFilters(String[] Filters) {
        for (int i = 0; i < Filters.length; i++) {
            if (Filters[i].matches("\\d{1,2}-\\d{1,2}-\\d{4}")) {
                start_date = Filters[i];
                end_date = Filters[i + 1];
                i += 1;
                continue;
            }
            if (Filters[i].matches("\\d+(\\.\\d+)?")) {
                try {
                    price_start = Double.parseDouble(Filters[i]);
                    price_end = Double.parseDouble(Filters[i + 1]);
                    i += 1;
                    continue;
                } catch (Exception e) {
                    System.out.println("Process <Parsing> -> Error(s)\nParsing 'price_start', 'price_end'");
                    continue;
                }
            }
            search_text = Filters[i];
        }
    }

    private boolean tripSearch(Trip trip, String search_filter) {
        if (search_filter == null)
            return true;
        return trip.getTitle().toLowerCase().contains(search_filter.toLowerCase())
                || trip.getDescription().toLowerCase().contains(search_filter.toLowerCase())
                || trip.getTripType().toLowerCase().contains(search_filter.toLowerCase());
    }

    private boolean tripSearch(Trip trip, String search_start_date, String search_end_date) throws ParseException {
        if (search_start_date == null || search_end_date == null)
            return true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date start_date = dateFormat.parse(search_start_date);
        Date end_date = dateFormat.parse(search_end_date);
        Date tripStartDates[] = trip.getStartDate();
        Date tripEndDates[] = trip.getEndDate();
        Boolean FoundDate = false;

        for (Date s_date : tripStartDates) {
            FoundDate = s_date.after(start_date) || s_date.equals(start_date);
        }
        for (Date e_date : tripEndDates) {
            FoundDate = e_date.after(end_date) || e_date.equals(end_date);
        }
        return FoundDate;
    }

    private boolean tripSearch(Trip trip, double price_start, double price_end) {
        double tripPrice = trip.getInitPrice();
        return tripPrice >= price_start && (price_end > 0 && price_end > price_start) ? tripPrice <= price_end : true;
    }

    private boolean tripSearch(Trip trip, String search_filter, String start_date, String end_date, double price_start,
            double price_end) throws ParseException {
        return tripSearch(trip, price_start, price_end) && tripSearch(trip, start_date, end_date)
                && tripSearch(trip, search_filter);
    }

}