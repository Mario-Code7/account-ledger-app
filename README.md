# Account Ledger App

This is a **ledger application** built in Java that helps you **track deposits and payments**. All transactions are saved to a `.csv` file and can be filtered/viewed in different ways.

---

## Files Included
- `mainApp.java`: The main Java program that runs the ledger.
- `transactions (1).csv`: The file where all transactions are saved.

---

## Features
- Add deposits and payments.
- View all transactions.
- Filter by:
  - Deposits only
  - Payments only
  - Time ranges (current month, previous year, etc.)
  - Vendor name
  - Custom search (date, vendor, description, amount)

---

## Main App Menu
- (A) Add Deposit
- (B) Make Payment
- (C) Show Ledger
- (L) Move forward to Ledger Screen
- (X) Exit

## Ledger Menu
- (E) All Entries
- (D) Deposits
- (P) Payments
- (R) Reports
- (X) Exit to Main

## Reports Menu
- (1) Month to Date
- (2) Previous Month
- (3) Year to Date
- (4) Previous Year
- (5) Search by Vendor
- (6) Custom Search
- (B) Back to Ledger Menu

## Transaction Format
YYYY-MM-DD|HH:MM:SS|Description|Vendor|Amount

---

## How to Run

1. Make sure you have **Java installed** (JDK 8 or higher).
2. Have Intellij or similar IDE
3. Open a terminal/command prompt.
4. Hit the RUN button(green sideways triangle)
5. Compile the program:

---

## Run the program
java com.pluralsight.mainApp
