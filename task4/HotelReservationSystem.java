import java.util.*;
public class HotelReservationSystem {

    static class Room {
        int roomNumber;
        String category;
        double price;
        boolean isBooked;
        public Room(int roomNumber, String category, double price) {
            this.roomNumber = roomNumber;
            this.category = category;
            this.price = price;
            this.isBooked = false;
        }
    }
    static class ReservationGroup {
        String guestName;
        List<Room> bookedRooms = new ArrayList<>();
        boolean isPaid = false;
        String paymentMethod = "Not Paid";

        public ReservationGroup(String guestName) {
            this.guestName = guestName;
        }
        public double getTotalAmount() {
            return bookedRooms.stream().mapToDouble(r -> r.price).sum();
        }
        public void processPayment(Scanner scanner) {
            System.out.println("\nTotal amount to be paid for all rooms: $" + getTotalAmount());
            System.out.println("Select Payment Method:");
            System.out.println("1. Credit Card");
            System.out.println("2. UPI");
            System.out.println("3. Cash");
            System.out.print("Enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                choice = 3;
            }
            switch (choice) {
                case 1 -> {
                    paymentMethod = "Credit Card";
                    System.out.print("Enter Credit Card Number: ");
                    scanner.nextLine();
                }
                case 2 -> {
                    paymentMethod = "UPI";
                    System.out.print("Enter UPI ID: ");
                    scanner.nextLine();
                }
                case 3 -> {
                    paymentMethod = "Cash";
                    System.out.println("Please pay cash at the counter.");
                }
            }

            System.out.print("Pay now? (yes/no): ");
            String payNow = scanner.nextLine().trim().toLowerCase();

            if (payNow.equals("yes")) {
                isPaid = true;
                System.out.println("Payment successful! $" + getTotalAmount() + " paid via " + paymentMethod + ".");
            } else {
                isPaid = false;
                System.out.println("Payment deferred. Please pay later.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(101, "Standard", 1000));
        rooms.add(new Room(102, "Standard", 1000));
        rooms.add(new Room(103, "Standard", 1000));
        rooms.add(new Room(104, "Standard", 1000));
        rooms.add(new Room(201, "Deluxe", 1500));
        rooms.add(new Room(202, "Deluxe", 1500));
        rooms.add(new Room(203, "Deluxe", 1500));
        rooms.add(new Room(301, "Suite", 3000));
        rooms.add(new Room(302, "Suite", 3000));
        rooms.add(new Room(303, "Suite", 3000));

        List<ReservationGroup> allReservations = new ArrayList<>();

        while (true) {
            System.out.println("\nCA/MA19495");
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. View available rooms");
            System.out.println("2. Make a reservation");
            System.out.println("3. View all reservations");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    int available = 0, booked = 0;
                    System.out.println("\nAvailable Rooms:");
                    for (Room room : rooms) {
                        if (!room.isBooked) {
                            System.out.println("Room " + room.roomNumber + " - " + room.category + " - $" + room.price);
                            available++;
                        } else {
                            booked++;
                        }
                    }
                    System.out.println("Total Available Rooms: " + available);
                    System.out.println("Total Booked Rooms: " + booked);
                }

                case 2 -> {
                    System.out.print("Enter your name: ");
                    String guestName = scanner.nextLine().trim();

                    System.out.println("How many rooms would you like to book?");
                    System.out.print("Suite: ");
                    int suiteCount = Integer.parseInt(scanner.nextLine());
                    System.out.print("Deluxe: ");
                    int deluxeCount = Integer.parseInt(scanner.nextLine());
                    System.out.print("Standard: ");
                    int standardCount = Integer.parseInt(scanner.nextLine());

                    ReservationGroup group = new ReservationGroup(guestName);

                    bookRooms("Suite", suiteCount, rooms, group);
                    bookRooms("Deluxe", deluxeCount, rooms, group);
                    bookRooms("Standard", standardCount, rooms, group);

                    if (!group.bookedRooms.isEmpty()) {
                        group.processPayment(scanner);
                        allReservations.add(group);
                        System.out.println("Reservation completed for " + guestName + ".");
                    } else {
                        System.out.println("No rooms available for booking.");
                    }
                }

                case 3 -> {
                    System.out.println("\nAll Reservations:");
                    if (allReservations.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        for (ReservationGroup group : allReservations) {
                            System.out.println("\nReservation for: " + group.guestName);
                            System.out.println("Rooms Booked:");
                            for (Room r : group.bookedRooms) {
                                System.out.printf("- Room %d (%s) - $%.2f\n", r.roomNumber, r.category, r.price);
                            }
                            System.out.printf("Total Price: $%.2f\n", group.getTotalAmount());
                            System.out.println("Payment Status: " + (group.isPaid ? "Paid via " + group.paymentMethod : "Pending"));
                        }
                    }
                }

                case 4 -> {
                    System.out.println("Thank you for using the Hotel Reservation System. Goodbye!");
                    scanner.close();
                    return;
                }

                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void bookRooms(String category, int count, List<Room> rooms, ReservationGroup group) {
        int booked = 0;
        for (Room room : rooms) {
            if (!room.isBooked && room.category.equalsIgnoreCase(category) && booked < count) {
                room.isBooked = true;
                group.bookedRooms.add(room);
                booked++;
            }
        }
        if (booked < count) {
            System.out.println("Only " + booked + " " + category + " room(s) booked out of requested " + count + ".");
        }
    }
}
