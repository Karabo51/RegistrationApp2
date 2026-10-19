/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationapp;

import java.util.Scanner;

/**
 * Handles the QuickChat menu (Part 2). Only called once the user has logged in.
 *
 * @author Student
 */
public class QuickChat {
    public static void run(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");

        int numMessages = readInt(scanner, "How many messages do you wish to enter? ");
        int messagesEntered = 0;
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int choice = readInt(scanner, "Choose an option: ");

            switch (choice) {
                case 1:
                    if (messagesEntered >= numMessages) {
                        System.out.println("You have already entered all " + numMessages + " messages.");
                        break;
                    }
                    for (int i = messagesEntered; i < numMessages; i++) {
                        System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");
                        enterMessage(scanner, i);
                        messagesEntered++;
                    }
                    System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Please choose option 1, 2 or 3.");
            }
        }
    }

    /** Collects the recipient and text for one message, then sends/stores/disregards it. */
    private static void enterMessage(Scanner scanner, int messageNumber) {
        String recipient;
        do {
            System.out.print("Enter recipient cell number (e.g. +27718693002): ");
            recipient = scanner.nextLine().trim();
            if (!Message.isValidRecipient(recipient)) {
                System.out.println(Message.CELL_BAD);
            }
        } while (!Message.isValidRecipient(recipient));
        System.out.println(Message.CELL_OK);

        String text;
        do {
            System.out.print("Enter your message: ");
            text = scanner.nextLine();
            if (!Message.checkMessageLength(text).equals(Message.MSG_READY)) {
                System.out.println("Please enter a message of less than 250 characters.");
                System.out.println(Message.checkMessageLength(text));
            }
        } while (!Message.checkMessageLength(text).equals(Message.MSG_READY));
        System.out.println("Message sent");

        Message message = new Message(messageNumber, recipient, text);
        System.out.println(message.getMessageIDStatement());

        String result = message.sentMessage(scanner);
        System.out.println(result);

        if (result.equals(Message.MSG_SENT)) {
            System.out.println("\n" + message.getDetails());
        } else if (result.equals(Message.MSG_DISREGARD)) {
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                System.out.println("Message deleted.");
            }
        }
    }

    /** Keeps asking until the user enters a whole number. */
    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
    

