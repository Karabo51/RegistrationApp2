/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.registrationapp;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class LoginIT {
    
     // A Login instance used purely to call the validation methods directly.
    Login login = new Login("Kyle", "Smith", "kyl_1", "Ver_2025#", "+27821234567");

    // --- Username tests ---

    @Test
    public void testUserName_CorrectlyFormatted() {
        boolean expected = true;
        boolean actual = login.checkUserName("kyl_1");
        assertEquals(expected, actual);
    }

    @Test
    public void testUserName_IncorrectlyFormatted() {
        boolean expected = false;
        boolean actual = login.checkUserName("kyle!!!!!!");
        assertEquals(expected, actual);
    }

    // --- Password tests ---

    @Test
    public void testPassword_MeetsComplexityRequirements() {
        boolean expected = true;
        boolean actual = login.checkPasswordComplexity("Ver_2025#");
        assertEquals(expected, actual);
    }

    @Test
    public void testPassword_DoesNotMeetComplexityRequirements() {
        boolean expected = false;
        boolean actual = login.checkPasswordComplexity("password");
        assertEquals(expected, actual);
    }

    // --- Cell phone number tests ---

    @Test
    public void testCellPhoneNumber_CorrectlyFormatted() {
        boolean expected = true;
        boolean actual = login.checkCellPhoneNumber("+27821234567");
        assertEquals(expected, actual);
    }

    @Test
    public void testCellPhoneNumber_IncorrectlyFormatted() {
        boolean expected = false;
        boolean actual = login.checkCellPhoneNumber("0821234567");
        assertEquals(expected, actual);
    }

    // --- Login / welcome message tests ---

    @Test
    public void testLogin_Successful_ReturnsWelcomeMessage() {
        String expected = "Welcome Kyle, Smith it is great to see you again.";
        boolean loginSuccess = login.loginUser("kyl_1", "Ver_2025#");
        String actual = login.returnLoginStatus(loginSuccess);
        assertEquals(expected, actual);
    }

    @Test
    public void testLogin_Unsuccessful_ReturnsErrorMessage() {
        String expected = "Username or password incorrect, please try again.";
        boolean loginSuccess = login.loginUser("wrong_user", "wrongPass1#");
        String actual = login.returnLoginStatus(loginSuccess);
        assertEquals(expected, actual);
    }
}