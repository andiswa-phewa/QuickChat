/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.iie.prog5121.quickchatpart2;

/**
 *
 * @author anphewa
 */
import java.util.Scanner;

/**
 * PROG5121 Part 2 - QuickChat beginner demonstration.
 * Put Login.java, Message.java and Main.java in ONE NetBeans package.
 * Start by running Main.java. No GUIs or JOptionPane.
 */
public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== CREATE YOUR ACCOUNT ===");
        Login user;
        boolean registered = false;

        // Keep asking until the Part 1 registration rules are satisfied.
        do {
            System.out.print("First name: ");
            String firstName = input.nextLine();
            System.out.print("Last name: ");
            String lastName = input.nextLine();
            System.out.print("Username (include _, maximum 5 characters): ");
            String username = input.nextLine();
            System.out.print("Password (8+ chars, uppercase, number, special): ");
            String password = input.nextLine();
            System.out.print("Cellphone in +27 format: ");
            String cellPhone = input.nextLine();

            user = new Login(firstName, lastName, username, password, cellPhone);
            String result = user.registerUser();
            System.out.println(result);
            registered = result.equals("Registration successful.");
        } while (!registered);

        // Users cannot access QuickChat unless they log in successfully.
        boolean loggedIn = false;
        System.out.println("\n=== LOGIN ===");
        while (!loggedIn) {
            System.out.print("Enter username: ");
            String username = input.nextLine();
            System.out.print("Enter password: ");
            String password = input.nextLine();
            System.out.println(user.returnLoginStatus(username, password));
            loggedIn = user.loginUser(username, password);
        }

        System.out.println("\nWelcome to QuickChat.");
        int messageLimit = readPositiveNumber(input);
        int messagesEntered = 0;
        boolean running = true;

        // WHILE loop: menu remains active until option 3 is chosen.
        while (running) {
            System.out.println("\n=== QUICKCHAT MENU ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String menuChoice = input.nextLine();

            if (menuChoice.equals("1")) {
                int remaining = messageLimit - messagesEntered;
                if (remaining == 0) {
                    System.out.println("You have reached your message limit.");
                }

                // FOR loop: do not create more than the user's message limit.
                for (int i = 0; i < remaining; i++) {
                    System.out.println("\nMESSAGE " + (messagesEntered + 1)
                            + " OF " + messageLimit);
                    String recipient = "";
                    String messageText = "";
                    boolean validRecipient = false;
                    boolean validMessage = false;

                    while (!validRecipient) {
                        System.out.print("Recipient (for example +27718693002): ");
                        recipient = input.nextLine();
                        String result = Message.validateRecipient(recipient);
                        System.out.println(result);
                        validRecipient = result.equals("Cell phone number successfully captured.");
                    }

                    while (!validMessage) {
                        System.out.print("Type message (maximum 250 characters): ");
                        messageText = input.nextLine();
                        String result = Message.validateMessageLength(messageText);
                        System.out.println(result);
                        validMessage = result.equals("Message ready to send.");
                    }

                    // The FOR-loop counter starts at 0 for the first hash.
                    // This matches the assessment example 00:0:HITONIGHT.
                    Message message = new Message(messagesEntered, recipient, messageText);
                    messagesEntered++;

                    System.out.println("\nMESSAGE PREVIEW");
                    System.out.println(message.getDetails());

                    boolean completed = false;
                    while (!completed) {
                        System.out.println("\n1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Select 1, 2 or 3: ");
                        String action = input.nextLine();
                        String result;

                        if (action.equals("1")) {
                            result = message.SentMessage(1);
                            System.out.println(result);
                            if (result.equals("Message successfully sent.")) {
                                // The PoE requires these details after a message is sent.
                                System.out.println("\nSENT MESSAGE DETAILS");
                                System.out.println(message.getDetails());
                                completed = true;
                            }
                        } else if (action.equals("2")) {
                            System.out.println(message.SentMessage(2));
                            System.out.print("Enter 0 to confirm deletion: ");
                            if (input.nextLine().equals("0")) {
                                message.disregardMessage();
                                System.out.println("Message disregarded.");
                                completed = true;
                            } else {
                                System.out.println("Deletion cancelled. Choose again.");
                            }
                        } else if (action.equals("3")) {
                            result = message.SentMessage(3);
                            System.out.println(result);
                            completed = result.equals("Message successfully stored.");
                        } else {
                            System.out.println("Invalid action. Choose 1, 2 or 3.");
                        }
                    }
                    System.out.println("Total messages sent: "
                            + Message.returnTotalMessagess());
                }
                System.out.println("\nTotal number of messages sent: "
                        + Message.returnTotalMessagess());
            } else if (menuChoice.equals("2")) {
                // Part 2 specifically instructs students to keep this undeveloped.
                System.out.println("Coming Soon.");
            } else if (menuChoice.equals("3")) {
                System.out.println("Total number of messages sent: "
                        + Message.returnTotalMessagess());
                System.out.println("Goodbye!");
                running = false;
            } else {
                System.out.println("Invalid menu option. Please enter 1, 2 or 3.");
            }
        }
    }

    // Avoid exceptions for menu input: hasNextInt is already enough for beginners.
    private static int readPositiveNumber(Scanner input) {
        int number = 0;
        while (number <= 0) {
            System.out.print("How many messages would you like to enter? ");
            if (input.hasNextInt()) {
                number = input.nextInt();
                input.nextLine(); // Remove the Enter key left after nextInt().
                if (number <= 0) {
                    System.out.println("Please enter a number greater than zero.");
                }
            } else {
                input.nextLine();
                System.out.println("Please enter a whole number.");
            }
        }
        return number;
    }
}
