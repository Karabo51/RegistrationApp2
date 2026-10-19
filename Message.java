/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationapp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Represents a single QuickChat message and keeps track of every message
 * that has been sent while the program is running.
 *
 * JSON storage uses the Gson library.
 * Reference: Google (2024) Gson User Guide.
 * Available at: https://github.com/google/gson/blob/main/UserGuide.md 

/**
 *
 * @author Student
 */
public class Message {
      // --- Messages shared by the app and the unit tests ---
    public static final String MSG_SENT = "Message successfully sent.";
    public static final String MSG_DISREGARD = "Press 0 to delete the message.";
    public static final String MSG_STORED = "Message successfully stored.";
    public static final String MSG_READY = "Message ready to send.";
    public static final String CELL_OK = "Cell phone number successfully captured.";
    public static final String CELL_BAD = "Cell phone number is incorrectly formatted or does not "
            + "contain an international code. Please correct the number and try again.";

    private static final int MAX_MESSAGE_LENGTH = 250;
    private static final Random RANDOM = new Random();

    // --- Static state: everything sent while the program runs ---
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static int totalMessages = 0;
    private static String storeFile = "storedMessages.json";

    // --- Instance state ---
    private String messageID;
    private int messageNumber;   // loop counter, starts at 0
    private String recipient;
    private String messageText;
    private String messageHash;

    /** Normal constructor: the message ID is generated automatically. */
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    /** Constructor with a fixed ID (used by unit tests so the hash is predictable). */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // --- Getters ---
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }

    /** Generates a random ten-digit ID using string manipulation (substring). */
    private static String generateMessageID() {
        String digits = String.valueOf(RANDOM.nextLong() & Long.MAX_VALUE);
        while (digits.length() < 10) {
            digits = "0" + digits;
        }
        return digits.substring(0, 10);
    }

    /** Returns the text the unit test expects when an ID has been created. */
    public String getMessageIDStatement() {
        return "Message ID generated: " + messageID;
    }

    /** Ensures the message ID is not more than ten characters. */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /** Static validation so the app can check a number before building a Message. */
    public static boolean isValidRecipient(String cell) {
        // Same regex as Login.checkCellPhoneNumber (+27 followed by 1-10 digits).
        return cell != null && Pattern.matches("^\\+27[0-9]{1,10}$", cell);
    }

    /** Ensures the recipient number has an international code and is not too long. */
    public String checkRecipientCell() {
        return isValidRecipient(recipient) ? CELL_OK : CELL_BAD;
    }

    /** Checks the message is not longer than 250 characters. */
    public static String checkMessageLength(String text) {
        if (text != null && text.length() <= MAX_MESSAGE_LENGTH) {
            return MSG_READY;
        }
        int length = (text == null) ? 0 : text.length();
        int over = length - MAX_MESSAGE_LENGTH;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    /**
     * Creates the hash: first two digits of the ID, ':', message number, ':',
     * then the first and last words of the message, all in capitals.
     * Example: 00:0:HITONIGHT
     */
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String firstTwo = messageID.substring(0, 2);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    /** Asks the user what to do with the message, then performs that choice. */
    public String sentMessage(Scanner scanner) {
        System.out.println("Choose an option:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Option: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            choice = -1;
        }
        return sentMessage(choice);
    }

    /** Testable version: 1 = send, 2 = disregard, 3 = store. */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessages++;
                return MSG_SENT;
            case 2:
                return MSG_DISREGARD;
            case 3:
                String storeResult = storeMessage();
                return storeResult;
            default:
                return "Invalid option; the message was not sent.";
        }
    }

    /** The full details of this message in the required order. */
    public String getDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    /** Returns every message sent while the program is running. */
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.getDetails()).append("\n\n");
        }
        return sb.toString();
    }

    /** Returns the total number of messages sent. */
    public static int returnTotalMessages() {
        return totalMessages;
    }

    // ---------------- JSON storage ----------------

    /** Appends this message to the JSON file. */
    public String storeMessage() {
        List<Message> stored = readStoredMessages();
        stored.add(this);
        return writeStoredMessages(stored).equals("OK") ? MSG_STORED
                : "Message could not be stored.";
    }

    /** Overwrites the JSON file with the given messages (used by store and delete). */
    public static String writeStoredMessages(List<Message> messages) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(storeFile)) {
            gson.toJson(messages, writer);
            return "OK";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /** Reads all stored messages from the JSON file (empty list if none). */
    public static List<Message> readStoredMessages() {
        List<Message> stored = new ArrayList<>();
        if (!Files.exists(Paths.get(storeFile))) {
            return stored;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Message>>() { }.getType();
        try (Reader reader = new FileReader(storeFile)) {
            List<Message> fromFile = gson.fromJson(reader, listType);
            if (fromFile != null) {
                stored = fromFile;
            }
        } catch (IOException e) {
            System.out.println("Could not read stored messages: " + e.getMessage());
        }
        return stored;
    }

    // ---------------- Helpers for tests ----------------

    public static void setStoreFile(String fileName) { storeFile = fileName; }

    public static void resetSentMessages() {
        sentMessages.clear();
        totalMessages = 0;
    }
}

