package apu.hostel.management;
import java.io.*;
import java.util.*;

import apu.hostel.management.APUHostelManagement.Manager;
import apu.hostel.management.APUHostelManagement.Resident;
import apu.hostel.management.APUHostelManagement.Staff;
import apu.hostel.management.APUHostelManagement.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class APUHostelManagement {
    // User abstract class
    public abstract static class User {
        protected String userID;
        protected String icPassportNumber;
        protected String username;
        protected String password;
        protected String contactNumber;
        protected String dateOfRegistration;
        protected String role;
        

        public User(String userID, String icPassportNumber, String username, String password, String contactNumber, String dateOfRegistration, String role) {
            this.userID = userID;
            this.icPassportNumber = icPassportNumber;
            this.username = username;
            this.password = password;
            this.contactNumber = contactNumber;
            this.dateOfRegistration = dateOfRegistration;
            this.role = role;
        }

        public String getUserID() {
            return userID;
        }

        public String getIcPassportNumber() {
            return icPassportNumber;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public String getDateOfRegistration() {
            return dateOfRegistration;
        }

        public String getRole() {
            return role;
        }

        public abstract void displayMenu();

        public void saveToFile(String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(userID + "," + icPassportNumber + "," + username + "," + password + "," + contactNumber + "," + dateOfRegistration + "," + role);
                writer.newLine();
            }
        }

        public static List<User> readFromFile(String filename) throws IOException {
            List<User> users = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        User user = null;
                        switch (parts[6]) {
                            case "Manager":
                                user = new Manager(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                                break;
                            case "Staff":
                                user = new Staff(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                                break;
                            case "Resident":
                                user = new Resident(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                                break;
                        }
                        if (user != null) {
                            users.add(user);
                        }
                    }
                }
            }
            return users;
        }
        

        public static User findUser(String username, String password, String filename) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7 && parts[2].equals(username) && parts[3].equals(password)) {
                        User user = null;
                        switch (parts[6]) {
                            case "Manager":
                                user = new Manager(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                                break;
                            case "Staff":
                                user = new Staff(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                                break;
                            case "Resident":
                                user = new Resident(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                                break;
                        }
                        return user;
                    }
                }
            }
            return null;
        }

        public static List<User> loadAllUsers() throws IOException {
            return readFromFile("users.txt");
        }

        // Method to check if IC/Passport Number, Username, or Contact Number is unique
        public static boolean isUnique(String icPassportNumber, String username, String contactNumber) throws IOException {
            List<User> users = new ArrayList<>();
            users.addAll(User.readFromFile("users.txt"));
            users.addAll(User.readFromFile("unapproved_residents.txt"));
            users.addAll(User.readFromFile("approved_residents.txt"));
            users.addAll(User.readFromFile("unapproved_staffs.txt"));
            users.addAll(User.readFromFile("approved_staffs.txt"));
            users.addAll(User.readFromFile("managers.txt"));

            for (User user : users) {
                if (user.getIcPassportNumber().equals(icPassportNumber) || user.getUsername().equals(username) || user.getContactNumber().equals(contactNumber)) {
                    return false;
                }
            }
            return true;
        }
    }

    // Manager class
    public static class Manager extends User {
        private String managerID;

        public Manager(String userID, String icPassportNumber, String username, String password, String contactNumber, String dateOfRegistration) {
            super(userID, icPassportNumber, username, password, contactNumber, dateOfRegistration, "Manager");
            this.managerID = "M" + userID.substring(1);
        }

        public String getManagerID() {
            return managerID;
        }

        @Override
        public void displayMenu() {
            // Manager-specific menu implementation
           
            System.out.println("Manager Menu:");
            System.out.println("1. Approve User Registration");
            System.out.println("2. Search User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Fix/Update Rate");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Approve User Registration logic
                    approveUserRegistration();
                    break;
                case 2:
                    // Search User logic
                    break;
                case 3:
                    // Update User logic
                    break;
                case 4:
                    // Delete User logic
                    break;
                case 5:
                    // Fix/Update Rate logic
                    break;
                case 6:
                    System.out.println("Logging out...");
                    displayWelcomePage();
                    
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    displayMenu(); // Recursively call to retry
                    break;
            }
        }

        private void approveUserRegistration() {
            try {
                List<User> unapprovedStaffs = User.readFromFile("unapproved_staffs.txt");
                List<User> unapprovedResidents = User.readFromFile("unapproved_residents.txt");

                if (unapprovedStaffs.isEmpty() && unapprovedResidents.isEmpty()) {
                    System.out.println("No users to approve.");
                    displayMenu();
                    return;
                }

                System.out.println("Unapproved Staffs:");
                for (int i = 0; i < unapprovedStaffs.size(); i++) {
                    User user = unapprovedStaffs.get(i);
                    System.out.println((i + 1) + ". " + user.getUsername() + " (" + user.getIcPassportNumber() + ")");
                }

                System.out.println("Unapproved Residents:");
                for (int i = 0; i < unapprovedResidents.size(); i++) {
                    User user = unapprovedResidents.get(i);
                    System.out.println((i + 1 + unapprovedStaffs.size()) + ". " + user.getUsername() + " (" + user.getIcPassportNumber() + ")");
                }

                System.out.print("Enter the number of the user to approve: ");
                Scanner scanner = new Scanner(System.in);
                int userIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // Consume newline

                if (userIndex >= 0 && userIndex < unapprovedStaffs.size()) {
                    Staff staffToApprove = (Staff) unapprovedStaffs.get(userIndex);
                    String userID = generateUserID("U");
                    staffToApprove.userID = userID;
                    staffToApprove.staffID = "S" + userID.substring(1);
                    staffToApprove.saveToFile("users.txt");
                    staffToApprove.saveToFile("approved_staffs.txt");
                    unapprovedStaffs.remove(userIndex);
                    saveUnapprovedUsers(unapprovedStaffs, "unapproved_staffs.txt");
                    System.out.println("Staff approved successfully.");
                } else if (userIndex >= unapprovedStaffs.size() && userIndex < unapprovedStaffs.size() + unapprovedResidents.size()) {
                    Resident residentToApprove = (Resident) unapprovedResidents.get(userIndex - unapprovedStaffs.size());
                    String userID = generateUserID("U");
                    residentToApprove.userID = userID;
                    residentToApprove.residentID = "R" + userID.substring(1);
                    residentToApprove.saveToFile("users.txt");
                    residentToApprove.saveToFile("approved_residents.txt");
                    unapprovedResidents.remove(userIndex - unapprovedStaffs.size());
                    saveUnapprovedUsers(unapprovedResidents, "unapproved_residents.txt");
                    System.out.println("Resident approved successfully.");
                } else {
                    System.out.println("Invalid user number.");
                }
                displayMenu();
            } catch (IOException e) {
                System.out.println("An error occurred while approving the user.");
                e.printStackTrace();
            }
        }

        // Method to save unapproved users to file
        private void saveUnapprovedUsers(List<User> users, String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    writer.write(user.getUserID() + "," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole());
                    writer.newLine();
                }
            }
        }
        

        public void searchUser(String username) {
            // Search user logic
        }

        public void updateUser(String username) {
            // Update user logic
        }

        public void deleteUser(String username) {
            // Delete user logic
        }

        public void fixOrUpdateRate(double rate) {
            // Fix or update rate logic
        }

        
    }

    // Staff class
    public static class Staff extends User {
        private String staffID;
        private String dateOfApproval;
        private boolean loggedIn;

        public Staff(String userID, String icPassportNumber, String username, String password, String contactNumber, String dateOfRegistration) {
            super(userID, icPassportNumber, username, password, contactNumber, dateOfRegistration, "Staff");
            this.loggedIn = true;
        }

        public String getStaffID() {
            return staffID;
        }

        public String getDateOfApproval() {
            return dateOfApproval;
        }

        public void setDateOfApproval(String dateOfApproval) {
            this.dateOfApproval = dateOfApproval;
        }

        public boolean isLoggedIn() {
            return loggedIn;
        }

        @Override
        public void displayMenu() {
            System.out.println("Staff Menu:");
            System.out.println("1. Update Individual Login Account");
            System.out.println("2. Make Payment for Resident");
            System.out.println("3. Generate Receipt");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
    
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1:
                    updatePersonalInformation();
                    
                    break;
                case 2:
                    // Make Payment for Resident logic
                    break;
                case 3:
                    // Generate Receipt logic
                    break;
                case 4:
                    System.out.println("Logging out...");
                    this.loggedIn = false;
                    System.out.println("You have been logged out successfully.");
                    displayWelcomePage();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        public void updatePersonalInformation() {
            Scanner scanner = new Scanner(System.in);
            int choice;
    
            do {
                System.out.println("Update Personal Information:");
                System.out.println("1. Update Username");
                System.out.println("2. Update Password");
                System.out.println("3. Update Contact Number");
                System.out.println("0. Go Back to Staff Menu");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                switch (choice) {
                    case 1:
                        System.out.print("Enter new username: ");
                        String newUsername = scanner.nextLine();
                        if (newUsername.isEmpty()) {
                            System.out.println("Username cannot be empty. Please try again.");
                            break;
                        }
                        try {
                            if (!User.isUnique("", newUsername, "")) {
                                System.out.println("Error: Username already exists.");
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                        this.username = newUsername;
                        System.out.println("Username updated successfully.");
                        break;
                    case 2:
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        if (newPassword.isEmpty()) {
                            System.out.println("Password cannot be empty. Please try again.");
                            break;
                        }
                        this.password = newPassword;
                        System.out.println("Password updated successfully.");
                        break;
                    case 3:
                        System.out.print("Enter new contact number: ");
                        String newContactNumber = scanner.nextLine();
                        if (newContactNumber.isEmpty()) {
                            System.out.println("Contact number cannot be empty. Please try again.");
                            break;
                        }
                        try {
                            if (!User.isUnique("", "", newContactNumber)) {
                                System.out.println("Error: Contact Number already exists.");
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                        this.contactNumber = newContactNumber;
                        System.out.println("Contact number updated successfully.");
                        break;
                    case 0:
                        System.out.println("Returning to Staff Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
    
                try {
                    updateFile("approved_staffs.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (choice != 0);
            displayMenu(); 
        }
    
        private void updateFile(String filename) throws IOException {
            List<User> users = User.readFromFile(filename);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    if (user.getUserID().equals(this.userID)) {
                        writer.write(this.userID + "," + this.icPassportNumber + "," + this.username + "," + this.password + "," + this.contactNumber + "," + this.dateOfRegistration + "," + this.role);
                    } else {
                        writer.write(user.getUserID() + "," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole());
                    }
                    writer.newLine();
                }
            }
        }

        public void makePayment(Resident resident, double amount) {
            // Make payment logic
        }

        public void generateReceipt(Resident resident, double amount) {
            // Generate receipt logic
        }
    }

    // Resident class
    public static class Resident extends User {
        private String residentID;
        private String dateOfApproval;
        private boolean loggedIn;

        public Resident(String userID, String icPassportNumber, String username, String password, String contactNumber, String dateOfRegistration) {
            super(userID, icPassportNumber, username, password, contactNumber, dateOfRegistration, "Resident");
            this.loggedIn = true;
        }

        public String getResidentID() {
            return residentID;
        }

        public String getDateOfApproval() {
            return dateOfApproval;
        }

        public void setDateOfApproval(String dateOfApproval) {
            this.dateOfApproval = dateOfApproval;
        }

        public boolean isLoggedIn() {
            return loggedIn;
        }

        @Override
        public void displayMenu() {
            // Resident-specific menu implementation
            System.out.println("Resident Menu:");
            System.out.println("1. Update Individual Information");
            System.out.println("2. View Payment Records");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    updatePersonalInformation();
                    break;
                case 2:
                    viewPaymentRecords();
                    break;
                case 3:
                    residentLogout();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
        }

        public void updatePersonalInformation() {
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Update Personal Information:");
                System.out.println("1. Update Username");
                System.out.println("2. Update Password");
                System.out.println("3. Update Contact Number");
                System.out.println("0. Go Back to Resident Menu");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter new username: ");
                        String newUsername = scanner.nextLine();
                        if (newUsername.isEmpty()) {
                            System.out.println("Username cannot be empty. Please try again.");
                            break;
                        }
                        try {
                            if (!User.isUnique("", newUsername, "")) {
                                System.out.println("Error: Username already exists.");
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                        this.username = newUsername;
                        System.out.println("Username updated successfully.");
                        break;
                    case 2:
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        if (newPassword.isEmpty()) {
                            System.out.println("Password cannot be empty. Please try again.");
                            break;
                        }
                        this.password = newPassword;
                        System.out.println("Password updated successfully.");
                        break;
                    case 3:
                        System.out.print("Enter new contact number: ");
                        String newContactNumber = scanner.nextLine();
                        if (newContactNumber.isEmpty()) {
                            System.out.println("Contact number cannot be empty. Please try again.");
                            break;
                        }
                        try {
                            if (!User.isUnique("", "", newContactNumber)) {
                                System.out.println("Error: Contact Number already exists.");
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                        this.contactNumber = newContactNumber;
                        System.out.println("Contact number updated successfully.");
                        break;
                    case 0:
                        System.out.println("Returning to Resident Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                try {
                    updateFile("approved_residents.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (choice != 0);
            displayMenu();
        }

        private void updateFile(String filename) throws IOException {
            List<User> users = User.readFromFile(filename);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    if (user.getUserID().equals(this.userID)) {
                        writer.write(this.userID + "," + this.icPassportNumber + "," + this.username + "," + this.password + "," + this.contactNumber + "," + this.dateOfRegistration + "," + this.role);
                    } else {
                        writer.write(user.getUserID() + "," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole());
                    }
                    writer.newLine();
                }
            }
        }
    

        public void viewPaymentRecords() {
            System.out.println("Payment Records:");
            String userID = this.getUserID(); // Assuming there's a method to get the current user's ID

            try (BufferedReader br = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                boolean hasRecords = false;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split(", ");
                    if (details[0].equals(userID)) {
                        hasRecords = true;
                        System.out.println("Username: " + details[1]);
                        System.out.println("Payment Amount: " + details[2]);
                        System.out.println("Payment Date: " + details[3]);
                        System.out.println("Room Number: " + details[4]);
                        System.out.println("Receipt Number: " + details[5]);
                        System.out.println("-----------------------------");
                    }
                }
                if (!hasRecords) {
                    System.out.println("No payment records found for your account.");
                    displayMenu();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the payment records.");
                e.printStackTrace();
            }
        }

        public void residentLogout() {
            System.out.println("Logging out...");
            // Perform any necessary cleanup, such as closing resources or saving state
            // For example:
            // closeDatabaseConnection();
            // saveUserSession();

            // Mark the user as logged out
            this.loggedIn = false;

            System.out.println("You have been logged out successfully.");
            // Route back to the main menu
            displayWelcomePage();
        }
    }

    // Payment class
    public static class Payment {
        private String paymentID;
        private String residentID;
        private String staffID;
        private double amount;
        private String paymentDate;
        private String receiptNumber;
        private String roomNumber;
        private String paymentMethod;

        public Payment(String paymentID, String residentID, String staffID, double amount, String paymentDate, String receiptNumber, String roomNumber, String paymentMethod) {
            this.paymentID = paymentID;
            this.residentID = residentID;
            this.staffID = staffID;
            this.amount = amount;
            this.paymentDate = paymentDate;
            this.receiptNumber = receiptNumber;
            this.roomNumber = roomNumber;
            this.paymentMethod = paymentMethod;
        }

        public String getPaymentID() {
            return paymentID;
        }

        public String getResidentID() {
            return residentID;
        }

        public String getStaffID() {
            return staffID;
        }

        public double getAmount() {
            return amount;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public String getReceiptNumber() {
            return receiptNumber;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void saveToFile(String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(paymentID + "," + residentID + "," + staffID + "," + amount + "," + paymentDate + "," + receiptNumber + "," + roomNumber + "," + paymentMethod);
                writer.newLine();
            }
        }

        public static List<Payment> readFromFile(String filename) throws IOException {
            List<Payment> payments = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 8) {
                        Payment payment = new Payment(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4], parts[5], parts[6], parts[7]);
                        payments.add(payment);
                    }
                }
            }
            return payments;
        }
    }

    // FeeRate class
    public static class FeeRate {
        private String feeRateID;
        private String roomType;
        private double monthlyRate;
        private String effectiveDate;

        public FeeRate(String feeRateID, String roomType, double monthlyRate, String effectiveDate) {
            this.feeRateID = feeRateID;
            this.roomType = roomType;
            this.monthlyRate = monthlyRate;
            this.effectiveDate = effectiveDate;
        }

        public String getFeeRateID() {
            return feeRateID;
        }

        public String getRoomType() {
            return roomType;
        }

        public double getMonthlyRate() {
            return monthlyRate;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public void saveToFile(String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(feeRateID + "," + roomType + "," + monthlyRate + "," + effectiveDate);
                writer.newLine();
            }
        }

        public static List<FeeRate> readFromFile(String filename) throws IOException {
            List<FeeRate> feeRates = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        FeeRate feeRate = new FeeRate(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]);
                        feeRates.add(feeRate);
                    }
                }
            }
            return feeRates;
        }
    }



    // Method to display the welcome page
    public static void displayWelcomePage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to APU Hostel Management Fees Payment System (AHMFPS)");
        System.out.println("Please choose your role:");
        System.out.println("1. Manager");
        System.out.println("2. Staff");
        System.out.println("3. Resident");
        System.out.print("Enter your choice (1-3): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.println("You have chosen Manager.");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.print("Enter your choice (1-2): ");
                int managerChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (managerChoice == 1) {
                    registerManager();
                } else if (managerChoice == 2) {
                    loginManager();
                } else {
                    System.out.println("Invalid choice. Please try again.");
                    displayWelcomePage(); // Recursively call to retry
                }
                break;
            case 2:
                System.out.println("You have chosen Staff.");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.print("Enter your choice (1-2): ");
                int staffChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (staffChoice == 1) {
                    registerStaff();
                } else if (staffChoice == 2) {
                    loginStaff();
                } else {
                    System.out.println("Invalid choice. Please try again.");
                    displayWelcomePage(); // Recursively call to retry
                }
                    break;
            case 3:
                System.out.println("You have chosen Resident.");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.print("Enter your choice (1-2): ");
                int residentChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (residentChoice == 1) {
                    registerResident();
                } else if (residentChoice == 2) {
                    loginResident();
                } else {
                    System.out.println("Invalid choice. Please try again.");
                    displayWelcomePage(); // Recursively call to retry
                }
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                displayWelcomePage(); // Recursively call to retry
                break;
        }
            
    }

    // Method to handle Manager registration
    public static void registerManager() {
        Scanner scanner = new Scanner(System.in);
        String icPassportNumber, username, password, contactNumber;

        while (true) {
            System.out.print("Enter IC/Passport Number: ");
            icPassportNumber = scanner.nextLine();
            if (icPassportNumber.isEmpty()) {
                System.out.println("IC/Passport Number cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique(icPassportNumber, "", "")) {
                    System.out.println("Error: IC/Passport Number already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique("", username, "")) {
                    System.out.println("Error: Username already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter contact number: ");
            contactNumber = scanner.nextLine();
            if (contactNumber.isEmpty()) {
                System.out.println("Contact number cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique("", "", contactNumber)) {
                    System.out.println("Error: Contact Number already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        try {
            String dateOfRegistration = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String userID = generateUserID("U");
            User manager = new Manager(userID, icPassportNumber, username, password, contactNumber, dateOfRegistration);
            manager.saveToFile("managers.txt");
            System.out.println("Manager registered successfully.");
            displayWelcomePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle Manager login
    public static void loginManager() {
        Scanner scanner = new Scanner(System.in);
        String username, password;

        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        try {
            User user = User.findUser(username, password, "managers.txt");
            if (user != null && user.getRole().equals("Manager")) {
                System.out.println("Login successful.");
                user.displayMenu();
            } else {
                System.out.println("Invalid username or password.");
                System.out.print("Do you want to retry? (yes/no): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("yes")) {
                    loginManager(); // Retry login
                } else {
                    System.out.println("Exiting login process.");
                    displayWelcomePage();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle Staff registration
    public static void registerStaff() {
        Scanner scanner = new Scanner(System.in);
        String icPassportNumber, username, password, contactNumber;

        while (true) {
            System.out.print("Enter IC/Passport Number: ");
            icPassportNumber = scanner.nextLine();
            if (icPassportNumber.isEmpty()) {
                System.out.println("IC/Passport Number cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique(icPassportNumber, "", "")) {
                    System.out.println("Error: IC/Passport Number already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique("", username, "")) {
                    System.out.println("Error: Username already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter contact number: ");
            contactNumber = scanner.nextLine();
            if (contactNumber.isEmpty()) {
                System.out.println("Contact number cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique("", "", contactNumber)) {
                    System.out.println("Error: Contact Number already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        try {
            String dateOfRegistration = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Staff staff = new Staff(null, icPassportNumber, username, password, contactNumber, dateOfRegistration);
            staff.saveToFile("unapproved_staffs.txt");
            System.out.println("Staff registered successfully.");
            displayWelcomePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle Staff login
    public static void loginStaff() {
        Scanner scanner = new Scanner(System.in);
        String username, password;

        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        try {
            User user = User.findUser(username, password, "approved_staffs.txt");
            if (user != null && user instanceof Staff) {
                System.out.println("Login successful.");
                user.displayMenu();
            } else {
                System.out.println("Invalid username or password.");
                System.out.print("Do you want to retry? (yes/no): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("yes")) {
                    loginStaff(); // Retry login
                } else {
                    System.out.println("Exiting login process.");
                    displayWelcomePage();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle Resident registration
    public static void registerResident() {
        Scanner scanner = new Scanner(System.in);
        String icPassportNumber, username, password, contactNumber;

        while (true) {
            System.out.print("Enter IC/Passport Number: ");
            icPassportNumber = scanner.nextLine();
            if (icPassportNumber.isEmpty()) {
                System.out.println("IC/Passport Number cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique(icPassportNumber, "", "")) {
                    System.out.println("Error: IC/Passport Number already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique("", username, "")) {
                    System.out.println("Error: Username already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter contact number: ");
            contactNumber = scanner.nextLine();
            if (contactNumber.isEmpty()) {
                System.out.println("Contact number cannot be empty. Please try again.");
                continue;
            }
            try {
                if (!User.isUnique("", "", contactNumber)) {
                    System.out.println("Error: Contact Number already exists.");
                    System.out.print("Do you want to try again? (yes/no): ");
                    if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                        displayWelcomePage();
                        return;
                    }
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
        }

        try {
            String dateOfRegistration = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Resident resident = new Resident(null, icPassportNumber, username, password, contactNumber, dateOfRegistration);
            resident.saveToFile("unapproved_residents.txt");
            System.out.println("Resident registered successfully.");
            displayWelcomePage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle Resident login
    public static void loginResident() {
        Scanner scanner = new Scanner(System.in);
        String username, password;

        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
                continue;
            }
            break;
        }

        try {
            User user = User.findUser(username, password, "approved_residents.txt");
            if (user != null && user instanceof Resident) {
                System.out.println("Login successful.");
                user.displayMenu();
            } else {
                System.out.println("Invalid username or password.");
                System.out.print("Do you want to retry? (yes/no): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("yes")) {
                    loginResident(); // Retry login
                } else {
                    System.out.println("Exiting login process.");
                    displayWelcomePage();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to generate unique IDs with a prefix
    private static String generateUserID(String prefix) {
        int id = 1;
        String filename = "users.txt";
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].startsWith(prefix)) {
                    int currentId = Integer.parseInt(parts[0].substring(1));
                    if (currentId >= id) {
                        id = currentId + 1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prefix + String.format("%02d", id);
    }

        // Main method to launch the application
    public static void main(String[] args) {
        displayWelcomePage();
    }
}

