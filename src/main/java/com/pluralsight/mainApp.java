package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class mainApp {
    static final String fileName = "transactions.csv";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            showMenu();
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    addTransaction("Deposit");
                    break;
                case "B":
                    addTransaction("Payment");
                    break;
                case "C":
                    showLedger();
                    break;
                case "L":
                    showLedgerMenu();
                    break;
                case "X":
                    System.out.println("Application closed. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void showMenu() {
        System.out.println("\n=== Home Screen ===");
        System.out.println("(A) To Add Deposit");
        System.out.println("(B) To Make a Payment (Debit)");
        System.out.println("(C) Show Ledger");
        System.out.println("(L) Move forward to Ledger Screen");
        System.out.println("(X) Exit");
        System.out.print("Enter your choice here: ");
    }

    static void addTransaction(String type) {
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());


            if (type.equalsIgnoreCase("Payment") && amount > 0) {
                amount = -amount; // Payments are negative
            }

            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            // date|time|description|vendor|amount
            String line = String.format("%s|%s|%s|%s|%.2f", date, time, description, vendor, amount);

            FileWriter fileWriter = new FileWriter(fileName, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(line);
            printWriter.close();

            System.out.println(type + " saved.");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

}