package apu.hostel.management;
import java.io.*;
import java.util.*;


public class APUHostelManagement {
    // User abstract class
    public abstract static class User {
        protected String username;
        protected String password;
        protected String role;
        protected boolean approved;
        

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.approved = false;
            
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }

        public boolean isApproved() {
            return approved;
        }

        public void setApproved(boolean approved) {
            this.approved = approved;
        }

        public abstract void displayMenu();

        public void saveToFile(String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(username + "," + password + "," + role + "," + approved);
                writer.newLine();
            }
        }

        public static List<User> readFromFile(String filename) throws IOException {
            List<User> users = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        User user = null;
                        switch (parts[2]) {
                            case "Manager":
                                user = new Manager(parts[0], parts[1]);
                                break;
                            case "Staff":
                                user = new Staff(parts[0], parts[1]);
                                break;
                            case "Resident":
                                user = new Resident(parts[0], parts[1]);
                                break;
                        }
                        if (user != null) {
                            user.setApproved(Boolean.parseBoolean(parts[3]));
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
                    if (parts.length == 4 && parts[0].equals(username) && parts[1].equals(password)) {
                        User user = null;
                        switch (parts[2]) {
                            case "Manager":
                                user = new Manager(parts[0], parts[1]);
                                break;
                            case "Staff":
                                user = new Staff(parts[0], parts[1]);
                                break;
                            case "Resident":
                                user = new Resident(parts[0], parts[1]);
                                break;
                        }
                        if (user != null) {
                            user.setApproved(Boolean.parseBoolean(parts[3]));
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


        public static void saveAllUsers(List<User> users) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
                for (User user : users) {
                    writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole() + "," + user.isApproved());
                    writer.newLine();
                }
            }
        }
    }

    // Manager class
    public static class Manager extends User {
        public Manager(String username, String password) {
            super(username, password, "Manager");
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
                    return;
                }

                System.out.println("Unapproved Staffs:");
                for (int i = 0; i < unapprovedStaffs.size(); i++) {
                    User user = unapprovedStaffs.get(i);
                    System.out.println((i + 1) + ". " + user.username + " (" + user.role + ")");
                }

                System.out.println("Unapproved Residents:");
                for (int i = 0; i < unapprovedResidents.size(); i++) {
                    User user = unapprovedResidents.get(i);
                    System.out.println((i + 1 + unapprovedStaffs.size()) + ". " + user.username + " (" + user.role + ")");
                }

                System.out.print("Enter the number of the user to approve: ");
                Scanner scanner = new Scanner(System.in);
                int userIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // Consume newline

                if (userIndex >= 0 && userIndex < unapprovedStaffs.size()) {
                    User userToApprove = unapprovedStaffs.get(userIndex);
                    userToApprove.saveToFile("approved_staffs.txt");
                    unapprovedStaffs.remove(userIndex);
                    saveUnapprovedUsers(unapprovedStaffs, "unapproved_staffs.txt");
                    System.out.println("Staff approved successfully.");
                } else if (userIndex >= unapprovedStaffs.size() && userIndex < unapprovedStaffs.size() + unapprovedResidents.size()) {
                    User userToApprove = unapprovedResidents.get(userIndex - unapprovedStaffs.size());
                    userToApprove.saveToFile("approved_residents.txt");
                    unapprovedResidents.remove(userIndex - unapprovedStaffs.size());
                    saveUnapprovedUsers(unapprovedResidents, "unapproved_residents.txt");
                    System.out.println("Resident approved successfully.");
                } else {
                    System.out.println("Invalid user number.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while approving the user.");
                e.printStackTrace();
            }
        }

        private void saveUnapprovedUsers(List<User> users, String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    writer.write(user.username + "," + user.password + "," + user.role + "," + user.approved);
                    writer.newLine();
                }
            }
        }
        

        public void searchUser(String username) {
            // Search user logic
        }

        public void updateUser(User user) {
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
        public Staff(String username, String password) {
            super(username, password, "Staff");
        }

        @Override
        public void displayMenu() {
            System.out.println("Staff Menu:");
            System.out.println("1. Register Individual Login Account");
            System.out.println("2. Update Individual Login Account");
            System.out.println("3. Make Payment for Resident");
            System.out.println("4. Generate Receipt");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
    
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1:
                    // Register Individual Login Account logic
                    break;
                case 2:
                    // Update Individual Login Account logic
                    break;
                case 3:
                    // Make Payment for Resident logic
                    break;
                case 4:
                    // Generate Receipt logic
                    break;
                case 5:
                    // Logout logic
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        public void registerUser(User user) {
            // Register user logic
        }

        public void updateUser(User user) {
            // Update user logic
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
        public Resident(String username, String password) {
            super(username, password, "Resident");
        }

        @Override
        public void displayMenu() {
            // Resident-specific menu implementation
            System.out.println("Resident Menu:");
            System.out.println("1. Register Individual Login Account");
            System.out.println("2. Update Individual Login Account");
            System.out.println("3. View Payment Records");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Register Individual Login Account logic
                    break;
                case 2:
                    // Update Individual Login Account logic
                    break;
                case 3:
                    // View Payment Records logic
                    break;
                case 4:
                    // Logout logic
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
        }

        public void updateDetails() {
            // Update details logic
        }

        public void viewPaymentRecords() {
            // View payment records logic
        }
    }

    // Payment class
    public static class Payment {
        private String username;
        private double amount;
        private Date date;

        public Payment(String username, double amount, Date date) {
            this.username = username;
            this.amount = amount;
            this.date = date;
        }

        public void saveToFile() throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt", true))) {
                writer.write(username + "," + amount + "," + date);
                writer.newLine();
            }
        }

        public static List<Payment> readFromFile(String username) throws IOException {
            List<Payment> payments = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(username)) {
                        payments.add(new Payment(parts[0], Double.parseDouble(parts[1]), new Date(parts[2])));
                    }
                }
            }
            return payments;
        }
    }

    // Main method to test the backend functionality
    /* 
    public static void main(String[] args) {
        try {
            // Register a new Staff user
            User staff = new Staff("staff1", "password1");
            staff.saveToFile();

            // Register a new Resident user
            User resident = new Resident("resident1", "password1");
            resident.saveToFile();

            // Attempt to login as the Staff user
            User loggedInUser = User.readFromFile("staff1", "password1");
            if (loggedInUser != null) {
                loggedInUser.displayMenu();
            }

            // Attempt to login as the Resident user
            loggedInUser = User.readFromFile("resident1", "password1");
            if (loggedInUser != null) {
                loggedInUser.displayMenu();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/



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
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User manager = new Manager(username, password);
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
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = User.findUser(username, password, "managers.txt");
            if (user != null && user.getRole().equals("Manager")) {
                System.out.println("Login successful.");
                user.displayMenu();
            } else {
                System.out.println("Invalid username or password.");
                loginManager(); // Retry login
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle Staff registration
    public static void registerStaff() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User staff = new Staff(username, password);
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
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = User.findUser(username, password, "approved_staffs.txt");
            if (user != null && user.getRole().equals("Staff")) {
                System.out.println("Login successful.");
                user.displayMenu();
            } else {
                System.out.println("Invalid username or password.");
                loginStaff(); // Retry login
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle Resident registration
    public static void registerResident() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User resident = new Resident(username, password);
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
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = User.findUser(username, password, "approved_residents.txt");
            if (user != null && user.getRole().equals("Resident")) {
                System.out.println("Login successful.");
                user.displayMenu();
            } else {
                System.out.println("Invalid username or password.");
                loginResident(); // Retry login
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        // Main method to launch the application
    public static void main(String[] args) {
        displayWelcomePage();
    }
}
