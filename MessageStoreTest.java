/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Unit tests for the Part 3 arrays, searches and report.
 *
 * @author Student
 */
public class MessageStoreTest {
    private MessageStore store;
    private Message message2;

    /** Loads the five test messages from the assignment into the arrays. */
    @BeforeEach
    public void setUp() {
        Message.setStoreFile("target/test_storedMessages.json");
        store = new MessageStore();

        Message message1 = new Message("1000000001", 0, "+27834557896", "Did you get the cake?");
        message2 = new Message("1000000002", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        Message message3 = new Message("1000000003", 2, "+27834484567", "Yohoooo, I am at your gate.");
        Message message4 = new Message("0838884567", 3, "0838884567", "It is dinner time!");
        Message message5 = new Message("1000000005", 4, "+27838884567", "Ok, I am leaving without you.");

        store.addMessage(message1, MessageStore.SENT);
        store.addMessage(message2, MessageStore.STORED);
        store.addMessage(message3, MessageStore.DISREGARD);
        store.addMessage(message4, MessageStore.SENT);
        store.addMessage(message5, MessageStore.STORED);
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals(List.of("Did you get the cake?", "It is dinner time!"), store.getSentMessages());
    }

    @Test
    public void testOtherArraysPopulated() {
        assertEquals(List.of("Yohoooo, I am at your gate."), store.getDisregardedMessages());
        assertEquals(2, store.getStoredMessages().size());
        assertEquals(5, store.getMessageHashes().size());
        assertEquals(5, store.getMessageIDs().size());
    }

    @Test
    public void testLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                store.longestStoredMessage());
    }

    @Test
    public void testSearchByMessageID() {
        assertEquals("It is dinner time!", store.searchByMessageID("0838884567"));
    }

    @Test
    public void testSearchByMessageID_NotFound() {
        assertEquals("Message ID not found.", store.searchByMessageID("9999999999"));
    }

    @Test
    public void testSearchByRecipient() {
        assertEquals(List.of("Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."),
                store.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteByHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\""
                + " successfully deleted.", store.deleteByHash(message2.getMessageHash()));
        assertEquals(1, store.getStoredMessages().size());
    }

    @Test
    public void testDeleteByHash_NotFound() {
        assertEquals("Message hash not found.", store.deleteByHash("00:0:NOPE"));
    }

    @Test
    public void testDisplayReport() {
        String report = store.generateReport(MessageStore.SENT);
        assertTrue(report.contains("10:0:DIDCAKE"));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("08:3:ITTIME"));
        assertTrue(report.contains("It is dinner time!"));
    }
}
    

