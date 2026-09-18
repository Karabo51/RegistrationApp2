/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.registrationapp;
import java.util.regex.Pattern;
/**
 *
 * @author Student
 */
public class Login {
        private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // --- Getters ---
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCellPhoneNumber() { return cellPhoneNumber; }

    /**
     * Checks that the username contains an underscore and is
     * no more than five characters long.
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks that the password meets complexity rules:
     * at least 8 characters, a capital letter, a number, and a special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        boolean longEnough = password.length() >= 8;
        boolean hasCapital = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecialChar = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();

        return longEnough && hasCapital && hasNumber && hasSpecialChar;
    }

    /**
     * Checks that the cell phone number contains the South African international
     * country code (+27) followed by a number that is no more than ten digits long.
     * Example of a valid number: +27821234567
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        String regex = "^\\+27[0-9]{1,10}$";
        return Pattern.matches(regex, cellPhoneNumber);
    }

    /**
     * Registers the user, returning the appropriate message depending on
     * whether the username, password, and cell phone number are valid.
     */
    public String registerUser() {
        boolean usernameOk = checkUserName(this.username);
        boolean passwordOk = checkPasswordComplexity(this.password);
        boolean cellOk = checkCellPhoneNumber(this.cellPhoneNumber);

        StringBuilder result = new StringBuilder();

        if (!usernameOk) {
            result.append("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
        } else {
            result.append("Username successfully captured.");
        }

        result.append("\n");

        if (!passwordOk) {
            result.append("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        } else {
            result.append("Password successfully captured.");
        }

        result.append("\n");

        if (!cellOk) {
            result.append("Cell phone number incorrectly formatted or does not contain international code.");
        } else {
            result.append("Cell phone number successfully added.");
        }

        if (usernameOk && passwordOk && cellOk) {
            result.append("\nUser ").append(firstName).append(" ").append(lastName).append(" has been registered successfully!");
        }

        return result.toString();
    }

    /**
     * Verifies that the entered username and password match the stored
     * registration details.
     */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            return false;
        }
        return enteredUsername.equals(this.username) && enteredPassword.equals(this.password);
    }

    /**
     * Returns the appropriate message depending on whether the login was
     * successful.
     */
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}

