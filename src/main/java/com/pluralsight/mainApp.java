package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    static void showLedger() {
        System.out.println("\n=== Ledger ===");

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("No transactions found.");
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;

            // Print header \ -(minus) keeps it left-aligned, %n stays in its line
            System.out.printf("%-12s %-10s %-20s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("----------------------------------------------------------------------------");

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|", 5); // split by pipe
                if (parts.length == 5) {
                    System.out.printf("%-12s %-10s %-20s %-20s $%-9s%n", parts[0], parts[1], parts[2], parts[3], parts[4]);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Error reading ledger.");
        }
    }

    static void showLedgerMenu() {
        while (true) {
            System.out.println("\n=== Ledger Menu ===");
            System.out.println("(E) All Entries");
            System.out.println("(D) Deposits");
            System.out.println("(P) Payments");
            System.out.println("(R) Reports");
            System.out.println("(X) Exit to Home");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();
            switch (choice) {
                case "E":
                    showTransactions(readTransactions(),"All Entries");
                    break;
                case "D":
                    showTransactions(onlyDeposits), "Deposits";
                    break;
                case "P":
                    showTransactions(onlyPayments), "Payment";
                    break;
                case "R":
                    showReportsMenu();
                    break;
                case "X":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void showReportsMenu() {
        while (true) {
            System.out.println("\n=== Reports ===");
            System.out.println("(1) Month To Date");
            System.out.println("(2) Previous Month");
            System.out.println("(3) Year To Date");
            System.out.println("(4) Previous Year");
            System.out.println("(5) Search by Vendor");
            System.out.println("(B) Back to Ledger");
            System.out.print("Choose a report: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showTransactions(byCurrentMonth), "Month to Date";
                    break;
                case "2":
                    showTransactions(byPreviousMonth), "Previous Month";
                    break;
                case "3":
                    showTransactions(byYearDate), "Year to Date";
                    break;
                case "4":
                    showTransactions(byPreviousYear), "Previous Year";
                    break;
                case "5":
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine();
                    showTransactions(byVendor(vendor)) = "vendor: " + vendor;
                    break;
                case "B":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    static ArrayList<Transaction> readTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().startsWith("date|")) {
                    continue; // Skip header line
                }
                Transaction transaction = new Transaction(line);
                transactions.add(transaction);
            }
            br.close();

            // Sort by newest, first check if the first t1 is different from t2 to change to new
            for (int i = 0; i < transactions.size() - 1; i++) {
                for (int j = i + 1; j < transactions.size(); j++) {
                    Transaction transaction1 = transactions.get(i);
                    Transaction transaction2 = transactions.get(j);
                    if (transaction1.date.isBefore(transaction2.date) ||
                            (transaction1.date.equals(transaction2.date) && transaction1.time.isBefore(transaction2.time))) {
                        transactions.set(i, transaction2);
                        transactions.set(j, transaction1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading transactions.");
        }
        return transactions;
    }
        static ArrayList<Transaction> onlyDeposits() {
        ArrayList<Transaction> all = readTransactions();
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction transaction : all) {
            if (transaction.amount >= 0) {
                deposits.add(transaction);
            }
        }
        return deposits;
    }

    static ArrayList<Transaction> onlyPayments() {
        ArrayList<Transaction> all = readTransactions();
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction transaction : all) {
            if (transaction.amount < 0) {
                payments.add(transaction);
            }
        }
        return payments;
    }

    static ArrayList<Transaction> byCurrentMonth() {
        ArrayList<Transaction> result = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int m = now.getMonthValue();
        int y = now.getYear();
        ArrayList<Transaction> all = readTransactions();

        for (Transaction t : all) {
            if (t.date.getMonthValue() == m && t.date.getYear() == y) {
                result.add(t);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byPreviousMonth() {
        ArrayList<Transaction> result = new ArrayList<>();
        LocalDate now = LocalDate.now().minusMonths(1);
        int m = now.getMonthValue();
        int y = now.getYear();
        ArrayList<Transaction> all = readTransactions();

        for (Transaction transaction : all) {
            if (transaction.date.getMonthValue() == m && transaction.date.getYear() == y) {
                result.add(transaction);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byCurrentYear() {
        ArrayList<Transaction> result = new ArrayList<>();
        int y = LocalDate.now().getYear();
        ArrayList<Transaction> all = readTransactions();

        for (Transaction transaction : all) {
            if (transaction.date.getYear() == y) {
                result.add(transaction);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byPreviousYear() {
        ArrayList<Transaction> result = new ArrayList<>();
        int y = LocalDate.now().getYear() - 1;
        ArrayList<Transaction> all = readTransactions();

        for (Transaction transaction : all) {
            if (transaction.date.getYear() == y) {
                result.add(transaction);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byVendor(String vendor) {
        ArrayList<Transaction> result = new ArrayList<>();
        ArrayList<Transaction> all = readTransactions();

        for (Transaction transaction : all) {
            if (transaction.vendor.equalsIgnoreCase(vendor)) {
                result.add(transaction);
            }
        }
        return result;
    }
}