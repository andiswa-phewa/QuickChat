/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.iie.prog5121.quickchatpart2;

/**
 *
 * @author anphewa
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * PROG5121 - Part 2 message operations.
 * Main ideas: variables, constructors, if statements, FOR loops, String methods.
 * The small storeMessage() section is the PoE's independent JSON research task.
 */
public class Message {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private boolean processed = false;

    private static int sentCount = 0;
    private static String sentMessages = "";
    // Keep used IDs in a String, so we do NOT need arrays or HashSet yet.
    private static String usedIDs = "|";

    public Message(int number, String recipient, String text) {
        this.messageID = generateMessageID();
        this.messageNumber = number;
        this.recipient = recipient;
        this.messageText = text;
    }

    // Extra constructor for unit tests: a known ID produces a predictable hash.
    public Message(String testID, int number, String recipient, String text) {
        this.messageID = testID;
        this.messageNumber = number;
        this.recipient = recipient;
        this.messageText = text;
    }

    // Ten random digits. A WHILE loop prevents duplicates in the current run.
    private static String generateMessageID() {
        String id;
        do {
            id = "";
            for (int i = 0; i < 10; i++) {
                int digit = (int) (Math.random() * 10);
                id = id + digit;
            }
        } while (usedIDs.contains("|" + id + "|"));
        usedIDs = usedIDs + id + "|";
        return id;
    }

    public boolean checkMessageID() {
        return messageID.length() == 10 && messageID.matches("[0-9]{10}");
    }

    public String checkRecipientCell() {
        return validateRecipient(recipient);
    }

    // Official example +27718693002 must pass (12 characters including +27).
    // A local number without its country code must fail.
    public static String validateRecipient(String cell) {
        if (cell != null && cell.matches("\\+27[6-8][0-9]{8}")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        return validateMessageLength(messageText);
    }

    public static String validateMessageLength(String text) {
        if (text == null || text.trim().length() == 0) {
            return "Please enter a message.";
        }
        if (text.length() > 250) {
            int over = text.length() - 250;
            return "Message exceeds 250 characters by " + over
                    + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    // ID's first 2 digits : FOR loop counter : FIRST word + LAST word, uppercase.
    // A trailing question mark does not form part of the final word.
    // ID 0012345678, number 0 and 'Hi Mike, can you join us for dinner tonight?'
    // produce exactly: 00:0:HITONIGHT
    public String createMessageHash() {
        String sentence = messageText.trim();
        if (sentence.length() == 0) {
            return "";
        }

        int firstSpace = sentence.indexOf(" ");
        int lastSpace = sentence.lastIndexOf(" ");
        String firstWord;
        String lastWord;
        if (firstSpace == -1) {
            firstWord = sentence;
            lastWord = sentence;
        } else {
            firstWord = sentence.substring(0, firstSpace);
            lastWord = sentence.substring(lastSpace + 1);
        }
        // String manipulation replaces punctuation around the words.
        firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "");

        return (messageID.substring(0, 2) + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // Action 1 = send; 2 = ask to disregard; 3 = store in JSON.
    public String SentMessage(int action) {
        if (processed) {
            return "This message has already been processed.";
        }
        if (!checkMessageID()) {
            return "Message ID is invalid.";
        }
        if (!checkRecipientCell().equals("Cell phone number successfully captured.")) {
            return checkRecipientCell();
        }
        if (!checkMessageLength().equals("Message ready to send.")) {
            return checkMessageLength();
        }

        if (action == 1) {
            sentCount++;
            sentMessages = sentMessages + getDetails() + "\n\n";
            processed = true;
            return "Message successfully sent.";
        } else if (action == 2) {
            return "Press 0 to delete the message.";
        } else if (action == 3) {
            if (storeMessage()) {
                processed = true;
                return "Message successfully stored.";
            }
            return "Message could not be stored. Please try again.";
        }
        return "Please select 1, 2 or 3.";
    }

    public void disregardMessage() {
        processed = true;
    }

    public String printMessages() {
        if (sentMessages.length() == 0) {
            return "No messages sent yet.";
        }
        return sentMessages;
    }

    // The method name matches the unusual spelling in the PoE.
    public static int returnTotalMessagess() {
        return sentCount;
    }

    public String getDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + createMessageHash() + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    public String getMessageID() {
        return messageID;
    }

    // JSON writing is specifically listed as a RESEARCH task in Part 2.
    // Using File, Scanner and FileWriter instead of java.nio or collections.
    // Reference: Oracle Java FileWriter API:
    // https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileWriter.html
    public boolean storeMessage() {
        try {
            File file = new File("storedMessages.json");
            String oldText = "";

            // Keep messages stored in previous runs; do not erase them.
            if (file.exists()) {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    oldText = oldText + reader.nextLine();
                }
                reader.close();
            }

            String entry = "  {\"messageID\":\"" + escapeJSON(messageID) + "\","
                    + "\"messageNumber\":" + messageNumber + ","
                    + "\"messageHash\":\"" + escapeJSON(createMessageHash()) + "\","
                    + "\"recipient\":\"" + escapeJSON(recipient) + "\","
                    + "\"message\":\"" + escapeJSON(messageText) + "\"}";

            String newText;
            oldText = oldText.trim();
            if (oldText.length() == 0 || oldText.equals("[]")) {
                newText = "[\n" + entry + "\n]";
            } else if (oldText.startsWith("[") && oldText.endsWith("]")) {
                // Remove only the closing ], add a comma, the new message and ].
                newText = oldText.substring(0, oldText.length() - 1)
                        + ",\n" + entry + "\n]";
            } else {
                System.out.println("The existing JSON file has an incorrect format.");
                return false;
            }

            FileWriter writer = new FileWriter(file);
            writer.write(newText);
            writer.close();
            return true;
        } catch (IOException error) {
            System.out.println("File error: " + error.getMessage());
            return false;
        }
    }

    // A quotation mark or backslash inside the message must not break JSON.
    private String escapeJSON(String text) {
        String answer = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"') {
                answer = answer + "\\\"";
            } else if (c == '\\') {
                answer = answer + "\\\\";
            } else if (c == '\n') {
                answer = answer + "\\n";
            } else if (c == '\r') {
                answer = answer + "\\r";
            } else if (c == '\t') {
                answer = answer + "\\t";
            } else if (c < 32) {
                // Escape below protects unusual control characters.
                String code = Integer.toHexString(c);
                while (code.length() < 4) {
                    code = "0" + code;
                }
                answer = answer + "\\u" + code;
            } else {
                answer = answer + c;
            }
        }
        return answer;
    }
}
