package com.pluralsight;
//import statements
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainApp {
    //Name of file where transactions are stored
    static final String fileName = "transactions (1).csv";
    //Scanner for user input(more than one Scanner is used(put above main)
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //to loop the user back when the user hits EXIT
        while (true) {
            showMenu();
            String choice = scanner.nextLine().toUpperCase();//Read and make the input uppercase
            //Keeps track of user choice in Menu
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
                    return;//Exit program
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Display main menu options
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
        //user adds deposit or payment transaction
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());


            if (type.equalsIgnoreCase("Payment") && amount > 0) {//if it's a payment store it as a negative.
                amount = -Math.abs(amount); // Payments are negative
            }

            // Get current date and time
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            // date|time|description|vendor|amount to format
            String line = String.format("%s|%s|%s|%s|%.2f", date, time, description, vendor, amount);
            //writes to the file
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
        //show all transaction from file in the table format/saves
        System.out.println("\n=== Ledger ===");

        try {
            File file = new File(fileName);//open the transaction file
            if (!file.exists()) {
                System.out.println("No transactions found.");
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;

            // Print header \ -(minus) keeps it left-aligned, %n stays in its line
            System.out.printf("%-12s %-10s %-20s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("----------------------------------------------------------------------------");
            //display and read each line
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
        //shows menu for ledger options
        while (true) {
            System.out.println("\n=== Ledger Menu ===");
            System.out.println("(E) All Entries");
            System.out.println("(D) Deposits");
            System.out.println("(P) Payments");
            System.out.println("(R) Reports");
            System.out.println("(X) Exit to Main");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "E":
                    showTransactions(readTransactions(), "All Entries");
                    break;
                case "D":
                    showTransactions(onlyDeposits(), "Deposits");
                    break;
                case "P":
                    showTransactions(onlyPayments(), "Payments");
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
        //show report options
        while (true) {
            System.out.println("\n=== Reports ===");
            System.out.println("(1) Month To Date");
            System.out.println("(2) Previous Month");
            System.out.println("(3) Year To Date");
            System.out.println("(4) Previous Year");
            System.out.println("(5) Search by Vendor");
            System.out.println("(6) Custom Search");
            System.out.println("(B) Back to Ledger Menu");
            System.out.print("Choose a report: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showTransactions(byCurrentMonth(), "Month To Date");
                    break;
                case "2":
                    showTransactions(byPreviousMonth(), "Previous Month");
                    break;
                case "3":
                    showTransactions(byCurrentYear(), "Year To Date");
                    break;
                case "4":
                    showTransactions(byPreviousYear(), "Previous Year");
                    break;
                case "5":
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine();
                    showTransactions(byVendor(vendor), "Vendor: " + vendor);
                    break;
                case "6":
                    showTransactions(byCustomSearch(), "Custom Search");
                    break;
                case "B":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static ArrayList<Transaction> readTransactions() {//Read all transaction to file in a list
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().startsWith("date|")) {
                    continue; // Skip header line
                }
                Transaction t = new Transaction(line);//create transaction object
                transactions.add(t);
            }
            br.close();

            // Sort by newest first
            for (int i = 0; i < transactions.size() - 1; i++) {//outer loop(goes through every item, except last one) i is the current position to be replaced as the new one
                for (int e = i + 1; e < transactions.size(); e++) {//inner loop(compares to i with every transaction basically to replace i
                    Transaction transaction1 = transactions.get(i);//position of i
                    Transaction transaction2 = transactions.get(e);//position of j
                    if (transaction1.date.isBefore(transaction2.date) ||//checks if t2 is newer than t1, compare the dates by who is early (date and time)
                            (transaction1.date.equals(transaction2.date) && transaction1.time.isBefore(transaction2.time))) {
                        transactions.set(i, transaction2);
                        transactions.set(e, transaction1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading transactions.");
        }
        return transactions;
    }

    static ArrayList<Transaction> onlyDeposits() {//shows only positive deposits amount
        ArrayList<Transaction> all = readTransactions();//lists all transactions loaded in the file
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction transaction : all) {//go through each transaction inside all list one by one, calls each transaction, checks to see transaction matches user's filters(date, amount, vendor)
            if (transaction.amount >= 0) {
                deposits.add(transaction);
            }
        }
        return deposits;
    }

    static ArrayList<Transaction> onlyPayments() {//shows only payments with negative amount
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
        LocalDate currentMonth = LocalDate.now();
        int month = currentMonth.getMonthValue();
        int year = currentMonth.getYear();
        ArrayList<Transaction> all = readTransactions();

        for (Transaction transaction : all) {
            if (transaction.date.getMonthValue() == month && transaction.date.getYear() == year) {
                result.add(transaction);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byPreviousMonth() {
        ArrayList<Transaction> result = new ArrayList<>();
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int month = lastMonth.getMonthValue();
        int year = lastMonth.getYear();
        ArrayList<Transaction> all = readTransactions();

        for (Transaction transaction : all) {
            if (transaction.date.getMonthValue() == month && transaction.date.getYear() == year) {
                result.add(transaction);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byCurrentYear() {
        ArrayList<Transaction> result = new ArrayList<>();
        int year = LocalDate.now().getYear();
        ArrayList<Transaction> all = readTransactions();

        for (Transaction transaction : all) {
            if (transaction.date.getYear() == year) {
                result.add(transaction);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byPreviousYear() {
        ArrayList<Transaction> result = new ArrayList<>();
        int year = LocalDate.now().getYear() - 1;
        ArrayList<Transaction> all = readTransactions();

        for (Transaction transaction : all) {
            if (transaction.date.getYear() == year) {
                result.add(transaction);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byVendor(String vendor) {
        ArrayList<Transaction> all = readTransactions();
        ArrayList<Transaction> result = new ArrayList<>();

        for (Transaction transaction : all) {
            if (transaction.vendor.equalsIgnoreCase(vendor)) {
                result.add(transaction);
            }
        }
        return result;
    }

    static ArrayList<Transaction> byCustomSearch() {
        ArrayList<Transaction> all = readTransactions();
        ArrayList<Transaction> result = new ArrayList<>();

        System.out.println("\n=== Custom Search ===");

        System.out.print("Provide start date(yyyy-MM-dd) or leave blank: ");
        String startDateSearch = scanner.nextLine().trim();
        LocalDate startDate = null;
        if (!startDateSearch.isEmpty()) {
            startDate = LocalDate.parse(startDateSearch);
        }

        System.out.print("Provide end date(yyyy-MM-dd) or leave blank: ");
        String endDateSearch = scanner.nextLine().trim();
        LocalDate endDate = null;
        if (!endDateSearch.isEmpty()) {
            endDate = LocalDate.parse(endDateSearch);
        }


        System.out.print("Enter Description / leave blank: ");
        String descriptionSearch = scanner.nextLine().trim();

        System.out.print("Enter Vendor / leave blank: ");
        String vendorSearch = scanner.nextLine().trim();

        System.out.print("Enter Amount / leave blank: ");
        String amount = scanner.nextLine().trim();
        Double amountValue = null;
        if (!amount.isEmpty()) {
            amountValue = Double.parseDouble(amount);
        }

        for (Transaction transaction : all) {//for-each loop for every transaction called all
            boolean same = true;//starts true, if no match in date, vendor or etc it goes to false

            if (startDate != null && transaction.date.isBefore(startDate)) {
                same = false;
            }

            if (endDate != null && transaction.date.isAfter(endDate)) {
                same = false;
            }
            //.contains helps without typing a whole description to match
            if (!descriptionSearch.isEmpty() && !transaction.description.toLowerCase().contains(descriptionSearch.toLowerCase())) {
                same = false;
            }

            if (!vendorSearch.isEmpty() && !transaction.vendor.toLowerCase().contains(vendorSearch.toLowerCase())) {
                same = false;
            }

            if (amountValue != null && transaction.amount != amountValue) {
                same = false;
            }

            if (same) {
                result.add(transaction);
            }
        }

        return result;
    }

    static void showTransactions(ArrayList<Transaction> list, String title) {
        System.out.println("\n=== " + title + " ===");
        if (list.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.printf("%-12s %-10s %-20s %-20s %10s\n", "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction transaction : list) {
            System.out.printf("%-12s %-10s %-20s %-20s %10.2f\n",
                    transaction.date.toString(),
                    transaction.time.toString(),
                    transaction.description,
                    transaction.vendor,
                    transaction.amount);
        }
    }

    static class Transaction {//constructor
        LocalDate date;
        LocalTime time;
        String description;
        String vendor;
        double amount;

        public Transaction(String line) {
            String[] parts = line.split("\\|");
            if (parts.length != 5) {
                throw new IllegalArgumentException("Invalid transaction format");
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            this.date = LocalDate.parse(parts[0], dateFormatter);
            this.time = LocalTime.parse(parts[1], timeFormatter);
            this.description = parts[2];
            this.vendor = parts[3];
            this.amount = Double.parseDouble(parts[4]);
        }
    }
}