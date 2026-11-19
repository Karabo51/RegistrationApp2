/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Part 3: holds the message arrays and the search/report features.
 * The master arrays (messageIDs, messageHashes, recipients, messageTexts,
 * messageNumbers, flags) are parallel: index i in each describes the same message.
 *
 * @author Student
 */
public class MessageStore {
     public static final String SENT = "Sent";
    public static final String STORED = "Stored";
    public static final String DISREGARD = "Disregard";

    // Required arrays
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<String> storedMessages = new ArrayList<>();
    private final ArrayList<String> messageHashes = new ArrayList<>();
    private final ArrayList<String> messageIDs = new ArrayList<>();

    // Extra parallel arrays needed for searching
    private final ArrayList<String> recipients = new ArrayList<>();
    private final ArrayList<String> messageTexts = new ArrayList<>();
    private final ArrayList<Integer> messageNumbers = new ArrayList<>();
    private final ArrayList<String> flags = new ArrayList<>();

    // --- Getters ---
    public ArrayList<String> getSentMessages() { return sentMessages; }
    public ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public ArrayList<String> getStoredMessages() { return storedMessages; }
    public ArrayList<String> getMessageHashes() { return messageHashes; }
    public ArrayList<String> getMessageIDs() { return messageIDs; }

    /** Adds a message to every array that it belongs in. */
    public void addMessage(Message message, String flag) {
        messageIDs.add(message.getMessageID());
        messageHashes.add(message.getMessageHash());
        recipients.add(message.getRecipient());
        messageTexts.add(message.getMessageText());
        messageNumbers.add(message.getMessageNumber());
        flags.add(flag);
        categoryList(flag).add(message.getMessageText());
    }

    /** Reads the JSON file into the arrays as stored messages. */
    public void loadStoredMessages() {
        for (Message m : Message.readStoredMessages()) {
            addMessage(m, STORED);
        }
    }

    private ArrayList<String> categoryList(String flag) {
        if (flag.equals(SENT)) {
            return sentMessages;
        } else if (flag.equals(STORED)) {
            return storedMessages;
        }
        return disregardedMessages;
    }

    /** a. Sender and recipient of every stored message. */
    public String displayStoredSenderAndRecipient(String sender) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(STORED)) {
                sb.append("Sender: ").append(sender)
                  .append(" | Recipient: ").append(recipients.get(i)).append("\n");
            }
        }
        return sb.length() == 0 ? "No stored messages." : sb.toString();
    }

    /** b. The longest stored message. */
    public String longestStoredMessage() {
        String longest = "";
        for (String text : storedMessages) {
            if (text.length() > longest.length()) {
                longest = text;
            }
        }
        return longest.isEmpty() ? "No stored messages." : longest;
    }

    /** c. Message text for a message ID. */
    public String searchByMessageID(String id) {
        int index = messageIDs.indexOf(id);
        return index == -1 ? "Message ID not found." : messageTexts.get(index);
    }

    /** c. Recipient for a message ID. */
    public String recipientByMessageID(String id) {
        int index = messageIDs.indexOf(id);
        return index == -1 ? "Message ID not found." : recipients.get(index);
    }

    /** d. All sent or stored messages for one recipient. */
    public ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            boolean wanted = flags.get(i).equals(SENT) || flags.get(i).equals(STORED);
            if (wanted && recipients.get(i).equals(recipient)) {
                results.add(messageTexts.get(i));
            }
        }
        return results;
    }

    /** e. Deletes a message using its hash (and updates the JSON file if it was stored). */
    public String deleteByHash(String hash) {
        int index = -1;
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equalsIgnoreCase(hash)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return "Message hash not found.";
        }

        String text = messageTexts.get(index);
        String flag = flags.get(index);

        messageIDs.remove(index);
        messageHashes.remove(index);
        recipients.remove(index);
        messageTexts.remove(index);
        messageNumbers.remove(index);
        flags.remove(index);
        categoryList(flag).remove(text);

        if (flag.equals(STORED)) {
            rewriteStoredFile();
        }
        return "Message: \"" + text + "\" successfully deleted.";
    }

    /** Keeps the JSON file in step with the stored messages in the arrays. */
    private void rewriteStoredFile() {
        List<Message> remaining = new ArrayList<>();
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(STORED)) {
                remaining.add(new Message(messageIDs.get(i), messageNumbers.get(i),
                        recipients.get(i), messageTexts.get(i)));
            }
        }
        Message.writeStoredMessages(remaining);
    }

    /** f. Report of every message with the given flag (Sent, Stored or Disregard). */
    public String generateReport(String flag) {
        StringBuilder sb = new StringBuilder("=== " + flag + " Messages Report ===\n");
        boolean found = false;
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i).equals(flag)) {
                found = true;
                sb.append("Message ID: ").append(messageIDs.get(i)).append("\n")
                  .append("Message Hash: ").append(messageHashes.get(i)).append("\n")
                  .append("Recipient: ").append(recipients.get(i)).append("\n")
                  .append("Message: ").append(messageTexts.get(i)).append("\n\n");
            }
        }
        if (!found) {
            sb.append("No messages to display.\n");
        }
        return sb.toString();
    }
}

