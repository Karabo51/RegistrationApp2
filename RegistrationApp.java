/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrationapp;
import java.util.Scanner;
/**
 * Part 1 registration/login, then Part 2 QuickChat once logged in.
 *
 * @author Student
 */
public class RegistrationApp {

       public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== User Registration ===");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter a username (must contain an underscore and be no more than 5 characters): ");
        String username = scanner.nextLine();

        System.out.print("Enter a password (at least 8 characters, a capital letter, a number, and a special character): ");
        String password = scanner.nextLine();

        System.out.print("Enter your cell phone number (e.g. +27821234567): ");
        String cellPhoneNumber = scanner.nextLine();

        Login login = new Login(firstName, lastName, username, password, cellPhoneNumber);

        System.out.println("\n" + login.registerUser());

        boolean usernameOk = login.checkUserName(username);
        boolean passwordOk = login.checkPasswordComplexity(password);
        boolean cellOk = login.checkCellPhoneNumber(cellPhoneNumber);

        if (usernameOk && passwordOk && cellOk) {
            System.out.println("\n=== User Login ===");

            System.out.print("Enter your username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();

            boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus(loginSuccess));

            // Part 2: messaging is only available after a successful login.
            if (loginSuccess) {
                QuickChat.run(scanner);
            }
        }

        scanner.close();
    }
}
    

