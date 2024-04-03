import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Main {
    private static int[][] morningSeats;
    private static int[][] afternoonSeats;
    private static int[][] nightSeats;
    private static List<BookingDetail> bookingHistory = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("-+".repeat(25));
        System.out.println("CSTAD HALL BOOKING SYSTEM");
        System.out.println("-+".repeat(25));

        System.out.print("> Config total rows in hall: ");
        int totalRows = scanner.nextInt();
        System.out.print("> Config total seats per row in hall: ");
        int totalCols = scanner.nextInt();
        morningSeats = new int[totalRows][totalCols];
        afternoonSeats = new int[totalRows][totalCols];
        nightSeats = new int[totalRows][totalCols];
        scanner.nextLine();

        boolean isRunning = true;
        while(isRunning){
            System.out.println("[[ Application Menu ]]");
            System.out.println("<A> Booking");
            System.out.println("<B> Hall");
            System.out.println("<C> Showtime");
            System.out.println("<D> Reboot Showtime");
            System.out.println("<E> History");
            System.out.println("<F> Exit");

            System.out.print("> Please input no: ");
            char menu = scanner.next().toUpperCase().charAt(0);
            scanner.nextLine();

            switch (menu) {
                case 'A':
                    processBooking(scanner);
                    break;
                case 'B':
                    displayHallInformation("Morning", morningSeats);
                    displayHallInformation("Afternoon", afternoonSeats);
                    displayHallInformation("Night", nightSeats);
                    break;
                case 'C':
                    displayShowtime();
                    break;
                case 'D':
                    morningSeats = new int[totalRows][totalCols];
                    afternoonSeats = new int[totalRows][totalCols];
                    nightSeats = new int[totalRows][totalCols];
                    System.out.println("Showtimes have been rebooted.");
                    break;
                case 'E':
                    displayBookingHistory();
                    break;
                case 'F':
                    System.out.println("Exiting application.");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
        }
    }

    public static void displayShowtime(){
        System.out.println("-+".repeat(25));
        System.out.println("# Showtime information");
        System.out.println("# 1) Morning (10:00AM - 12:30PM)");
        System.out.println("# 2) Afternoon (03:00PM - 05:30PM)");
        System.out.println("# 3) Night (07:00PM - 09:30PM)");
    }

    public static void processBooking(Scanner scanner) {
        displayShowtime();
        System.out.println("Choose a shift to book seats ( 1 | 2 | 3):");
        System.out.print("> ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                bookSeats(scanner, morningSeats, "Morning");
                break;
            case 2:
                bookSeats(scanner, afternoonSeats, "Afternoon");
                break;
            case 3:
                bookSeats(scanner, nightSeats, "Night");
                break;
            default:
                System.out.println("Invalid shift selected.");
                break;
        }
    }

    public static void bookSeats(Scanner scanner, int[][] hallSeats, String shift) {
        System.out.println("Booking for " + shift + " show.");
        displayHallInformation(shift, hallSeats);
        System.out.println("# Instruction");
        System.out.println("# Single: A-1");
        System.out.println("# Multiple (separate by comma): A-1,B-2");
        System.out.print("> Please select available seats: ");
        String input = scanner.nextLine();
        String[] selectedSeats = input.split(",");

        for (String seat : selectedSeats) {
            seat = seat.trim();
            int row = seat.charAt(0) - 'A';
            int col = Integer.parseInt(seat.substring(2)) - 1;

            if (row >= 0 && row < hallSeats.length && col >= 0 && col < hallSeats[row].length) {
                if (hallSeats[row][col] == 0) {
                    hallSeats[row][col] = 1;
                    System.out.println("Seat " + seat + " booked successfully.");
                } else {
                    System.out.println("Seat " + seat + " is already booked.");
                }
            } else {
                System.out.println("Seat " + seat + " is not valid.");
            }
        }
        System.out.println("Current booking status:");
        displayHallInformation(shift, hallSeats);

        String studentId = getStudentId(scanner);

        LocalDateTime bookingTime = LocalDateTime.now();

        BookingDetail bookingDetail = new BookingDetail(shift, studentId, bookingTime, selectedSeats);
        bookingHistory.add(bookingDetail);
    }

    public static void displayHallInformation(String shift, int[][] hallSeats) {
        System.out.println("# Hall - " + shift);
        for (int row = 0; row < hallSeats.length; row++) {
            for (int col = 0; col < hallSeats[row].length; col++) {
                String status = hallSeats[row][col] == 0 ? "AV" : "BK";
                System.out.print("| " + (char) ('A' + row) + "-" + (col + 1) + ":" + status + " ");
            }
            System.out.println("|");
        }
    }

    public static String getStudentId(Scanner scanner) {
        System.out.print("> Please enter student ID: ");
        return scanner.nextLine();
    }

    public static void displayBookingHistory() {
        System.out.println("# Booking History");
        System.out.println("----------------------------------------");
        int no = 1;
        for (BookingDetail detail : bookingHistory) {
            System.out.println("#NO: " + no++);
            System.out.println("#SEATS: " + detail.getSeatsFormatted());
            System.out.println("#HALL\t#STU.ID\t#CREAT");
            System.out.println(detail);
            System.out.println("----------------------------------------");
        }
    }
}

class BookingDetail {
    private String shift;
    private String studentId;
    private LocalDateTime bookingTime;
    private String[] seats;

    public BookingDetail(String shift, String studentId, LocalDateTime bookingTime, String[] seats) {
        this.shift = shift;
        this.studentId = studentId;
        this.bookingTime = bookingTime;
        this.seats = seats;
    }

    public String getSeatsFormatted() {
        return Arrays.toString(seats);
    }

    @Override
    public String toString() {
        return "HALL " + shift.charAt(0) + "\t" + studentId + "\t" + bookingTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));
    }
}

