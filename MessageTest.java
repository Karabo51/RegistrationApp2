/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationapp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Message class (Part 2).
 *
 * @author Student
 */
public class MessageTest {
    // Test data from the assignment (fixed ID so the hash is predictable).
    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        Message.setStoreFile("target/test_storedMessages.json");
        Message.resetSentMessages();
        message1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        message2 = new Message("0098765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    // --- Message length ---
    @Test
    public void testMessageLength_Success() {
        assertEquals("Message ready to send.", Message.checkMessageLength("a".repeat(250)));
    }

    @Test
    public void testMessageLength_Failure() {
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength("a".repeat(260)));
    }

    // --- Recipient number ---
    @Test
    public void testRecipient_Success() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipient_Failure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    // --- Message hash ---
    @Test
    public void testMessageHash_TestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    public void testMessageHash_AllMessagesInLoop() {
        Message[] messages = {message1, message2};
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].getMessageHash());
        }
    }

    // --- Message ID ---
    @Test
    public void testMessageID_Created() {
        Message generated = new Message(0, "+27718693002", "Hello there");
        assertEquals("Message ID generated: " + generated.getMessageID(),
                generated.getMessageIDStatement());
        assertEquals(10, generated.getMessageID().length());
    }

    @Test
    public void testCheckMessageID_True() {
        assertTrue(message1.checkMessageID());
    }

    @Test
    public void testCheckMessageID_False() {
        Message tooLong = new Message("12345678901", 0, "+27718693002", "Hello there");
        assertFalse(tooLong.checkMessageID());
    }

    // --- SentMessage options ---
    @Test
    public void testSentMessage_Send() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    // --- Total messages ---
    @Test
    public void testReturnTotalMessages() {
        message1.sentMessage(1);   // sent
        message2.sentMessage(2);   // disregarded, not counted
        assertEquals(1, Message.returnTotalMessages());
    }
}

