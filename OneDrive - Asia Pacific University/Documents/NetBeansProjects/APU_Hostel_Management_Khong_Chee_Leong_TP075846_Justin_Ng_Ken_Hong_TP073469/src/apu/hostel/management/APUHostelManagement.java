// KHONG CHEE LEONG TP075846
// JUSTIN NG KEN HONG TP073469

package apu.hostel.management;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;

/**
 * Main class for APU Hostel Management System.
 * Handles user management, authentication, room bookings and payments.
 */
public class APUHostelManagement {

    /**
     * Abstract base class for all user types in the system.
     * Contains common user properties and methods.
     */
    public abstract static class User {
        protected String userID;
        protected String icPassportNumber;
        protected String username;
        protected String password;
        protected String contactNumber;
        protected String dateOfRegistration;
        protected String role;
        protected boolean isActive;

        // Constructor
        public User(String userID, String icPassportNumber, String username, String password, String contactNumber, String dateOfRegistration, String role, boolean isActive) {
            this.userID = userID;
            this.icPassportNumber = icPassportNumber;
            this.username = username;
            this.password = password;
            this.contactNumber = contactNumber;
            this.dateOfRegistration = dateOfRegistration;
            this.role = role;
            this.isActive = isActive;
        }

        @Override
        public String toString() {
            return "UserID: " + userID + ", IC/Passport Number: " + icPassportNumber + ", Username: " + username + ", Contact Number: " + contactNumber + ", Date of Registration: " + dateOfRegistration + ", Role: " + role + ", IsActive: " + isActive;
        }

        // Getters and setters
        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getUserID() {
            return userID;
        }

        public void setIcPassportNumber(String icPassportNumber) {
            this.icPassportNumber = icPassportNumber;
        }

        public String getIcPassportNumber() {
            return icPassportNumber;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setPassword(String password) {
            this.password = password;
        }    

        public String getPassword() {
            return password;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public String getDateOfRegistration() {
            return dateOfRegistration;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public boolean getIsActive() {
            return isActive;
        }

        public abstract void displayMenu();

        public void saveToFile(String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(userID + "," + icPassportNumber + "," + username + "," + password + "," + contactNumber + "," + dateOfRegistration + "," + role + "," + isActive);
                writer.newLine();
            }
        }

        // Method to read users from file
        public static List<User> readFromFile(String filename) throws IOException {
            List<User> users = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    User user = null;
                    if (parts.length == 8) {
                        switch (parts[6]) {
                            case "manager" -> user = new Manager(parts);
                            case "staff" -> user = new Staff(parts);
                            case "resident" -> user = new Resident(parts);
                        }
                    } else if (parts.length == 10) {
                        switch (parts[7]) {
                            case "manager" -> user = new Manager(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], Boolean.parseBoolean(parts[8]), parts[9]);
                            case "staff" -> user = new Staff(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], Boolean.parseBoolean(parts[8]), parts[9]);
                            case "resident" -> user = new Resident(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], Boolean.parseBoolean(parts[8]), parts[9]);
                        }
                    }
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
            return users;
        }


        // Method to find user by username and password
        public static User findUser(String username, String password, String filename) throws IOException {
            List<User> users = User.readFromFile(filename);
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
            return null;
        }

        
        public static boolean isUnique(String icPassportNumber, String username, String contactNumber) throws IOException {
            List<User> users = new ArrayList<>();
            users.addAll(User.readFromFile("users.txt"));
            users.addAll(User.readFromFile("unapproved_residents.txt"));
            users.addAll(User.readFromFile("approved_residents.txt"));
            users.addAll(User.readFromFile("unapproved_staffs.txt"));
            users.addAll(User.readFromFile("approved_staffs.txt"));
            users.addAll(User.readFromFile("approved_managers.txt"));
            users.addAll(User.readFromFile("unapproved_managers.txt"));
        
            for (User user : users) {
                if ((icPassportNumber != null && !icPassportNumber.isEmpty() && user.getIcPassportNumber().equals(icPassportNumber)) ||
                    (username != null && !username.isEmpty() && user.getUsername().equals(username)) ||
                    (contactNumber != null && !contactNumber.isEmpty() && user.getContactNumber().equals(contactNumber))) {
                    return false;
                }
            }
            return true;
        }

        public static void writeToFile(List<User> users, String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    writer.write(user.userID + "," + user.icPassportNumber + "," + user.username + "," + user.password + "," + user.contactNumber + "," + user.dateOfRegistration + "," + user.role + "," + user.isActive);
                    writer.newLine();
                }
            }
        }

        public static List<User> readFromFileForSearch(String filename) throws IOException {
            List<User> users = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    User user = null;
                    if (parts.length == 8) {
                        switch (parts[6]) {
                            case "manager" -> user = new Manager(parts);
                            case "staff" -> user = new Staff(parts);
                            case "resident" -> user = new Resident(parts);
                        }
                    } else if (parts.length == 10) {
                        switch (parts[7]) {
                            case "manager" -> user = new Manager(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7],Boolean.parseBoolean(parts[8]), parts[9]);
                            case "staff" -> user = new Staff(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7],Boolean.parseBoolean(parts[8]), parts[9]);
                            case "resident" -> user = new Resident(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7],Boolean.parseBoolean(parts[8]), parts[9]);
                        }
                    }
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
            return users;
        }

        public List<User> readUsersForSearch() throws IOException {
            List<User> users = new ArrayList<>();
            users.addAll(User.readFromFileForSearch("users.txt"));
            users.addAll(User.readFromFileForSearch("unapproved_managers.txt"));
            users.addAll(User.readFromFileForSearch("unapproved_staffs.txt"));
            users.addAll(User.readFromFileForSearch("unapproved_residents.txt"));
            return users;
        }


    }


    public static class Manager extends User {
        private String managerID;
        private String dateOfApproval;

        public Manager(String managerID, String userID, String icPassportNumber, String username, String password, String contactNumber, String dateOfRegistration, String role, boolean isActive, String dateOfApproval) {
            super(userID, icPassportNumber, username, password, contactNumber, dateOfRegistration, role, isActive);
            this.managerID = managerID;
            this.dateOfApproval = dateOfApproval;
        }

        public Manager(String[] parts) {
            super(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], Boolean.parseBoolean(parts[7]));
            this.dateOfApproval = parts[5];
        }

        public String getManagerID() {
            return managerID;
        }

        public void setManagerID(String managerID) {
            this.managerID = managerID;
        }

        public String getDateOfApproval() {
            return dateOfApproval;
        }

        public void setDateOfApproval(String dateOfApproval) {
            this.dateOfApproval = dateOfApproval;
        }

        @Override
        public String toString() {
            return "UserID: " + getUserID() + ", IC/Passport Number: " + getIcPassportNumber() + ", Username: " + getUsername() + ", Contact Number: " + getContactNumber() + ", Date of Registration: " + getDateOfRegistration() + ", Role: " + getRole() + ", IsActive: " + getIsActive() + ", Date of Approval: " + dateOfApproval;
        }

        public void saveToManagerFile(String managerID, String userID, String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(managerID + "," + userID + "," + getIcPassportNumber() + "," + getUsername() + "," + getPassword() + "," + getContactNumber() + "," + getDateOfRegistration() + "," + getRole() + "," + getIsActive() + "," + dateOfApproval);
                writer.newLine();
            }
        }

        public static List<Manager> readManagersFromFile(String filename) throws IOException {
            List<Manager> managers = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 10) {
                        managers.add(new Manager(parts));
                    }
                }
            }
            return managers;
        }

        public static Manager findManager(String username, String password, String filename) throws IOException {
            List<Manager> managers = readManagersFromFile(filename);
            for (Manager manager : managers) {
                if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                    return manager;
                }
            }
            return null;
        }

        private static final Scanner scanner = new Scanner(System.in);

        @Override
        public void displayMenu() {
            while (true) {

                System.out.println("Manager Menu:");
                System.out.println("1. Approve User Registration");
                System.out.println("2. Search, Update, Delete or Restore User");
                System.out.println("3. Fix, Update, Delete or Restore Rate");
                System.out.println("4. Manage Rooms");
                System.out.println("5. Update Personal Information");
                System.out.println("6. Logout");
                System.out.print("Enter your choice: ");
        
                int choice = getValidatedChoice(scanner, 1, 6);
        
                switch (choice) {
                    case 1 -> approveUserRegistration();
                    case 2 -> searchUsers();
                    case 3 -> fixOrUpdateRate();
                    case 4 -> manageRooms();
                    case 5 -> updatePersonalInformation();
                    case 6 -> {
                        System.out.println("Logging out...");

                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        
        
        public void updatePersonalInformation() {
            int choice;
        
            do {
                System.out.println("Update Personal Information:");
                System.out.println("1. Update IC Passport Number");
                System.out.println("2. Update Username");
                System.out.println("3. Update Password");
                System.out.println("4. Update Contact Number");
                System.out.println("0. Go Back to Manager Menu");
                System.out.print("Enter your choice: ");
                choice = getValidatedChoice(scanner, 0, 4);
        
                switch (choice) {
                    case 1 -> {
                        System.out.println("Current IC Passport Number: " + this.icPassportNumber);
                        while (true) {
                            System.out.print("Enter new IC Passport Number: ");
                            String newIcPassportNumber = scanner.nextLine();
                            String error = validateUpdateICPassport(newIcPassportNumber, this.icPassportNumber);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.icPassportNumber = newIcPassportNumber;
                            System.out.println("IC Passport Number updated successfully.");
                            break;
                        }
                    }
                    case 2 -> {
                        System.out.println("Current Username: " + this.username);
                        while (true) {
                            System.out.print("Enter new username: ");
                            String newUsername = scanner.nextLine();
                            String error = validateUpdateUsername(newUsername, this.username);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.username = newUsername;
                            System.out.println("Username updated successfully.");
                            break;
                        }
                    }
                    case 3 -> {
                        System.out.println("Current Password: " + this.password);
                        while (true) {
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();
                            String error = validateUpdatePassword(newPassword, this.username);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.password = newPassword;
                            System.out.println("Password updated successfully.");
                            break;
                        }
                    }
                    case 4 -> {
                        System.out.println("Current Contact Number: " + this.contactNumber);
                        while (true) {
                            System.out.print("Enter new contact number: ");
                            String newContactNumber = scanner.nextLine();
                            String error = validateUpdateContactNumber(newContactNumber, this.contactNumber);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.contactNumber = newContactNumber;
                            System.out.println("Contact number updated successfully.");
                            break;
                        }
                    }
                    case 0 -> {
                        System.out.println("Returning to Manager Menu...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
        
                try {
                    updateFile("approved_managers.txt", this);
                    updateFile("users.txt", this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (choice != 0);
        }

        private void approveUserRegistration() {
            try {
                List<User> unapprovedManagers = User.readFromFile("unapproved_managers.txt");
                List<User> unapprovedStaffs = User.readFromFile("unapproved_staffs.txt");
                List<User> unapprovedResidents = User.readFromFile("unapproved_residents.txt");
        
                if (unapprovedManagers.isEmpty() && unapprovedStaffs.isEmpty() && unapprovedResidents.isEmpty()) {
                    System.out.println("No users to approve.");
                    return;
                }
        
                System.out.println("Choose the role to approve:");
                System.out.println("1. Manager");
                System.out.println("2. Staff");
                System.out.println("3. Resident");
                System.out.print("Enter your choice (1-3): ");
                int roleChoice = getValidatedChoice(scanner, 1, 3);
        
                List<User> selectedRoleList = new ArrayList<>();
                String role = "";
        
                switch (roleChoice) {
                    case 1 -> {
                        selectedRoleList = unapprovedManagers;
                        role = "Manager";
                    }
                    case 2 -> {
                        selectedRoleList = unapprovedStaffs;
                        role = "Staff";
                    }
                    case 3 -> {
                        selectedRoleList = unapprovedResidents;
                        role = "Resident";
                    }
                    default -> {
                        System.out.println("Invalid choice. Returning to menu.");
                        return;
                    }
                }
        
                if (selectedRoleList.isEmpty()) {
                    System.out.println("No unapproved " + role.toLowerCase() + "s found.");
                    return;
                }
        
                System.out.println("Unapproved " + role + "s:");
                for (int i = 0; i < selectedRoleList.size(); i++) {
                    User user = selectedRoleList.get(i);
                    System.out.println((i + 1) + ". " + user.getUsername() + " (" + user.getIcPassportNumber() + ")");
                }
        
                System.out.print("Enter the number of the " + role.toLowerCase() + " to approve: ");
                int userIndex = getValidatedChoice(scanner, 1, selectedRoleList.size()) - 1;
        
                String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
                if (role.equals("Manager")) {
                    Manager managerToApprove = (Manager) selectedRoleList.get(userIndex);
                    String userID = generateUserID("U");
                    String managerID = generateUserID("M");
                    managerToApprove.setUserID(userID);
                    managerToApprove.setManagerID(managerID);
                    managerToApprove.setDateOfApproval(currentDate);
                    managerToApprove.setIsActive(true);
                    managerToApprove.saveToFile("users.txt");
                    managerToApprove.saveToManagerFile(managerID, userID, "approved_managers.txt");
                    selectedRoleList.remove(userIndex);
                    saveUnapprovedUsers(selectedRoleList, "unapproved_managers.txt");
                    System.out.println("Manager approved successfully.");
                } else if (role.equals("Staff")) {
                    Staff staffToApprove = (Staff) selectedRoleList.get(userIndex);
                    String userID = generateUserID("U");
                    String staffID = generateUserID("S");
                    staffToApprove.setUserID(userID);
                    staffToApprove.setStaffID(staffID);
                    staffToApprove.setDateOfApproval(currentDate);
                    staffToApprove.setIsActive(true);
                    staffToApprove.saveToFile("users.txt");
                    staffToApprove.saveToStaffFile(staffID, userID, "approved_staffs.txt");
                    selectedRoleList.remove(userIndex);
                    saveUnapprovedUsers(selectedRoleList, "unapproved_staffs.txt");
                    System.out.println("Staff approved successfully.");
                } else if (role.equals("Resident")) {
                    Resident residentToApprove = (Resident) selectedRoleList.get(userIndex);
                    String userID = generateUserID("U");
                    String residentID = generateUserID("R");
                    residentToApprove.setUserID(userID);
                    residentToApprove.setResidentID(residentID);
                    residentToApprove.setDateOfApproval(currentDate);
                    residentToApprove.setIsActive(true);
                    residentToApprove.saveToFile("users.txt");
                    residentToApprove.saveToResidentFile(residentID, userID, "approved_residents.txt");
                    selectedRoleList.remove(userIndex);
                    saveUnapprovedUsers(selectedRoleList, "unapproved_residents.txt");
                    System.out.println("Resident approved successfully.");
                } else {
                    System.out.println("Invalid user number.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred while approving the user.");
            }
        }
        
        //Method to save unapproved users to file
        static void saveUnapprovedUsers(List<User> users, String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    if (user instanceof Manager) {
                        Manager manager = (Manager) user;
                        manager.setIsActive(true);
                        writer.write(manager.getManagerID() + "," + manager.getUserID() + "," + manager.getIcPassportNumber() + "," + manager.getUsername() + "," + manager.getPassword() + "," + manager.getContactNumber() + "," + manager.getDateOfRegistration() + "," + manager.getRole() + "," + manager.getIsActive() + "," + null);
                    } else if (user instanceof Staff) {
                        Staff staff = (Staff) user;
                        staff.setIsActive(true);
                        writer.write(staff.getStaffID() + "," + staff.getUserID() + "," + staff.getIcPassportNumber() + "," + staff.getUsername() + "," + staff.getPassword() + "," + staff.getContactNumber() + "," + staff.getDateOfRegistration() + "," + staff.getRole() + "," + staff.getIsActive() + "," + null);
                    } else if (user instanceof Resident) {
                        Resident resident = (Resident) user;
                        resident.setIsActive(true);
                        writer.write(resident.getResidentID() + "," + resident.getUserID() + "," + resident.getIcPassportNumber() + "," + resident.getUsername() + "," + resident.getPassword() + "," + resident.getContactNumber() + "," + resident.getDateOfRegistration() + "," + resident.getRole() + "," + resident.getIsActive() + "," + null);
                    }
                    writer.newLine();
                }
            }
        }
        

        public void searchUsers() {
            List<User> users = new ArrayList<>();
        

            try {
                users = readUsersForSearch();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        
            while (true) {

                System.out.println("Filter options:");
                System.out.println("1. Approved/Unapproved");
                System.out.println("2. Role");
                System.out.println("3. IsActive");
                System.out.println("4. No filter");
                System.out.print("Enter your choice (1-4): ");
        
                int filterChoice = getValidatedChoice(scanner, 1, 4);
        
                List<User> filteredUsers = new ArrayList<>(users);
        
                switch (filterChoice) {
                    case 1 -> {
                        System.out.println("1. Approved");
                        System.out.println("2. Unapproved");
                        System.out.print("Enter your choice (1-2): ");
                        int approvalChoice = getValidatedChoice(scanner, 1, 2);
                        try {
                            if (approvalChoice == 1) {

                                filteredUsers = readApprovedUsers();
                            } else {
                                
                                filteredUsers = readUnapprovedUsers();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    case 2 -> {
                        System.out.println("1. Manager");
                        System.out.println("2. Staff");
                        System.out.println("3. Resident");
                        System.out.print("Enter your choice (1-3): ");
                        int roleChoice = getValidatedChoice(scanner, 1, 3);
                        final String[] role = {""};
                        switch (roleChoice) {
                            case 1 -> role[0] = "manager";
                            case 2 -> role[0] = "staff";
                            case 3 -> role[0] = "resident";
                        }
                        filteredUsers = filteredUsers.stream()
                                .filter(user -> user.getRole().equalsIgnoreCase(role[0]))
                                .collect(Collectors.toList());
                    }
                    case 3 -> {
                        System.out.println("1. Active");
                        System.out.println("2. Inactive");
                        System.out.print("Enter your choice (1-2): ");
                        int activeChoice = getValidatedChoice(scanner, 1, 2);
                        boolean isActive = (activeChoice == 1);
                        filteredUsers = filteredUsers.stream()
                                .filter(user -> user.getIsActive() == isActive)
                                .collect(Collectors.toList());
                    }
                    default -> {
                    }
                }
        
                
                System.out.println("Sort options:");
                System.out.println("1. Primary Key Ascending");
                System.out.println("2. Primary Key Descending");
                System.out.println("3. Username Ascending");
                System.out.println("4. Username Descending");
                System.out.print("Enter your choice (1-4): ");
        
                int sortChoice = getValidatedChoice(scanner, 1, 4);
        
                switch (sortChoice) {
                    case 1 -> filteredUsers.sort(Comparator.comparing(User::getUserID));
                    case 2 -> filteredUsers.sort(Comparator.comparing(User::getUserID).reversed());
                    case 3 -> filteredUsers.sort(Comparator.comparing(User::getUsername));
                    case 4 -> filteredUsers.sort(Comparator.comparing(User::getUsername).reversed());
                    default -> {
                    }
                }
        
                
                System.out.print("Enter username to search (or press Enter to skip): ");
                String usernameSearch = scanner.nextLine();
                if (!usernameSearch.isEmpty()) {
                    filteredUsers = filteredUsers.stream()
                            .filter(user -> user.getUsername().toLowerCase().contains(usernameSearch.toLowerCase()))
                            .collect(Collectors.toList());
                }
        
                
                System.out.println("Filtered and Sorted Users:");
                int index = 1;
                for (User user : filteredUsers) {
                    System.out.println(index + ". " + user);
                    index++;
                }
                System.out.println("Total users: " + filteredUsers.size());
                System.out.println("Search completed.");
        
                if (filteredUsers.isEmpty()) {
                    System.out.print("No users found. Do you want to search again? (yes/no): ");
                    String retryChoice = scanner.nextLine();
                    if (retryChoice.equalsIgnoreCase("yes")) {
                        continue; 
                    } else {
                        return; 
                    }
                }
        
                updateDeleteOrRestoreUser(filteredUsers);
                break; 
            }
        }
        
        //Method to read approved users from file
        public static List<User> readApprovedUsers() throws IOException {
            return User.readFromFileForSearch("users.txt");
        }
        
        //Method to read unapproved users from file
        public static List<User> readUnapprovedUsers() throws IOException {
            List<User> unapprovedUsers = new ArrayList<>();
            unapprovedUsers.addAll(User.readFromFileForSearch("unapproved_managers.txt"));
            unapprovedUsers.addAll(User.readFromFileForSearch("unapproved_staffs.txt"));
            unapprovedUsers.addAll(User.readFromFileForSearch("unapproved_residents.txt"));
            return unapprovedUsers;
        }

        public void updateDeleteOrRestoreUser(List<User> users) {
            OUTER:
            while (true) {
                System.out.println("User Management:");
                System.out.println("1. Choose user to update or delete");
                System.out.println("2. Delete all users");
                System.out.println("3. Restore all users");
                System.out.println("4. Return to main menu");
                System.out.print("Enter your choice (1-4): ");
                int choice = getValidatedChoice(scanner, 1, 4);
                switch (choice) {
                    case 1 -> {
                        
                        System.out.print("Enter the number of the user to update or delete (or 0 to cancel): ");
                        int userChoice = getValidatedChoice(scanner, 0, users.size());
                        if (userChoice == 0) {
                            System.out.println("Operation cancelled.");
                            continue;
                        }
                        User userToUpdate = users.get(userChoice - 1);
                        
                        System.out.println("1. Update User");
                        System.out.println("2. Delete User");
                        System.out.println("3. Restore User");
                        System.out.println("4. Return to main menu");
                        System.out.print("Enter your choice: ");
                        int actionChoice = getValidatedChoice(scanner, 1, 4);
                        switch (actionChoice) {
                            case 1 -> updateUser(userToUpdate);
                            case 2 -> deleteUser(userToUpdate);
                            case 3 -> restoreUser(userToUpdate);
                            default -> {
                                System.out.println("Returning to main menu...");
                                break OUTER;
                            }
                        }
                    }
                    case 2 -> deleteAllUsers(users);
                    case 3 -> restoreAllUsers(users);
                    default -> {
                        System.out.println("Returning to main menu...");
                        return;
                    }
                }
            }
        }
        
        private void deleteAllUsers(List<User> users) {
            System.out.println("Users that will be deleted:");
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i));
            }
        
            System.out.print("Are you sure you want to delete all users? This action cannot be undone. You can restore all users on the menu. (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                for (User user : users) {
                    user.setIsActive(false);
                }
                System.out.println("All users deleted successfully.");
                try {
                    for (String filename : getAllFilenames()) {
                        for (User user : users) {
                            updateFile(filename, user);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Delete all users cancelled.");
            }
        }
        
        private void restoreAllUsers(List<User> users) {
            System.out.println("Users that will be restored:");
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i));
            }
        
            System.out.print("Are you sure you want to restore all users? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                for (User user : users) {
                    user.setIsActive(true);
                }
                System.out.println("All users restored successfully.");
                try {
                    for (String filename : getAllFilenames()) {
                        for (User user : users) {
                            updateFile(filename, user);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Restore all users cancelled.");
            }
        }
        
        private List<String> getAllFilenames() {
            return Arrays.asList("approved_staffs.txt", "approved_residents.txt", "users.txt", "approved_managers.txt", "unapproved_managers.txt", "unapproved_staffs.txt", "unapproved_residents.txt");
        }
        
        public void updateUser(User userToUpdate) {
            int choice;
            do {
                System.out.println("Update User Information:");
                System.out.println("1. Update IC Passport Number");
                System.out.println("2. Update Username");
                System.out.println("3. Update Password");
                System.out.println("4. Update Contact Number");
                System.out.println("0. Go Back");
                System.out.print("Enter your choice: ");
                choice = getValidatedChoice(scanner, 0, 4);
        
                switch (choice) {
                    case 1 -> {
                        System.out.println("Current IC Passport Number: " + userToUpdate.getIcPassportNumber());
                        while (true) {
                            System.out.print("Enter new IC Passport Number: ");
                            String newIcPassportNumber = scanner.nextLine();
                            String error = validateUpdateICPassport(newIcPassportNumber, userToUpdate.getIcPassportNumber());
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            userToUpdate.setIcPassportNumber(newIcPassportNumber);
                            System.out.println("IC Passport Number updated successfully.");
                            break;
                        }
                    }
                    case 2 -> {
                        System.out.println("Current Username: " + userToUpdate.getUsername());
                        while (true) {
                            System.out.print("Enter new username: ");
                            String newUsername = scanner.nextLine();
                            String error = validateUpdateUsername(newUsername, userToUpdate.getUsername());
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            userToUpdate.setUsername(newUsername);
                            System.out.println("Username updated successfully.");
                            break;
                        }
                    }
                    case 3 -> {
                        System.out.println("Current Password: " + userToUpdate.getPassword());
                        while (true) {
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();
                            String error = validateUpdatePassword(newPassword, userToUpdate.getUsername());
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            userToUpdate.setPassword(newPassword);
                            System.out.println("Password updated successfully.");
                            break;
                        }
                    }
                    case 4 -> {
                        System.out.println("Current Contact Number: " + userToUpdate.getContactNumber());
                        while (true) {
                            System.out.print("Enter new contact number: ");
                            String newContactNumber = scanner.nextLine();
                            String error = validateUpdateContactNumber(newContactNumber, userToUpdate.getContactNumber());
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            userToUpdate.setContactNumber(newContactNumber);
                            System.out.println("Contact number updated successfully.");
                            break;
                        }
                    }
                    case 0 -> {
                        System.out.println("Returning to main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
        
                try {
                    updateFile("approved_staffs.txt", userToUpdate);
                    updateFile("approved_residents.txt", userToUpdate);
                    updateFile("users.txt", userToUpdate);
                    updateFile("approved_managers.txt", userToUpdate);
                    updateFile("unapproved_managers.txt", userToUpdate);
                    updateFile("unapproved_staffs.txt", userToUpdate);
                    updateFile("unapproved_residents.txt", userToUpdate);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (choice != 0);
        }
        
        public void deleteUser(User userToUpdate) {
            if (!userToUpdate.getIsActive()) {
                System.out.println("This user is already deactivated.");
            } else {
                System.out.println("User Details:");
                System.out.println("IC Passport Number: " + userToUpdate.getIcPassportNumber());
                System.out.println("Username: " + userToUpdate.getUsername());
                System.out.println("Password: " + userToUpdate.getPassword());
                System.out.println("Contact Number: " + userToUpdate.getContactNumber());
                System.out.print("Are you sure you want to delete this user? (yes/no): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();
        
                if (confirmation.equals("yes")) {
                    
                    userToUpdate.setIsActive(false);
                    System.out.println("User deactivated successfully.");
        
                    try {
                        updateFile("approved_staffs.txt", userToUpdate);
                        updateFile("approved_residents.txt", userToUpdate);
                        updateFile("users.txt", userToUpdate);
                        updateFile("approved_managers.txt", userToUpdate);
                        updateFile("unapproved_managers.txt", userToUpdate);
                        updateFile("unapproved_staffs.txt", userToUpdate);
                        updateFile("unapproved_residents.txt", userToUpdate);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("User deletion cancelled.");
                }
            }
        }
        
        public void restoreUser(User userToRestore) {
            if (userToRestore.getIsActive()) {
                System.out.println("This user is already active.");
                return;
            }
        
            
            System.out.println("User Details:");
            System.out.println("IC Passport Number: " + userToRestore.getIcPassportNumber());
            System.out.println("Username: " + userToRestore.getUsername());
            System.out.println("Password: " + userToRestore.getPassword());
            System.out.println("Contact Number: " + userToRestore.getContactNumber());
            System.out.print("Are you sure you want to restore this user? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
        
            if (confirmation.equals("yes")) {
                
                userToRestore.setIsActive(true);
                System.out.println("User restored successfully.");
        
                try {
                    updateFile("approved_staffs.txt", userToRestore);
                    updateFile("approved_residents.txt", userToRestore);
                    updateFile("users.txt", userToRestore);
                    updateFile("approved_managers.txt", userToRestore);
                    updateFile("unapproved_managers.txt", userToRestore);
                    updateFile("unapproved_staffs.txt", userToRestore);
                    updateFile("unapproved_residents.txt", userToRestore);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("User restoration cancelled.");
            }
        }
        
        //Method to update user information in file
        static void updateFile(String filename, User updatedUser) throws IOException {
            List<User> users = User.readFromFile(filename);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    boolean isMatch = user.getUserID().equals(updatedUser.getUserID());
                    if (filename.equals("unapproved_staffs.txt") || filename.equals("unapproved_residents.txt") || filename.equals("unapproved_managers.txt")) {
                        isMatch = user.getIcPassportNumber().equals(updatedUser.getIcPassportNumber()) ||
                                  user.getUsername().equals(updatedUser.getUsername()) ||
                                  user.getContactNumber().equals(updatedUser.getContactNumber());
                    }
        
                    if (isMatch) {
                        if (filename.equals("users.txt")) {
                            writer.write(updatedUser.getUserID() + "," + updatedUser.getIcPassportNumber() + "," + updatedUser.getUsername() + "," + updatedUser.getPassword() + "," + updatedUser.getContactNumber() + "," + updatedUser.getDateOfRegistration() + "," + updatedUser.getRole() + "," + updatedUser.getIsActive());
                        } else if (filename.equals("approved_managers.txt") && user instanceof Manager) {
                            Manager manager = (Manager) user;
                            writer.write(manager.getManagerID() + "," + updatedUser.getUserID() + "," + updatedUser.getIcPassportNumber() + "," + updatedUser.getUsername() + "," + updatedUser.getPassword() + "," + updatedUser.getContactNumber() + "," + updatedUser.getDateOfRegistration() + "," + updatedUser.getRole() + "," + updatedUser.getIsActive() + "," + manager.getDateOfApproval());
                        } else if (filename.equals("approved_staffs.txt") && user instanceof Staff) {
                            Staff staff = (Staff) user;
                            writer.write(staff.getStaffID() + "," + updatedUser.getUserID() + "," + updatedUser.getIcPassportNumber() + "," + updatedUser.getUsername() + "," + updatedUser.getPassword() + "," + updatedUser.getContactNumber() + "," + updatedUser.getDateOfRegistration() + "," + updatedUser.getRole() + "," + updatedUser.getIsActive() + "," + staff.getDateOfApproval());
                        } else if (filename.equals("approved_residents.txt") && user instanceof Resident) {
                            Resident resident = (Resident) user;
                            writer.write(resident.getResidentID() + "," + updatedUser.getUserID() + "," + updatedUser.getIcPassportNumber() + "," + updatedUser.getUsername() + "," + updatedUser.getPassword() + "," + updatedUser.getContactNumber() + "," + updatedUser.getDateOfRegistration() + "," + updatedUser.getRole() + "," + updatedUser.getIsActive() + "," + resident.getDateOfApproval());
                        } else if (filename.equals("unapproved_managers.txt") && user instanceof Manager) {
                            writer.write("null,null," + updatedUser.getIcPassportNumber() + "," + updatedUser.getUsername() + "," + updatedUser.getPassword() + "," + updatedUser.getContactNumber() + "," + updatedUser.getDateOfRegistration() + "," + updatedUser.getRole() + "," + updatedUser.getIsActive() + ",null");
                        } else if (filename.equals("unapproved_staffs.txt") && user instanceof Staff) {
                            writer.write("null,null," + updatedUser.getIcPassportNumber() + "," + updatedUser.getUsername() + "," + updatedUser.getPassword() + "," + updatedUser.getContactNumber() + "," + updatedUser.getDateOfRegistration() + "," + updatedUser.getRole() + "," + updatedUser.getIsActive() + ",null");
                        } else if (filename.equals("unapproved_residents.txt") && user instanceof Resident) {
                            writer.write("null,null," + updatedUser.getIcPassportNumber() + "," + updatedUser.getUsername() + "," + updatedUser.getPassword() + "," + updatedUser.getContactNumber() + "," + updatedUser.getDateOfRegistration() + "," + updatedUser.getRole() + "," + updatedUser.getIsActive() + ",null");
                        }
                    } else {
                        if (filename.equals("users.txt")) {
                            writer.write(user.getUserID() + "," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole() + "," + user.getIsActive());
                        } else if (filename.equals("approved_managers.txt") && user instanceof Manager) {
                            Manager manager = (Manager) user;
                            writer.write(manager.getManagerID() + "," + user.getUserID() + "," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole() + "," + user.getIsActive() + "," + manager.getDateOfApproval());
                        } else if (filename.equals("approved_staffs.txt") && user instanceof Staff) {
                            Staff staff = (Staff) user;
                            writer.write(staff.getStaffID() + "," + staff.getUserID() + "," + staff.getIcPassportNumber() + "," + staff.getUsername() + "," + staff.getPassword() + "," + staff.getContactNumber() + "," + staff.getDateOfRegistration() + "," + staff.getRole() + "," + staff.getIsActive() + "," + staff.getDateOfApproval());
                        } else if (filename.equals("approved_residents.txt") && user instanceof Resident) {
                            Resident resident = (Resident) user;
                            writer.write(resident.getResidentID() + "," + resident.getUserID() + "," + resident.getIcPassportNumber() + "," + resident.getUsername() + "," + resident.getPassword() + "," + resident.getContactNumber() + "," + resident.getDateOfRegistration() + "," + resident.getRole() + "," + resident.getIsActive() + "," + resident.getDateOfApproval());
                        } else if (filename.equals("unapproved_managers.txt") && user instanceof Manager) {
                            writer.write("null,null," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole() + "," + user.getIsActive() + ",null");
                        } else if (filename.equals("unapproved_staffs.txt") && user instanceof Staff) {
                            writer.write("null,null," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole() + "," + user.getIsActive() + ",null");
                        } else if (filename.equals("unapproved_residents.txt") && user instanceof Resident) {
                            writer.write("null,null," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole() + "," + user.getIsActive() + ",null");
                        }
                    }
                    writer.newLine();
                }
            }
        }

        //Method to read rates from file
        public static List<FeeRate> readRatesFromFile(String filename) throws IOException {
            List<FeeRate> feeRates = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        FeeRate feeRate = new FeeRate(parts[0], parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Boolean.parseBoolean(parts[6]));
                        feeRates.add(feeRate);
                    }
                }
            }
            return feeRates;
        }
        
        public void fixOrUpdateRate() {
            List<FeeRate> rates = new ArrayList<>();
        
            
            try {
                rates = readRatesFromFile("fee_rates.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            OUTER:
            while (true) {
                System.out.println("Fix or Update Rates:");
                System.out.println("1. Set Initial Rates");
                System.out.println("2. Update Existing Rates");
                System.out.println("3. Delete Rate");
                System.out.println("4. Restore Deleted Rate");
                System.out.println("5. Delete All Rates");
                System.out.println("6. Restore All Rates");
                System.out.println("7. Exit");
                System.out.print("Enter your choice (1-7): ");
                int choice = getValidatedChoice(scanner, 1, 7);
                switch (choice) {
                    case 1 -> {
                        setInitialRates(scanner, rates);
                        saveRatesToFile(rates);
                    }
                    case 2 -> {
                        updateExistingRates(scanner, rates);
                        saveRatesToFile(rates);
                    }
                    case 3 -> {
                        deleteRate(scanner, rates);
                        saveRatesToFile(rates);
                    }
                    case 4 -> {
                        restoreDeletedRate(scanner, rates);
                        saveRatesToFile(rates);
                    }
                    case 5 -> {
                        deleteAllRates(scanner, rates);
                        saveRatesToFile(rates);
                    }
                    case 6 -> {
                        restoreAllRates(scanner, rates);
                        saveRatesToFile(rates);
                    }
                    case 7 -> {
                        break OUTER;
                    }
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
            displayMenu();
        }
        
        private void setInitialRates(Scanner scanner, List<FeeRate> rates) {
            while (true) {
                String feeRateID = "FR" + String.format("%02d", rates.size() + 1);
        
                System.out.println("Available Room Types:");
                System.out.println("1. Standard");
                System.out.println("2. Large");
                System.out.println("3. Family");
                System.out.print("Enter your choice (1-3): ");
                int roomTypeChoice = getValidatedChoice(scanner, 1, 3);
                String roomType;
                switch (roomTypeChoice) {
                    case 1 -> roomType = "standard";
                    case 2 -> roomType = "large";
                    case 3 -> roomType = "family";
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                    }
                }
                double currentRate=0;
        
                double dailyRate = validateRate("Daily Rate",currentRate);
                double weeklyRate = validateRate("Weekly Rate", currentRate);
                double monthlyRate = validateRate("Monthly Rate", currentRate);
                double yearlyRate = validateRate("Yearly Rate", currentRate);
        
                System.out.println("Fee Rate Details:");
                System.out.println("Fee Rate ID: " + feeRateID);
                System.out.println("Room Type: " + roomType);
                System.out.println("Daily Rate: " + dailyRate);
                System.out.println("Weekly Rate: " + weeklyRate);
                System.out.println("Monthly Rate: " + monthlyRate);
                System.out.println("Yearly Rate: " + yearlyRate);
                System.out.print("Are you sure you want to add this rate? (yes/no): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (confirm.equals("yes")) {
                    rates.add(new FeeRate(feeRateID, roomType, dailyRate, weeklyRate, monthlyRate, yearlyRate, true));
                    System.out.println("Rate added successfully.");
                } else {
                    System.out.println("Rate addition cancelled.");
                }
        
                System.out.print("Do you want to add another rate? (yes/no): ");
                String addMore = scanner.nextLine().trim().toLowerCase();
                if (addMore.equalsIgnoreCase("no")) {
                    break;
                }
            }
        }
        
        
        private void updateExistingRates(Scanner scanner, List<FeeRate> rates) {
            if (rates.isEmpty()) {
                System.out.println("No existing rates to update.");
                return;
            }
        
            System.out.println("Existing Fee Rates:");
            for (int i = 0; i < rates.size(); i++) {
                System.out.println((i + 1) + ". " + rates.get(i));
            }
        
            System.out.print("Enter the number of the fee rate to update: ");
            int rateChoice = getValidatedChoice(scanner, 1, rates.size());
            FeeRate rateToUpdate = rates.get(rateChoice - 1);
        
            System.out.println("Current Rates:");
            System.out.println("Room Type: " + rateToUpdate.getRoomType());
            System.out.println("Daily Rate: " + rateToUpdate.getDailyRate());
            System.out.println("Weekly Rate: " + rateToUpdate.getWeeklyRate());
            System.out.println("Monthly Rate: " + rateToUpdate.getMonthlyRate());
            System.out.println("Yearly Rate: " + rateToUpdate.getYearlyRate());
        
            System.out.println("Which attribute do you want to update?");
            System.out.println("1. Room Type");
            System.out.println("2. Daily Rate");
            System.out.println("3. Weekly Rate");
            System.out.println("4. Monthly Rate");
            System.out.println("5. Yearly Rate");
            System.out.print("Enter your choice (1-5): ");
            int attributeChoice = getValidatedChoice(scanner, 1, 5);
        
            
            List<String> restrictedFeeRateIDs = new ArrayList<>();
            List<Room> rooms = readRoomsFromFile("rooms.txt");
            for (Room room : rooms) {
                if (!restrictedFeeRateIDs.contains(room.getFeeRateID())) {
                    restrictedFeeRateIDs.add(room.getFeeRateID());
                }
            }
        
            if (attributeChoice == 1 && restrictedFeeRateIDs.contains(rateToUpdate.getFeeRateID())) {
                System.out.println("Cannot update room type for fee rate ID: " + rateToUpdate.getFeeRateID() + " as it exists in rooms.txt.");
                return;
            }
        
            if (attributeChoice == 1) {
                System.out.println("Available Room Types:");
                System.out.println("1. Standard");
                System.out.println("2. Large");
                System.out.println("3. Family");
                System.out.print("Enter your choice (1-3): ");
                int roomTypeChoice = getValidatedChoice(scanner, 1, 3);
                String roomType;
                int roomCapacity = 0;
                switch (roomTypeChoice) {
                    case 1 -> {
                        roomType = "standard";
                        roomCapacity = 1;
                    }
                    case 2 -> {
                        roomType = "large";
                        roomCapacity = 3;
                    }
                    case 3 -> {
                        roomType = "family";
                        roomCapacity = 6;
                    }
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                        return;
                    }
                }
                if (roomType.equals(rateToUpdate.getRoomType())) {
                    System.out.println("The selected room type is the same as the current room type.");
                    return;
                }
                System.out.println("Current Room Type: " + rateToUpdate.getRoomType());
                System.out.println("New Room Type: " + roomType);
                System.out.print("Are you sure you want to update the room type? (yes/no): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (confirm.equals("yes")) {
                    rateToUpdate.setRoomType(roomType);
        
                    
                    for (Room room : rooms) {
                        if (room.getFeeRateID().equals(rateToUpdate.getFeeRateID())) {
                            room.setRoomType(roomType);
                            room.setRoomCapacity(roomCapacity);
                        }
                    }
                    saveRoomsToFile(rooms);
        
                    System.out.println("Room Type updated successfully.");
                } else {
                    System.out.println("Room type update cancelled.");
                }
            } else {
                double currentRate = 0;
                double newRate = validateRate("new rate", currentRate);
                if (newRate == getCurrentRate(rateToUpdate, attributeChoice)) {
                    System.out.println("The new rate is the same as the current rate.");
                    return;
                }
                System.out.println("Current Rate: " + getCurrentRate(rateToUpdate, attributeChoice));
                System.out.println("New Rate: " + newRate);
                System.out.print("Are you sure you want to update the rate? (yes/no): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (confirm.equals("yes")) {
                    switch (attributeChoice) {
                        case 2 -> rateToUpdate.setDailyRate(newRate);
                        case 3 -> rateToUpdate.setWeeklyRate(newRate);
                        case 4 -> rateToUpdate.setMonthlyRate(newRate);
                        case 5 -> rateToUpdate.setYearlyRate(newRate);
                    }
                    System.out.println("Rate updated successfully.");
                } else {
                    System.out.println("Rate update cancelled.");
                }
            }
        }
        
        private double getCurrentRate(FeeRate rate, int attributeChoice) {
            return switch (attributeChoice) {
                case 2 -> rate.getDailyRate();
                case 3 -> rate.getWeeklyRate();
                case 4 -> rate.getMonthlyRate();
                case 5 -> rate.getYearlyRate();
                default -> -1;
            };
        }
        
        private void deleteRate(Scanner scanner, List<FeeRate> rates) {
            if (rates.isEmpty()) {
                System.out.println("No existing rates to delete.");
                return;
            }
        
            
            List<String> usedFeeRateIDs = new ArrayList<>();
            List<Room> rooms = readRoomsFromFile("rooms.txt");
            for (Room room : rooms) {
                if (!usedFeeRateIDs.contains(room.getFeeRateID())) {
                    usedFeeRateIDs.add(room.getFeeRateID());
                }
            }
        
            System.out.println("Existing Fee Rates:");
            List<FeeRate> deletableRates = new ArrayList<>();
            for (int i = 0; i < rates.size(); i++) {
                if (rates.get(i).isActive() && !usedFeeRateIDs.contains(rates.get(i).getFeeRateID())) {
                    deletableRates.add(rates.get(i));
                    System.out.println((deletableRates.size()) + ". " + rates.get(i));
                }
            }
        
            if (deletableRates.isEmpty()) {
                System.out.println("No deletable rates available.");
                return;
            }
        
            System.out.print("Enter the number of the fee rate to delete: ");
            int rateChoice = getValidatedChoice(scanner, 1, deletableRates.size());
            FeeRate rateToDelete = deletableRates.get(rateChoice - 1);
        
            System.out.println("Rate Details:");
            System.out.println(rateToDelete);
        
            System.out.print("Are you sure you want to delete this rate? (yes/no): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                rateToDelete.setActive(false);
                System.out.println("Rate deleted successfully.");
            } else {
                System.out.println("Rate deletion cancelled.");
            }
        }
        
        private void restoreDeletedRate(Scanner scanner, List<FeeRate> rates) {
            List<FeeRate> deletedRates = new ArrayList<>();
            for (FeeRate rate : rates) {
                if (!rate.isActive()) {
                    deletedRates.add(rate);
                }
            }
        
            if (deletedRates.isEmpty()) {
                System.out.println("No deleted rates to restore.");
                return;
            }
        
            System.out.println("Deleted Fee Rates:");
            for (int i = 0; i < deletedRates.size(); i++) {
                System.out.println((i + 1) + ". " + deletedRates.get(i));
            }
        
            System.out.print("Enter the number of the fee rate to restore: ");
            int rateChoice = getValidatedChoice(scanner, 1, deletedRates.size());
            FeeRate rateToRestore = deletedRates.get(rateChoice - 1);
        
            System.out.println("Rate Details:");
            System.out.println(rateToRestore);
        
            System.out.print("Are you sure you want to restore this rate? (yes/no): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                rateToRestore.setActive(true);
                System.out.println("Rate restored successfully.");
            } else {
                System.out.println("Rate restoration cancelled.");
            }
        }
        
        private void deleteAllRates(Scanner scanner, List<FeeRate> rates) {
            if (rates.isEmpty()) {
                System.out.println("No existing rates to delete.");
                return;
            }
        
            
            List<String> usedFeeRateIDs = new ArrayList<>();
            List<Room> rooms = readRoomsFromFile("rooms.txt");
            for (Room room : rooms) {
                if (!usedFeeRateIDs.contains(room.getFeeRateID())) {
                    usedFeeRateIDs.add(room.getFeeRateID());
                }
            }
        
            List<FeeRate> deletableRates = new ArrayList<>();
            for (FeeRate rate : rates) {
                if (!usedFeeRateIDs.contains(rate.getFeeRateID())) {
                    deletableRates.add(rate);
                }
            }
        
            if (deletableRates.isEmpty()) {
                System.out.println("No deletable rates available.");
                return;
            }
        
            System.out.println("Rates that will be deleted:");
            for (int i = 0; i < deletableRates.size(); i++) {
                System.out.println((i + 1) + ". " + deletableRates.get(i));
            }
        
            System.out.print("Are you sure you want to delete all these rates? This action cannot be undone. You can restore all rates on the menu. (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                for (FeeRate rate : deletableRates) {
                    rate.setActive(false);
                }
                saveRatesToFile(rates);
                System.out.println("All deletable rates deleted successfully.");
            } else {
                System.out.println("Delete all rates cancelled.");
            }
        }
        
        private void restoreAllRates(Scanner scanner, List<FeeRate> rates) {
            List<FeeRate> deletedRates = new ArrayList<>();
            for (FeeRate rate : rates) {
                if (!rate.isActive()) {
                    deletedRates.add(rate);
                }
            }
        
            if (deletedRates.isEmpty()) {
                System.out.println("No deleted rates to restore.");
                return;
            }
        
            System.out.println("Rates that will be restored:");
            for (int i = 0; i < deletedRates.size(); i++) {
                System.out.println((i + 1) + ". " + deletedRates.get(i));
            }
        
            System.out.print("Are you sure you want to restore all these rates? (yes/no): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                for (FeeRate rate : deletedRates) {
                    rate.setActive(true);
                }
                saveRatesToFile(rates);
                System.out.println("All rates restored successfully.");
            } else {
                System.out.println("Restore all rates cancelled.");
            }
        }

        //Get fee rate that are currently being used
        public static List<String> getRestrictedFeeRateIDs() {
            List<String> restrictedFeeRateIDs = new ArrayList<>();
            List<Room> rooms = readRoomsFromFile("rooms.txt");
            for (Room room : rooms) {
                if (!restrictedFeeRateIDs.contains(room.getFeeRateID())) {
                    restrictedFeeRateIDs.add(room.getFeeRateID());
                }
            }
            return restrictedFeeRateIDs;
        }

        //Method to validate rate
        public static double validateRate(String rateType, double currentRate) {
            if (currentRate <= 0) {
                throw new IllegalArgumentException(rateType + " must be greater than zero");
            }
            return currentRate;
        }

        //Method to generate Fee Rate ID
        public static String generateFeeRateID(int currentSize) {
            return "FR" + String.format("%02d", currentSize + 1);
        }


        //Method to save rates to file
        static void saveRatesToFile(List<FeeRate> rates) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("fee_rates.txt"))) {
                for (FeeRate rate : rates) {
                    writer.write(rate.toString());
                    writer.newLine();
                }
            } catch (IOException e) {
            }
        }

        private void manageRooms() {
            OUTER:
            while (true) {
                System.out.println("Manage Rooms:");
                System.out.println("1. Add Room");
                System.out.println("2. Update Room Status");
                System.out.println("3. Update Fee Rate for Room Type");
                System.out.println("4. Delete Room");
                System.out.println("5. Restore Room");
                System.out.println("6. Delete All Rooms");
                System.out.println("7. Restore All Rooms");
                System.out.println("8. Return to main menu");
                System.out.print("Enter your choice (1-8): ");
                int choice = getValidatedChoice(scanner, 1, 8);
                switch (choice) {
                    case 1 -> addRoom(scanner);
                    case 2 -> updateRoomStatus(scanner);
                    case 3 -> updateRoomType(scanner);
                    case 4 -> deleteRoom(scanner);
                    case 5 -> restoreRoom(scanner);
                    case 6 -> deleteAllRooms(scanner);
                    case 7 -> restoreAllRooms(scanner);
                    case 8 -> {
                        break OUTER;
                    }
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
            displayMenu();
        }
        
        private void addRoom(Scanner scanner) {
            List<FeeRate> feeRates = new ArrayList<>();
            List<Room> rooms = new ArrayList<>();
            try {
                feeRates = readRatesFromFile("fee_rates.txt");
                rooms = readRoomsFromFile("rooms.txt");
            } catch (IOException e) {
                return;
            }
        
            String roomId = "RM" + String.format("%02d", rooms.size() + 1);
            int roomNumber = 101 + rooms.size();
        
            System.out.println("Available Room Types:");
            List<String> roomTypes = feeRates.stream()
                    .map(FeeRate::getRoomType)
                    .distinct()
                    .collect(Collectors.toList());
            for (int i = 0; i < roomTypes.size(); i++) {
                System.out.println((i + 1) + ". " + roomTypes.get(i));
            }
        
            System.out.print("Enter the number of the room type to use: ");
            int roomTypeChoice = getValidatedChoice(scanner, 1, roomTypes.size());
            String selectedRoomType = roomTypes.get(roomTypeChoice - 1);
        
            FeeRate selectedFeeRate = feeRates.stream()
                    .filter(rate -> rate.getRoomType().equalsIgnoreCase(selectedRoomType))
                    .findFirst()
                    .orElse(null);
        
            if (selectedFeeRate == null) {
                System.out.println("No fee rate found for the selected room type.");
                return;
            }
        
            int roomCapacity;
            switch (selectedRoomType.toLowerCase()) {
                case "standard" -> roomCapacity = 1;
                case "large" -> roomCapacity = 3;
                case "family" -> roomCapacity = 6;
                default -> {
                    System.out.println("Invalid room type.");
                    return;
                }
            }
        
            Room newRoom = new Room(roomId, selectedFeeRate.getFeeRateID(), selectedRoomType, roomNumber, "available", roomCapacity, true);
        
            
            System.out.println("Room Details:");
            System.out.println("Room ID: " + newRoom.getRoomID());
            System.out.println("Fee Rate ID: " + newRoom.getFeeRateID());
            System.out.println("Room Type: " + newRoom.getRoomType());
            System.out.println("Room Number: " + newRoom.getRoomNumber());
            System.out.println("Room Status: " + newRoom.getRoomStatus());
            System.out.println("Room Capacity: " + newRoom.getRoomCapacity());
            System.out.print("Do you want to add this room? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
        
            if (confirmation.equals("yes")) {
                rooms.add(newRoom);
                saveRoomsToFile(rooms);
                System.out.println("Room added successfully.");
            } else {
                System.out.println("Room addition cancelled.");
            }
        
            System.out.print("Do you want to add another room? (yes/no): ");
            String addMore = scanner.nextLine();
            if (addMore.equalsIgnoreCase("yes")) {
                addRoom(scanner);
            }
        }
        
        private void updateRoomStatus(Scanner scanner) {
            List<Room> rooms = readRoomsFromFile("rooms.txt");
        
            System.out.println("Existing Rooms:");
            for (int i = 0; i < rooms.size(); i++) {
                System.out.println((i + 1) + ". " + rooms.get(i));
            }
            System.out.print("Enter the number of the room to update: ");
            int roomChoice = getValidatedChoice(scanner, 1, rooms.size());
            Room roomToUpdate = rooms.get(roomChoice - 1);
        
            System.out.println("Current Room Details:");
            System.out.println("Room ID: " + roomToUpdate.getRoomID());
            System.out.println("Fee Rate ID: " + roomToUpdate.getFeeRateID());
            System.out.println("Room Type: " + roomToUpdate.getRoomType());
            System.out.println("Room Number: " + roomToUpdate.getRoomNumber());
            System.out.println("Room Status: " + roomToUpdate.getRoomStatus());
            System.out.println("Room Capacity: " + roomToUpdate.getRoomCapacity());
        
            String newStatus = roomToUpdate.getRoomStatus().equals("available") ? "unavailable" : "available";
            roomToUpdate.setRoomStatus(newStatus);
            System.out.println("Room status updated successfully to " + newStatus + ".");
        
            
            System.out.print("Do you want to save the changes? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
        
            if (confirmation.equals("yes")) {
                saveRoomsToFile(rooms);
                System.out.println("Room updated successfully.");
            } else {
                System.out.println("Room update cancelled.");
            }
        }
        
        private void updateRoomType(Scanner scanner) {
            List<FeeRate> feeRates = new ArrayList<>();
            List<Room> rooms = new ArrayList<>();
            try {
                feeRates = readRatesFromFile("fee_rates.txt");
                rooms = readRoomsFromFile("rooms.txt");
            } catch (IOException e) {
                return;
            }
        
            System.out.println("Available Room Types:");
            List<String> roomTypes = feeRates.stream()
                    .map(FeeRate::getRoomType)
                    .distinct()
                    .collect(Collectors.toList());
            for (int i = 0; i < roomTypes.size(); i++) {
                System.out.println((i + 1) + ". " + roomTypes.get(i));
            }
        
            System.out.print("Enter the number of the room type to update: ");
            int roomTypeChoice = getValidatedChoice(scanner, 1, roomTypes.size());
            String selectedRoomType = roomTypes.get(roomTypeChoice - 1);
        
            
            String currentFeeRateID = rooms.stream()
                    .filter(room -> room.getRoomType().equalsIgnoreCase(selectedRoomType))
                    .map(Room::getFeeRateID)
                    .findFirst()
                    .orElse(null);
        
            System.out.println("Current Fee Rate ID for " + selectedRoomType + ": " + currentFeeRateID);
        
            System.out.println("Available Fee Rates for " + selectedRoomType + ":");
            List<FeeRate> selectedFeeRates = feeRates.stream()
                    .filter(rate -> rate.getRoomType().equalsIgnoreCase(selectedRoomType) && !rate.getFeeRateID().equals(currentFeeRateID) && rate.getIsActive())
                    .collect(Collectors.toList());
            for (int i = 0; i < selectedFeeRates.size(); i++) {
                System.out.println((i + 1) + ". " + selectedFeeRates.get(i));
            }
        
            if (selectedFeeRates.size() < 1) {
                System.out.println("Not enough fee rates available for this room type. You can add fee rates in another section of the main menu.");
                return;
            }
        
            System.out.print("Enter the number of the fee rate to use: ");
            int feeRateChoice = getValidatedChoice(scanner, 1, selectedFeeRates.size());
            FeeRate selectedFeeRate = selectedFeeRates.get(feeRateChoice - 1);
        
            for (Room room : rooms) {
                if (room.getRoomType().equalsIgnoreCase(selectedRoomType)) {
                    room.setFeeRateID(selectedFeeRate.getFeeRateID());
                }
            }
        
            
            System.out.print("Do you want to save the changes? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
        
            if (confirmation.equals("yes")) {
                saveRoomsToFile(rooms);
                System.out.println("Rooms updated successfully.");
            } else {
                System.out.println("Room type update cancelled.");
            }
        }
        
        
        private void deleteRoom(Scanner scanner) {
            List<Room> rooms = readRoomsFromFile("rooms.txt");
        
            System.out.println("Existing Active and Available Rooms:");
            List<Room> deletableRooms = new ArrayList<>();
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i).isActive() && rooms.get(i).getRoomStatus().equalsIgnoreCase("available")) {
                    deletableRooms.add(rooms.get(i));
                    System.out.println((deletableRooms.size()) + ". " + rooms.get(i));
                }
            }
        
            if (deletableRooms.isEmpty()) {
                System.out.println("No active and available rooms to delete.");
                return;
            }
        
            System.out.print("Enter the number of the room to delete: ");
            int roomChoice = getValidatedChoice(scanner, 1, deletableRooms.size());
            Room roomToDelete = deletableRooms.get(roomChoice - 1);
        
            System.out.print("Are you sure you want to delete this room? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                roomToDelete.setActive(false);
                saveRoomsToFile(rooms);
                System.out.println("Room deleted successfully.");
            } else {
                System.out.println("Room deletion cancelled.");
            }
        }
        
        private void restoreRoom(Scanner scanner) {
            List<Room> rooms = readRoomsFromFile("rooms.txt");
        
            System.out.println("Existing Inactive Rooms:");
            List<Room> inactiveRooms = new ArrayList<>();
            for (int i = 0; i < rooms.size(); i++) {
                if (!rooms.get(i).isActive()) {
                    inactiveRooms.add(rooms.get(i));
                    System.out.println((inactiveRooms.size()) + ". " + rooms.get(i));
                }
            }
        
            if (inactiveRooms.isEmpty()) {
                System.out.println("No inactive rooms available.");
                return;
            }
        
            System.out.print("Enter the number of the room to restore: ");
            int roomChoice = getValidatedChoice(scanner, 1, inactiveRooms.size());
            Room roomToRestore = inactiveRooms.get(roomChoice - 1);
        
            System.out.print("Are you sure you want to restore this room? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                roomToRestore.setActive(true);
                saveRoomsToFile(rooms);
                System.out.println("Room restored successfully.");
            } else {
                System.out.println("Room restoration cancelled.");
            }
        }
        
        private void deleteAllRooms(Scanner scanner) {
            List<Room> rooms = readRoomsFromFile("rooms.txt");
        
            System.out.println("Existing Active and Available Rooms:");
            List<Room> deletableRooms = new ArrayList<>();
            for (Room room : rooms) {
                if (room.isActive() && room.getRoomStatus().equalsIgnoreCase("available")) {
                    deletableRooms.add(room);
                    System.out.println(deletableRooms.size() + ". " + room);
                }
            }
        
            if (deletableRooms.isEmpty()) {
                System.out.println("No active and available rooms to delete.");
                return;
            }
        
            System.out.print("Are you sure you want to delete all active and available rooms? This action cannot be undone. You can restore all rooms from the menu. (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                for (Room room : deletableRooms) {
                    room.setActive(false);
                }
                saveRoomsToFile(rooms);
                System.out.println("All active and available rooms deleted successfully.");
            } else {
                System.out.println("Delete all rooms cancelled.");
            }
        }
        
        private void restoreAllRooms(Scanner scanner) {
            List<Room> rooms = readRoomsFromFile("rooms.txt");
        
            System.out.println("Existing Inactive Rooms:");
            List<Room> inactiveRooms = new ArrayList<>();
            for (Room room : rooms) {
                if (!room.isActive()) {
                    inactiveRooms.add(room);
                    System.out.println(inactiveRooms.size() + ". " + room);
                }
            }
        
            if (inactiveRooms.isEmpty()) {
                System.out.println("No inactive rooms available.");
                return;
            }
        
            System.out.print("Are you sure you want to restore all inactive rooms? This action will restore the following rooms: ");
            System.out.print("(yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                for (Room room : inactiveRooms) {
                    room.setActive(true);
                }
                saveRoomsToFile(rooms);
                System.out.println("All inactive rooms restored successfully.");
            } else {
                System.out.println("Restore all rooms cancelled.");
            }
        }

        //Method to read rooms from file
        static List<Room> readRoomsFromFile(String filename) {
            List<Room> rooms = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        Room room = new Room(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[6]));
                        rooms.add(room);
                    }
                }
            } catch (IOException e) {
            }
            return rooms;
        }

        //Method to save rooms to file
        static void saveRoomsToFile(List<Room> rooms) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
                for (Room room : rooms) {
                    writer.write(room.toString());
                    writer.newLine();
                }
                System.out.println("Rooms updated successfully.");
            } catch (IOException e) {
            }
        }

        private int getValidatedChoice(Scanner scanner, int min, int max) {
            int choice = -1;
            while (choice < min || choice > max) {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); 
                    if (choice < min || choice > max) {
                        System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                    scanner.nextLine(); 
                }
            }
            return choice;
        }
        
        public void logout() {
            
            this.userID = null;
            this.icPassportNumber = null;
            this.username = null;
            this.password = null;
            this.contactNumber = null;
            this.dateOfRegistration = null;
            this.role = null;
            this.isActive = false;
            this.managerID = null;
            this.dateOfApproval = null;
        }
    }

    
    public static class Staff extends User {
        private String staffID;
        private String dateOfApproval;

        public Staff(String staffID, String userID, String icPassportNumber, String username, String password, String contactNumber, String dateOfRegistration, String role, boolean isActive, String dateOfApproval) {
            super(userID, icPassportNumber, username, password, contactNumber, dateOfRegistration, role, isActive);
            this.staffID = staffID;
            this.dateOfApproval = dateOfApproval;
        }

        public Staff(String[] parts) {
            super(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], Boolean.parseBoolean(parts[7]));
            this.dateOfApproval = parts[5];
        }

        @Override
        public String toString() {
            return "UserID: " + getUserID() + ", IC/Passport Number: " + getIcPassportNumber() + ", Username: " + getUsername() + ", Contact Number: " + getContactNumber() + ", Date of Registration: " + getDateOfRegistration() + ", Role: " + getRole() + ", IsActive: " + getIsActive() + ", Date of Approval: " + dateOfApproval;
        }

        public String getStaffID() {
            return staffID;
        }

        public void setStaffID(String staffID) {
            this.staffID = staffID;
        }

        public String getDateOfApproval() {
            return dateOfApproval;
        }

        public void setDateOfApproval(String dateOfApproval) {
            this.dateOfApproval = dateOfApproval;
        }

        public void saveToStaffFile(String staffID, String userID, String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(staffID + "," + userID + "," + icPassportNumber + "," + username + "," + password + "," + contactNumber + "," + dateOfRegistration + "," + role + "," + isActive + "," + dateOfApproval);
                writer.newLine();
            }
        }

        public static List<Staff> readStaffsFromFile(String filename) throws IOException {
            List<Staff> staffs = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 10) {
                        staffs.add(new Staff(parts));
                    }
                }
            }
            return staffs;
        }

        
        private static final Scanner scanner = new Scanner(System.in);

        @Override
        public void displayMenu() {
            System.out.println("Staff Menu:");
            System.out.println("1. Update Personal Information");
            System.out.println("2. Make Payment for Resident");
            System.out.println("3. Generate Receipt");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
        
            int choice = getValidatedChoice(scanner, 1, 4);
        
            switch (choice) {
                case 1 -> updatePersonalInformation();
                case 2 -> makePayment(); 
                case 3 -> generateReceipt(dateOfApproval, dateOfApproval); 
                case 4 -> {
                    System.out.println("Logging out...");
                    System.out.println("You have been logged out successfully.");

                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            displayMenu();
        }

        public void updatePersonalInformation() {
            int choice;
        
            do {
                System.out.println("Update Personal Information:");
                System.out.println("1. Update IC Passport Number");
                System.out.println("2. Update Username");
                System.out.println("3. Update Password");
                System.out.println("4. Update Contact Number");
                System.out.println("0. Go Back to Staff Menu");
                System.out.print("Enter your choice: ");
                choice = getValidatedChoice(scanner, 0, 4);
        
                switch (choice) {
                    case 1 -> {
                        System.out.println("Current IC Passport Number: " + this.icPassportNumber);
                        while (true) {
                            System.out.print("Enter new IC Passport Number: ");
                            String newIcPassportNumber = scanner.nextLine();
                            String error = validateUpdateICPassport(newIcPassportNumber, this.icPassportNumber);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.icPassportNumber = newIcPassportNumber;
                            System.out.println("IC Passport Number updated successfully.");
                            break;
                        }
                    }
                    case 2 -> {
                        System.out.println("Current Username: " + this.username);
                        while (true) {
                            System.out.print("Enter new username: ");
                            String newUsername = scanner.nextLine();
                            String error = validateUpdateUsername(newUsername, this.username);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.username = newUsername;
                            System.out.println("Username updated successfully.");
                            break;
                        }
                    }
                    case 3 -> {
                        System.out.println("Current Password: " + this.password);
                        while (true) {
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();
                            String error = validateUpdatePassword(newPassword, this.username);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.password = newPassword;
                            System.out.println("Password updated successfully.");
                            break;
                        }
                    }
                    case 4 -> {
                        System.out.println("Current Contact Number: " + this.contactNumber);
                        while (true) {
                            System.out.print("Enter new contact number: ");
                            String newContactNumber = scanner.nextLine();
                            String error = validateUpdateContactNumber(newContactNumber, this.contactNumber);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.contactNumber = newContactNumber;
                            System.out.println("Contact number updated successfully.");
                            break;
                        }
                    }
                    case 0 -> {
                        System.out.println("Returning to Staff Menu...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
        
                try {
                    updateFile("approved_staffs.txt");
                    updateFile("users.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (choice != 0);
        }
        
        
        private void updateFile(String filename) throws IOException {
            List<User> users = User.readFromFile(filename);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    if (user.getUserID().equals(this.userID)) {
                        if (filename.equals("users.txt")) {
                            writer.write(this.userID + "," + this.icPassportNumber + "," + this.username + "," + this.password + "," + this.contactNumber + "," + this.dateOfRegistration + "," + this.role + "," + this.isActive);
                        } else if (user instanceof Staff staff) {
                            writer.write(staff.getStaffID() + "," + this.userID + "," + this.icPassportNumber + "," + this.username + "," + this.password + "," + this.contactNumber + "," + this.dateOfRegistration + "," + this.role + "," + this.isActive + "," + staff.getDateOfApproval());
                        } else if (user instanceof Resident resident) {
                            writer.write(resident.getResidentID() + "," + this.userID + "," + this.icPassportNumber + "," + this.username + "," + this.password + "," + this.contactNumber + "," + this.dateOfRegistration + "," + this.role + "," + this.isActive + "," + resident.getDateOfApproval());
                        }
                    } else {
                        if (filename.equals("users.txt")) {
                            writer.write(user.getUserID() + "," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole() + "," + user.getIsActive());
                        } else if (user instanceof Staff staff) {
                            writer.write(staff.getStaffID() + "," + staff.getUserID() + "," + staff.getIcPassportNumber() + "," + staff.getUsername() + "," + staff.getPassword() + "," + staff.getContactNumber() + "," + staff.getDateOfRegistration() + "," + staff.getRole() + "," + staff.getIsActive() + "," + staff.getDateOfApproval());
                        } else if (user instanceof Resident resident) {
                            writer.write(resident.getResidentID() + "," + resident.getUserID() + "," + resident.getIcPassportNumber() + "," + resident.getUsername() + "," + resident.getPassword() + "," + resident.getContactNumber() + "," + resident.getDateOfRegistration() + "," + resident.getRole() + "," + resident.getIsActive() + "," + resident.getDateOfApproval());
                        }
                    }
                    writer.newLine();
                }
            }
        }

        public void makePayment() {
            List<String[]> payments = new ArrayList<>();
        
            
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payments.add(line.split(","));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        
            
            System.out.println("Pending Payments:");
            List<String[]> pendingPayments = new ArrayList<>();
            for (String[] payment : payments) {
                if (payment[7].equalsIgnoreCase("pending")) {
                    pendingPayments.add(payment);
                    System.out.println(pendingPayments.size() + ". Payment ID: " + payment[0] + ", Resident ID: " + payment[1] + ", Amount: " + payment[6]);
                }
            }
        
            if (pendingPayments.isEmpty()) {
                System.out.println("No pending payments found.");
                return;
            }
        
            
            System.out.print("Enter the number of the payment to update: ");
            int paymentIndex = getValidatedChoice(scanner, 1, pendingPayments.size()) - 1;
        
            
            String[] selectedPayment = pendingPayments.get(paymentIndex);
            System.out.println("Selected Payment Details:");
            System.out.println("Payment ID: " + selectedPayment[0]);
            System.out.println("Resident ID: " + selectedPayment[1]);
            System.out.println("Staff ID: " + selectedPayment[2]);
            System.out.println("Start Date: " + selectedPayment[3]);
            System.out.println("End Date: " + selectedPayment[4]);
            System.out.println("Room ID: " + selectedPayment[5]);
            System.out.println("Payment Amount: " + selectedPayment[6]);
            System.out.println("Payment Status: " + selectedPayment[7]);
            System.out.println("Booking DateTime: " + selectedPayment[8]);
            System.out.println("Payment Method: " + selectedPayment[9]);
            System.out.println("Booking Status: " + selectedPayment[10]);
        
            
            String confirmation = "";
            while (!confirmation.equalsIgnoreCase("yes") && !confirmation.equalsIgnoreCase("no")) {
                System.out.print("Do you want to update this payment? (yes/no): ");
                confirmation = scanner.nextLine();
                if (!confirmation.equalsIgnoreCase("yes") && !confirmation.equalsIgnoreCase("no")) {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        
            if (confirmation.equalsIgnoreCase("no")) {
                System.out.println("Payment update cancelled.");
                return;
            }
        
            
            selectedPayment[2] = this.staffID; 
            selectedPayment[7] = "paid"; 
        
            
            List<Room> rooms = readRoomsFromFile("rooms.txt");
            for (Room room : rooms) {
                if (room.getRoomID().equals(selectedPayment[5])) {
                    room.setRoomStatus("available");
                    break;
                }
            }
            saveRoomsToFile(rooms);
        
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt"))) {
                for (String[] payment : payments) {
                    writer.write(String.join(",", payment));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            System.out.println("Payment updated successfully.");
        }

        private static List<Room> readRoomsFromFile(String filename) {
            List<Room> rooms = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        Room room = new Room(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[6]));
                        rooms.add(room);
                    }
                }
            } catch (IOException e) {
            }
            return rooms;
        }
        
        private static void saveRoomsToFile(List<Room> rooms) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
                for (Room room : rooms) {
                    writer.write(room.toString());
                    writer.newLine();
                }
                System.out.println("Rooms updated successfully.");
            } catch (IOException e) {
            }
        }
               
        //Method to process pending payment
        public static boolean processPendingPayment(String paymentId, String staffId) {
            List<String[]> payments = new ArrayList<>();
            boolean success = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payments.add(line.split(","));
                }
            } catch (IOException e) {
                return false;
            }
    
            
            String roomId = null;
            for (String[] payment : payments) {
                if (payment[0].equals(paymentId)) {
                    payment[2] = staffId;  
                    payment[7] = "paid";  
                    roomId = payment[5]; 
                    success = true;
                    break;
                }
            }
    
            if (success) {
                
                List<Room> rooms = readRoomsFromFile("rooms.txt");
                for (Room room : rooms) {
                    if (room.getRoomID().equals(roomId)) {
                        room.setRoomStatus("available");
                        break;
                    }
                }
    
                try {
                    
                    saveRoomsToFile(rooms);
            
                    
                    BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt"));
                    for (String[] payment : payments) {
                        writer.write(String.join(",", payment));
                        writer.newLine();
                    }
                    writer.close();
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }

            return false;
        }

        //Method to generate receipt
        public static boolean generateReceipt(String paymentId, String staffId) {
            List<String[]> receipts = new ArrayList<>();
            boolean success = false;
            
            
            try (BufferedReader reader = new BufferedReader(new FileReader("receipts.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    receipts.add(line.split(","));
                }
            } catch (IOException e) {
                
            }
    
            
            String receiptID = "RC" + String.format("%02d", receipts.size() + 1);
            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String[] newReceipt = {receiptID, paymentId, staffId, currentDateTime};
    
            try {
                
                BufferedWriter writer = new BufferedWriter(new FileWriter("receipts.txt", true));
                writer.write(String.join(",", newReceipt));
                writer.newLine();
                writer.close();
    
                
                success = updatePaymentToCompleted(paymentId);
            } catch (IOException e) {
                return false;
            }
    
            return success;
        }
    
        private static boolean updatePaymentToCompleted(String paymentId) {
            List<String[]> payments = new ArrayList<>();
            boolean updated = false;
    
            try {
                
                BufferedReader reader = new BufferedReader(new FileReader("payments.txt"));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] payment = line.split(",");
                    if (payment[0].equals(paymentId)) {
                        payment[10] = "completed";
                        updated = true;
                    }
                    payments.add(payment);
                }
                reader.close();
    
                
                BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt"));
                for (String[] payment : payments) {
                    writer.write(String.join(",", payment));
                    writer.newLine();
                }
                writer.close();
            } catch (IOException e) {
                return false;
            }
    
            return updated;
        }

        private int getValidatedChoice(Scanner scanner, int min, int max) {
            int choice = -1;
            while (choice < min || choice > max) {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); 
                    if (choice < min || choice > max) {
                        System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                    scanner.nextLine(); 
                }
            }
            return choice;
        }

        public void logout() {
            
            this.userID = null;
            this.icPassportNumber = null; 
            this.username = null;
            this.password = null;
            this.contactNumber = null;
            this.dateOfRegistration = null;
            this.role = null;
            this.isActive = false;
            this.staffID = null;
            this.dateOfApproval = null;
        }
    }

    
    public static class Resident extends User {
        private String residentID;
        private String dateOfApproval;

        public Resident(String residentID, String userID, String icPassportNumber, String username, String password, String contactNumber, String dateOfRegistration, String role, boolean isActive, String dateOfApproval) {
            super(userID, icPassportNumber, username, password, contactNumber, dateOfRegistration, role, isActive);
            this.residentID = residentID;
            this.dateOfApproval = dateOfApproval;
        }

        public Resident(String[] parts) {
            super(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], Boolean.parseBoolean(parts[7]));
            this.dateOfApproval = parts[5];
        }

        @Override
        public String toString() {
            return "UserID: " + getUserID() + ", IC/Passport Number: " + getIcPassportNumber() + ", Username: " + getUsername() + ", Contact Number: " + getContactNumber() + ", Date of Registration: " + getDateOfRegistration() + ", Role: " + getRole() + ", IsActive: " + getIsActive() + ", Date of Approval: " + dateOfApproval;
        }

        public String getResidentID() {
            return residentID;
        }

        public void setResidentID(String residentID) {
            this.residentID = residentID;
        }

        public String getDateOfApproval() {
            return dateOfApproval;
        }

        public void setDateOfApproval(String dateOfApproval) {
            this.dateOfApproval = dateOfApproval;
        }

        public void saveToResidentFile(String residentID, String userID, String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(residentID + "," + userID + "," + icPassportNumber + "," + username + "," + password + "," + contactNumber + "," + dateOfRegistration + "," + role + "," + isActive + "," + dateOfApproval);
                writer.newLine();
            }
        }
		
		public static List<Resident> readResidentsFromFile(String filename) throws IOException {
            List<Resident> residents = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 10) {
                        residents.add(new Resident(parts));
                    }
                }
            }
            return residents;
        }

        
        private static final Scanner scanner = new Scanner(System.in);

        @Override
        public void displayMenu() {
            
            System.out.println("Resident Menu:");
            System.out.println("1. Update Personal Information");
            System.out.println("2. View Payment Records");
            System.out.println("3. Manage Bookings");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
        
            int choice = getValidatedChoice(scanner, 1, 4);
        
            switch (choice) {
                case 1 -> updatePersonalInformation();
                case 2 -> viewPaymentRecords();
                case 3 -> manageBookings();
                case 4 -> logout();
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    displayMenu(); 
                }
            }
            displayMenu();
        }

        public void updatePersonalInformation() {
            int choice;
        
            do {
                System.out.println("Update Personal Information:");
                System.out.println("1. Update IC Passport Number");
                System.out.println("2. Update Username");
                System.out.println("3. Update Password");
                System.out.println("4. Update Contact Number");
                System.out.println("0. Go Back to Resident Menu");
                System.out.print("Enter your choice: ");
                choice = getValidatedChoice(scanner, 0, 4);
        
                switch (choice) {
                    case 1 -> {
                        System.out.println("Current IC Passport Number: " + this.icPassportNumber);
                        while (true) {
                            System.out.print("Enter new IC Passport Number: ");
                            String newIcPassportNumber = scanner.nextLine();
                            String error = validateUpdateICPassport(newIcPassportNumber, this.icPassportNumber);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.icPassportNumber = newIcPassportNumber;
                            System.out.println("IC Passport Number updated successfully.");
                            break;
                        }
                    }
                    case 2 -> {
                        System.out.println("Current Username: " + this.username);
                        while (true) {
                            System.out.print("Enter new username: ");
                            String newUsername = scanner.nextLine();
                            String error = validateUpdateUsername(newUsername, this.username);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.username = newUsername;
                            System.out.println("Username updated successfully.");
                            break;
                        }
                    }
                    case 3 -> {
                        System.out.println("Current Password: " + this.password);
                        while (true) {
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();
                            String error = validateUpdatePassword(newPassword, this.username);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.password = newPassword;
                            System.out.println("Password updated successfully.");
                            break;
                        }
                    }
                    case 4 -> {
                        System.out.println("Current Contact Number: " + this.contactNumber);
                        while (true) {
                            System.out.print("Enter new contact number: ");
                            String newContactNumber = scanner.nextLine();
                            String error = validateUpdateContactNumber(newContactNumber, this.contactNumber);
                            if (error != null) {
                                System.out.println(error);
                                System.out.print("Do you want to try again? (yes/no): ");
                                if (!scanner.nextLine().equalsIgnoreCase("yes")) {
                                    break;
                                }
                                continue;
                            }
                            this.contactNumber = newContactNumber;
                            System.out.println("Contact number updated successfully.");
                            break;
                        }
                    }
                    case 0 -> {
                        System.out.println("Returning to Resident Menu...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
        
                try {
                    updateFile("approved_residents.txt");
                    updateFile("users.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (choice != 0);
        }
        
        private void updateFile(String filename) throws IOException {
            List<User> users = User.readFromFile(filename);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (User user : users) {
                    if (user.getUserID().equals(this.userID)) {
                        if (filename.equals("users.txt")) {
                            writer.write(this.userID + "," + this.icPassportNumber + "," + this.username + "," + this.password + "," + this.contactNumber + "," + this.dateOfRegistration + "," + this.role + "," + this.isActive);
                        } else if (user instanceof Staff staff) {
                            writer.write(staff.getStaffID() + "," + this.userID + "," + this.icPassportNumber + "," + this.username + "," + this.password + "," + this.contactNumber + "," + this.dateOfRegistration + "," + this.role + "," + this.isActive + "," + staff.getDateOfApproval());
                        } else if (user instanceof Resident resident) {
                            writer.write(resident.getResidentID() + "," + this.userID + "," + this.icPassportNumber + "," + this.username + "," + this.password + "," + this.contactNumber + "," + this.dateOfRegistration + "," + this.role + "," + this.isActive + "," + resident.getDateOfApproval());
                        }
                    } else {
                        if (filename.equals("users.txt")) {
                            writer.write(user.getUserID() + "," + user.getIcPassportNumber() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getContactNumber() + "," + user.getDateOfRegistration() + "," + user.getRole() + "," + user.getIsActive());
                        } else if (user instanceof Staff staff) {
                            writer.write(staff.getStaffID() + "," + staff.getUserID() + "," + staff.getIcPassportNumber() + "," + staff.getUsername() + "," + staff.getPassword() + "," + staff.getContactNumber() + "," + staff.getDateOfRegistration() + "," + staff.getRole() + "," + staff.getIsActive() + "," + staff.getDateOfApproval());
                        } else if (user instanceof Resident resident) {
                            writer.write(resident.getResidentID() + "," + resident.getUserID() + "," + resident.getIcPassportNumber() + "," + resident.getUsername() + "," + resident.getPassword() + "," + resident.getContactNumber() + "," + resident.getDateOfRegistration() + "," + resident.getRole() + "," + resident.getIsActive() + "," + resident.getDateOfApproval());
                        }
                    }
                    writer.newLine();
                }
            }
        }

        //Method to view payment records
        public static List<String[]> viewPaymentRecords(String residentID) {
            List<String[]> paymentRecords = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split(",");
                    if (details.length > 0 && details[1].equals(residentID) && !details[7].equals("unpaid") && !details[9].equals("null")) {
                        paymentRecords.add(details);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return paymentRecords;
        }
    
        public void viewPaymentRecords() {
            System.out.println("Payment Records:");
            String residentID = this.getResidentID(); 
        
            
            Map<String, String> roomMap = new HashMap<>();
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        roomMap.put(parts[0], parts[3]); 
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
        
            List<String[]> relevantPayments = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] details = line.split(",");
                    if (details[1].equals(residentID) && !details[7].equals("unpaid") && !details[10].equals("null")) { 
                        relevantPayments.add(details);
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the payment records.");
            }
        
            if (relevantPayments.isEmpty()) {
                System.out.println("No payment records found for your account.");
                return;
            }
        
            
            System.out.println("Choose which Payment Record to view:");
            for (int i = 0; i < relevantPayments.size(); i++) {
                String[] details = relevantPayments.get(i);
                System.out.printf("%d. Payment ID: %s, Payment Amount: RM %s, Booking Date: %s%n", i + 1, details[0], details[6], details[8]);
            }
        
            
            System.out.printf("Enter your choice (1-%d): ", relevantPayments.size());
            int choice = getValidatedChoice(scanner, 1, relevantPayments.size());
        
            
            String[] selectedDetails = relevantPayments.get(choice - 1);
            String roomNumber = roomMap.getOrDefault(selectedDetails[5], "Unknown Room"); 
            LocalDate startDate = LocalDate.parse(selectedDetails[3]); 
            LocalDate endDate = LocalDate.parse(selectedDetails[4]); 
            long stayDuration = ChronoUnit.DAYS.between(startDate, endDate);
            System.out.println("Payment ID: " + selectedDetails[0]);
            System.out.println("Payment Status: " + selectedDetails[7]);
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
            System.out.println("Stay Duration: " + stayDuration + " days");
            System.out.println("Payment Amount: " + selectedDetails[6]);
            System.out.println("Booking Date: " + selectedDetails[8]);
            System.out.println("Room Number: " + roomNumber);
            System.out.println("Payment Method: " + selectedDetails[9]);
            System.out.println("Booking Status: " + selectedDetails[10]);
            System.out.println("-----------------------------");
        }

        public static Map<String, String> getRoomMap1() {
            Map<String, String> roomMap = new HashMap<>();
            
            try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    roomMap.put(parts[0], parts[1]); 
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return roomMap;
        }

        public void manageBookings() {
            int choice;
        
            do {
                System.out.println("Manage Bookings:");
                System.out.println("1. Make Booking");
                System.out.println("2. Make Payment for Booking");
                System.out.println("3. Cancel Booking");
                System.out.println("0. Go Back to Resident Menu");
                System.out.print("Enter your choice: ");
                
                choice = getValidatedChoice(scanner, 0, 3);
        
                switch (choice) {
                    case 1 -> makeBooking();
                    case 2 -> makePaymentForBooking();
                    case 3 -> cancelBooking();
                    case 0 -> {
                        System.out.println("Returning to Resident Menu...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 0);
        }
        
        public void makePaymentForBooking() {
            List<String[]> payments = new ArrayList<>();
            String residentID = this.getResidentID(); 
        
            
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payments.add(line.split(","));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        
            
            Map<String, String> roomMap = new HashMap<>();
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        roomMap.put(parts[0], parts[3]); 
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
        
            
            List<String[]> unpaidBookings = new ArrayList<>();
            for (String[] payment : payments) {
                if (payment[1].equals(residentID) && payment[7].equals("unpaid") && !payment[10].equals("cancelled")) {
                    unpaidBookings.add(payment);
                }
            }
        
            if (unpaidBookings.isEmpty()) {
                System.out.println("No unpaid bookings found.");
                return;
            }
        
            
            System.out.println("Unpaid Bookings:");
            for (int i = 0; i < unpaidBookings.size(); i++) {
                String[] booking = unpaidBookings.get(i);
                long daysBetween = ChronoUnit.DAYS.between(LocalDate.parse(booking[3]), LocalDate.parse(booking[4]));
                String roomNumber = roomMap.getOrDefault(booking[5], "Unknown Room"); 
                System.out.printf("%d. PaymentID: %s, ResidentID: %s, Room Number: %s, Stay Duration: %d days, Payment Amount: RM %s%n", 
                                  i + 1, booking[0], booking[1], roomNumber, daysBetween, booking[6]);
            }
        
            
            System.out.printf("Enter the number of the booking to pay for (1-%d): ", unpaidBookings.size());
            int bookingIndex = getValidatedChoice(scanner, 1, unpaidBookings.size()) - 1;
        
            
            String[] selectedBooking = unpaidBookings.get(bookingIndex);
            System.out.println("Payment Details:");
            System.out.println("PaymentID: " + selectedBooking[0]);
            System.out.println("ResidentID: " + selectedBooking[1]);
            System.out.println("StaffID: " + selectedBooking[2]);
            System.out.println("Start Date: " + selectedBooking[3]);
            System.out.println("End Date: " + selectedBooking[4]);
            System.out.println("Stay Duration: " + ChronoUnit.DAYS.between(LocalDate.parse(selectedBooking[3]), LocalDate.parse(selectedBooking[4])) + " days");
            System.out.println("RoomID: " + selectedBooking[5]);
            System.out.println("Payment Amount: " + selectedBooking[6]);
            System.out.println("Payment Status: " + selectedBooking[7]);
            System.out.println("Booking Date and Time: " + selectedBooking[8]);
            System.out.println("Payment Method: " + selectedBooking[9]);
            System.out.println("Booking Status: " + selectedBooking[10]);
            System.out.println("=====================");
        
            
            String paymentMethod = "";
            while (true) {
                System.out.println("Select Payment Method:");
                System.out.println("1. Credit Card");
                System.out.println("2. Bank Transfer");
                System.out.println("3. Cash");
                System.out.print("Enter your choice: ");
                int paymentMethodChoice = getValidatedChoice(scanner, 1, 3);
        
                switch (paymentMethodChoice) {
                    case 1 -> paymentMethod = "credit_card";
                    case 2 -> paymentMethod = "bank_transfer";
                    case 3 -> paymentMethod = "cash";
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                    }
                }
                break;
            }
        
            
            String confirmation = "";
            while (!confirmation.equalsIgnoreCase("yes") && !confirmation.equalsIgnoreCase("no")) {
                System.out.print("Do you want to proceed with the payment? (yes/no): ");
                confirmation = scanner.nextLine();
                if (!confirmation.equalsIgnoreCase("yes") && !confirmation.equalsIgnoreCase("no")) {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Payment cancelled.");
                return;
            }
        
            
            selectedBooking[7] = "pending"; 
            selectedBooking[9] = paymentMethod; 
        
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt"))) {
                for (String[] payment : payments) {
                    writer.write(String.join(",", payment));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            System.out.println("Your payment is successful.");
        }

        //Method to filter unpaid bookings for a resident
        public static List<String[]> getUnpaidBookingsForResident(String residentID) {
            List<String[]> payments = new ArrayList<>();
            List<String[]> unpaidBookings = new ArrayList<>();
    
            
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payments.add(line.split(","));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return unpaidBookings;
            }
    
            
            for (String[] payment : payments) {
                if (payment[1].equals(residentID) && payment[7].equals("unpaid") && !payment[10].equals("cancelled")) {
                    unpaidBookings.add(payment);
                }
            }
    
            return unpaidBookings;
        }
    
        // Helper method to get the room map    
        public static Map<String, String> getRoomMap() {
            Map<String, String> roomMap = new HashMap<>();
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        roomMap.put(parts[0], parts[3]); 
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
            return roomMap;
        }

        // Helper method to get the room type based on the room ID
        public static String getRoomType(String roomID) {
            try (BufferedReader br = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(roomID)) {
                        return parts[2]; 
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Unknown Room Type";
        }

        //Method to update the payment status and method
        public static boolean updatePaymentStatusAndMethod(String paymentID, String paymentMethod) {
            List<String[]> payments = new ArrayList<>();
    
            
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payments.add(line.split(","));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
    
            
            for (String[] payment : payments) {
                if (payment[0].equals(paymentID)) {
                    payment[7] = "pending"; 
                    payment[9] = paymentMethod; 
                    break;
                }
            }
    
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt"))) {
                for (String[] payment : payments) {
                    writer.write(String.join(",", payment));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
    
            return true;
        }

        public void cancelBooking() {
            List<String[]> payments = new ArrayList<>();
            String residentID = this.getResidentID(); 
        
            
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payments.add(line.split(","));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        
            
            List<String[]> unpaidBookings = new ArrayList<>();
            for (String[] payment : payments) {
                if (payment[1].equals(residentID) && payment[7].equals("unpaid") && !payment[10].equals("cancelled")) {
                    unpaidBookings.add(payment);
                }
            }
        
            if (unpaidBookings.isEmpty()) {
                System.out.println("No unpaid bookings found.");
                return;
            }
        
            
            System.out.println("Unpaid Bookings:");
            for (int i = 0; i < unpaidBookings.size(); i++) {
                String[] booking = unpaidBookings.get(i);
                long daysBetween = ChronoUnit.DAYS.between(LocalDate.parse(booking[3]), LocalDate.parse(booking[4]));
                System.out.printf("Booking %d: Payment ID: %s, Room Number: %s, Stay Duration: %d Days, Payment Amount: RM %s, Booking DateTime: %s%n",
                        i + 1, booking[0], booking[5], daysBetween, booking[6], booking[8]);
            }
        
            
            System.out.print("Enter the number of the booking to cancel: ");
            int bookingIndex = getValidatedChoice(scanner, 1, unpaidBookings.size()) - 1;
        
            
            String[] selectedBooking = unpaidBookings.get(bookingIndex);
            System.out.println("Selected Booking Details:");
            System.out.println("Payment ID: " + selectedBooking[0]);
            System.out.println("Resident ID: " + selectedBooking[1]);
            System.out.println("Staff ID: " + selectedBooking[2]);
            System.out.println("Start Date: " + selectedBooking[3]);
            System.out.println("End Date: " + selectedBooking[4]);
            System.out.println("Stay Duration: " + ChronoUnit.DAYS.between(LocalDate.parse(selectedBooking[3]), LocalDate.parse(selectedBooking[4])) + " days");
            System.out.println("Room ID: " + selectedBooking[5]);
            System.out.println("Payment Amount: " + selectedBooking[6]);
            System.out.println("Payment Status: " + selectedBooking[7]);
            System.out.println("Booking DateTime: " + selectedBooking[8]);
            System.out.println("Payment Method: " + selectedBooking[9]);
            System.out.println("Booking Status: " + selectedBooking[10]);
        
            
            String confirmation = "";
            while (!confirmation.equalsIgnoreCase("yes") && !confirmation.equalsIgnoreCase("no")) {
                System.out.print("Do you want to proceed with the cancellation? (yes/no): ");
                confirmation = scanner.nextLine();
                if (!confirmation.equalsIgnoreCase("yes") && !confirmation.equalsIgnoreCase("no")) {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        
            if (!confirmation.equalsIgnoreCase("yes")) {
                System.out.println("Cancellation cancelled.");
                return;
            }
        
            
            selectedBooking[10] = "cancelled"; 
        
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt"))) {
                for (String[] payment : payments) {
                    writer.write(String.join(",", payment));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            
            String roomID = selectedBooking[5]; 
            List<String[]> rooms = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(roomID)) {
                        parts[4] = "available"; 
                    }
                    rooms.add(parts);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
        
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
                for (String[] room : rooms) {
                    writer.write(String.join(",", room));
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while updating the room data.");
            }
        
            System.out.println("This booking has been successfully cancelled.");
        }
        
        // Method to get the list of cancellable bookings for a resident
        public static List<String[]> getCancellableBookingsForResident(String residentID) {
            List<String[]> payments = new ArrayList<>();
            List<String[]> cancellableBookings = new ArrayList<>();

            
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    payments.add(line.split(","));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return cancellableBookings;
            }

            
            for (String[] payment : payments) {
                if (payment[1].equals(residentID) && payment[7].equals("unpaid") && !payment[10].equals("cancelled")) {
                    cancellableBookings.add(payment);
                }
            }

            return cancellableBookings;
        }

        // Method to update the booking status to cancelled
        public static boolean cancelBooking(String paymentID) {
            List<String[]> payments = new ArrayList<>();
            boolean bookingCancelled = false;

            
            try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] payment = line.split(",");
                    if (payment[0].equals(paymentID)) {
                        payment[10] = "cancelled"; 
                        bookingCancelled = true;
                    }
                    payments.add(payment);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt"))) {
                for (String[] payment : payments) {
                    writer.write(String.join(",", payment));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return bookingCancelled;
        }

        // Method to update the room status to available after a booking is cancelled
        public static void updateRoomStatus1(String roomID, String status) {
            List<String[]> rooms = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(roomID)) {
                        parts[4] = status; 
                    }
                    rooms.add(parts);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
                for (String[] room : rooms) {
                    writer.write(String.join(",", room));
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while updating the room data.");
            }
        }

        public void makeBooking() {
            
            displayRoomPricing();
        
            System.out.println("Room Pricing");
            System.out.println("Room Type\t\tCapacity");
            System.out.println("1. Standard\t\t1");
            System.out.println("2. Large\t\t3");
            System.out.println("3. Family\t\t6");
            System.out.print("Enter your choice: ");
            int roomTypeChoice = getValidatedChoice(scanner, 1, 3);
        
            String roomType = null;
            switch (roomTypeChoice) {
                case 1 -> roomType = "standard";
                case 2 -> roomType = "large";
                case 3 -> roomType = "family";
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    return;
                }
            }
        
            
            String roomID = selectAvailableRoomByType(roomType);
            if (roomID == null) {
                System.out.println("No available rooms of the selected type.");
                return;
            }
        
            
            String feeRateID = null;
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(roomID)) {
                        feeRateID = parts[1]; 
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
                return;
            }
        
            LocalDate startDate = null;
            LocalDate endDate = null;
            String datePattern = "\\d{4}-\\d{2}-\\d{2}";
            LocalDate currentDate = LocalDate.now();
        
            
            while (startDate == null) {
                System.out.print("Enter start date of your stay (yyyy-MM-dd): ");
                String startDateInput = scanner.nextLine();
                if (startDateInput.matches(datePattern)) {
                    try {
                        String[] dateParts = startDateInput.split("-");
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        if (year == 0 || month == 0 || day == 0) {
                            throw new DateTimeParseException("Invalid date components", startDateInput, 0);
                        }
                        if (isInvalidDate(year, month, day)) {
                            throw new DateTimeParseException("Invalid day for the month", startDateInput, 0);
                        }
                        LocalDate parsedDate = LocalDate.of(year, month, day);
                        startDate = parsedDate;
                        if (startDate.isBefore(currentDate)) {
                            System.out.println("You cannot travel back in time. Please enter a valid start date.");
                            startDate = null;
                        }
                    } catch (DateTimeParseException | NumberFormatException e) {
                        System.out.println("This date does not exist, please input a valid date.");
                        startDate = null;
                    }
                } else {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
            }
        
            
            while (endDate == null) {
                System.out.print("Enter end date of your stay (yyyy-MM-dd): ");
                String endDateInput = scanner.nextLine();
                if (endDateInput.matches(datePattern)) {
                    try {
                        String[] dateParts = endDateInput.split("-");
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]);
                        int day = Integer.parseInt(dateParts[2]);
                        if (year == 0 || month == 0 || day == 0) {
                            throw new DateTimeParseException("Invalid date components", endDateInput, 0);
                        }
                        if (isInvalidDate(year, month, day)) {
                            throw new DateTimeParseException("Invalid day for the month", endDateInput, 0);
                        }
                        LocalDate parsedDate = LocalDate.of(year, month, day);
                        endDate = parsedDate;
                        if (!endDate.isAfter(startDate)) {
                            System.out.println("The end date must be after the start date.");
                            endDate = null;
                        }
                    } catch (DateTimeParseException | NumberFormatException e) {
                        System.out.println("This date does not exist, please input a valid date.");
                        endDate = null;
                    }
                } else {
                    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
            }
        
            
            String paymentID = generatePaymentID();
        
            
            String residentID = this.getResidentID();
        
            
            double paymentAmount = calculatePaymentAmount(startDate, endDate, feeRateID);
        
            
            String bookingDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt", true))) {
                writer.write(paymentID + "," + residentID + "," + null + "," + startDate + "," + endDate + "," + roomID + "," + paymentAmount + ",unpaid," + bookingDateTime + "," + null + ",active");
                writer.newLine();
                System.out.println("Booking successful.");
            } catch (IOException e) {
                System.out.println("An error occurred while saving the booking.");
            }
        
            
            updateRoomStatus(roomID, "unavailable");
        
            
            Map<String, String> roomMap = new HashMap<>();
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        roomMap.put(parts[0], parts[3]); 
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
        
            
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            String roomNumber = roomMap.getOrDefault(roomID, "Unknown Room");
            System.out.println("Your Booking :");
            System.out.println("Payment ID : " + paymentID);
            System.out.println("Resident ID : " + residentID);
            System.out.println("Staff ID : null");
            System.out.println("Start Date : " + startDate);
            System.out.println("End Date : " + endDate);
            System.out.println("Stay Duration : " + daysBetween + " days");
            System.out.println("Room Number : " + roomNumber);
            System.out.println("Payment Amount : RM " + paymentAmount);
            System.out.println("Payment Status : unpaid");
            System.out.println("Booking DateTime : " + bookingDateTime);
            System.out.println("Payment Method : null");
            System.out.println("Booking Status : active");
            System.out.println("=========================");
            System.out.println("Please go back to Manage Bookings to make payment for this booking.");
        }

        //Method to get room number based on room ID
        public static String getRoomNumber(String roomID) {
            String roomNumber = "Unknown Room";
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(roomID)) {
                        roomNumber = parts[3]; 
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
            return roomNumber;
        }

        //Method to get the room pricing
        public static List<String[]> getRoomPricing() {
            List<String[]> roomPricing = new ArrayList<>();
            Map<String, String> roomTypeToFeeRateID = new HashMap<>();
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        roomTypeToFeeRateID.put(parts[2].toLowerCase(), parts[1]); 
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
                return roomPricing;
            }
    
            Map<String, double[]> feeRates = new HashMap<>();
            try (BufferedReader feeRateReader = new BufferedReader(new FileReader("fee_rates.txt"))) {
                String line;
                while ((line = feeRateReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6 && parts[6].equalsIgnoreCase("true")) { 
                        String feeRateID = parts[0];
                        double dailyRate = Double.parseDouble(parts[2]);
                        double weeklyRate = Double.parseDouble(parts[3]);
                        double monthlyRate = Double.parseDouble(parts[4]);
                        double yearlyRate = Double.parseDouble(parts[5]);
                        feeRates.put(feeRateID, new double[]{dailyRate, weeklyRate, monthlyRate, yearlyRate});
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the fee rate data.");
                return roomPricing;
            }
    
            for (Map.Entry<String, String> entry : roomTypeToFeeRateID.entrySet()) {
                String roomType = entry.getKey();
                String feeRateID = entry.getValue();
                double[] rates = feeRates.get(feeRateID);
                if (rates != null) {
                    roomPricing.add(new String[]{
                            roomType.substring(0, 1).toUpperCase() + roomType.substring(1),
                            getRoomCapacity(roomType),
                            String.format("%.2f", rates[0]),
                            String.format("%.2f", rates[1]),
                            String.format("%.2f", rates[2]),
                            String.format("%.2f", rates[3])
                    });
                }
            }
            return roomPricing;
        }
    
        private static String getRoomCapacity(String roomType) {
            switch (roomType.toLowerCase()) {
                case "standard":
                    return "1";
                case "large":
                    return "3";
                case "family":
                    return "6";
                default:
                    return "Unknown";
            }
        }

        //Method to get available rooms based on room type
        public static String selectAvailableRoomByType1(String roomType) {
            List<String> availableRooms = new ArrayList<>();
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7 && parts[2].equalsIgnoreCase(roomType) && parts[4].equals("available") && Boolean.parseBoolean(parts[6])) {
                        availableRooms.add(parts[0]); 
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
    
            if (!availableRooms.isEmpty()) {
                Random random = new Random();
                return availableRooms.get(random.nextInt(availableRooms.size())); 
            }
            return null;
        }

        //Method to generate payment ID
        public static String generatePaymentID1() {
            int id = 1;
            String filename = "payments.txt";
            File file = new File(filename);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                }
            }
    
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].startsWith("P")) {
                        int currentId = Integer.parseInt(parts[0].substring(1));
                        if (currentId >= id) {
                            id = currentId + 1;
                        }
                    }
                }
            } catch (IOException e) {
            }
            return "P" + String.format("%02d", id);
        }

        //Method to get Fee Rate ID based on room ID
        public static String getFeeRateID(String roomID) {
            String feeRateID = null;
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(roomID)) {
                        feeRateID = parts[1]; 
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }
            return feeRateID;
        }

        public static boolean addBookingToFile(String paymentID, String residentID, LocalDate startDate, LocalDate endDate, String roomID, double paymentAmount, String bookingDateTime) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt", true))) {
                writer.write(paymentID + "," + residentID + "," + null + "," + startDate + "," + endDate + "," + roomID + "," + paymentAmount + ",unpaid," + bookingDateTime + "," + null + ",active");
                writer.newLine();
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred while saving the booking.");
                return false;
            }
        }

        //Method to calculate payment amount
        static double calculatePaymentAmount(LocalDate startDate, LocalDate endDate, String feeRateID) {
            long totalDays = ChronoUnit.DAYS.between(startDate, endDate); 
            System.out.println("Total days: " + totalDays);
        
            double dailyRate = 0;
            double weeklyRate = 0;
            double monthlyRate = 0;
            double yearlyRate = 0;
        
            try (BufferedReader rateReader = new BufferedReader(new FileReader("fee_rates.txt"))) {
                String line;
                while ((line = rateReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(feeRateID)) {
                        dailyRate = Double.parseDouble(parts[2]);
                        weeklyRate = Double.parseDouble(parts[3]);
                        monthlyRate = Double.parseDouble(parts[4]);
                        yearlyRate = Double.parseDouble(parts[5]);
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the fee rate data.");
            }
        
            long years = totalDays / 365;
            long remainingDaysAfterYears = totalDays % 365;
            long months = remainingDaysAfterYears / 30;
            long remainingDaysAfterMonths = remainingDaysAfterYears % 30;
            long weeks = remainingDaysAfterMonths / 7;
            long remainingDays = remainingDaysAfterMonths % 7;
            System.out.println("Years: " + years + ", Months: " + months + ", Weeks: " + weeks + ", Days: " + remainingDays);
        
            return (years * yearlyRate) + (months * monthlyRate) + (weeks * weeklyRate) + (remainingDays * dailyRate);
        }

        private void displayRoomPricing() {
            
            Map<String, String> roomTypeToFeeRateID = new HashMap<>();
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        roomTypeToFeeRateID.put(parts[2].toLowerCase(), parts[1]); 
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
                return;
            }
        
            
            Map<String, double[]> feeRates = new HashMap<>();
            try (BufferedReader feeRateReader = new BufferedReader(new FileReader("fee_rates.txt"))) {
                String line;
                while ((line = feeRateReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6 && parts[6].equalsIgnoreCase("true")) { 
                        String feeRateID = parts[0];
                        double dailyRate = Double.parseDouble(parts[2]);
                        double weeklyRate = Double.parseDouble(parts[3]);
                        double monthlyRate = Double.parseDouble(parts[4]);
                        double yearlyRate = Double.parseDouble(parts[5]);
                        feeRates.put(feeRateID, new double[]{dailyRate, weeklyRate, monthlyRate, yearlyRate});
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the fee rate data.");
                return;
            }
        
            
            System.out.println("Room Pricing:");
            System.out.printf("%-10s\t%-12s\t%-12s\t%-12s\t%-12s%n", "Room Type", "Daily Rate", "Weekly Rate", "Monthly Rate", "Yearly Rate");
            for (Map.Entry<String, String> entry : roomTypeToFeeRateID.entrySet()) {
                String roomType = entry.getKey();
                String feeRateID = entry.getValue();
                double[] rates = feeRates.get(feeRateID);
                if (rates != null) {
                    System.out.printf("%-10s\tRM %-10.2f\tRM %-10.2f\tRM %-10.2f\tRM %-10.2f%n",
                            roomType.substring(0, 1).toUpperCase() + roomType.substring(1), rates[0], rates[1], rates[2], rates[3]);
                } else {
                    System.out.printf("%-10s\tNo rates found%n", roomType);
                }
            }
        }

        private String selectAvailableRoomByType(String roomType) {
            List<String> availableRooms = new ArrayList<>();
            try (BufferedReader roomReader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = roomReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 7 && parts[2].equalsIgnoreCase(roomType) && parts[4].equals("available") && Boolean.parseBoolean(parts[6])) {
                        availableRooms.add(parts[0]); 
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }

            if (!availableRooms.isEmpty()) {
                Random random = new Random();
                return availableRooms.get(random.nextInt(availableRooms.size())); 
            }
            return null;
        }

        private boolean isInvalidDate(int year, int month, int day) {
            switch (month) {
                case 2 -> {
                    if (day > 29 || (day == 29 && !Year.isLeap(year))) {
                        return true;
                    }
                }
                case 4, 6, 9, 11 -> {
                    if (day > 30) {
                        return true;
                    }
                }
                default -> {
                    if (day > 31) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void updateRoomStatus(String roomID, String status) {
            List<String[]> rooms = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(roomID)) {
                        parts[4] = status; 
                    }
                    rooms.add(parts);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the room data.");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("rooms.txt"))) {
                for (String[] room : rooms) {
                    writer.write(String.join(",", room));
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while updating the room data.");
            }
        }

        private String generatePaymentID() {
            int id = 1;
            String filename = "payments.txt";
            File file = new File(filename);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].startsWith("P")) {
                        int currentId = Integer.parseInt(parts[0].substring(1));
                        if (currentId >= id) {
                            id = currentId + 1;
                        }
                    }
                }
            } catch (IOException e) {
            }
            return "P" + String.format("%02d", id);
        }

        public void logout() {
            this.userID = null;
            this.icPassportNumber = null;
            this.username = null;
            this.password = null;
            this.contactNumber = null;
            this.dateOfRegistration = null;
            this.role = null;
            this.isActive = false;
            this.residentID = null;
            this.dateOfApproval = null;
        }

        private int getValidatedChoice(Scanner scanner, int min, int max) {
            int choice = -1;
            while (choice < min || choice > max) {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); 
                    if (choice < min || choice > max) {
                        System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                    scanner.nextLine(); 
                }
            }
            return choice;
        }
    }

    
    public static class Payment {
        private String paymentID;
        private String residentID; 
        private String staffID;
        private double amount;
        private String bookingDate;
        private String roomNumber;
        private String paymentStatus;
    
        public Payment(String paymentID, String residentID, String staffID, 
                      double amount, String bookingDate, String roomNumber, String paymentStatus) {
            this.paymentID = paymentID;
            this.residentID = residentID;
            this.staffID = staffID;
            this.amount = amount;
            this.bookingDate = bookingDate; 
            this.roomNumber = roomNumber;
            this.paymentStatus = paymentStatus;
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

        public String getBookingDate() {
            return bookingDate;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void saveToFile(String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(paymentID + "," + residentID + "," + (staffID != null ? staffID : "NULL") + "," + amount + "," + bookingDate + "," + roomNumber + "," + (paymentStatus != null ? paymentStatus : "NULL"));
                writer.newLine();
            }
        }

        public static List<Payment> readFromFile(String filename) throws IOException {
            List<Payment> payments = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        Payment payment = new Payment(parts[0], parts[1], parts[2].equals("NULL") ? null : parts[2], Double.parseDouble(parts[3]), parts[4], parts[5], parts[6].equals("NULL") ? null : parts[6]);
                        payments.add(payment);
                    }
                }
            }
            return payments;
        }
    }

    //Fee Rate class
    public static class FeeRate {
        private String feeRateID;
        private String roomType;
        private double dailyRate;
        private double weeklyRate;
        private double monthlyRate;
        private double yearlyRate;
        private boolean isActive;
    
        // Constructor
        public FeeRate(String feeRateID, String roomType, double dailyRate, double weeklyRate, double monthlyRate, double yearlyRate, boolean isActive) {
            this.feeRateID = feeRateID;
            this.roomType = roomType;
            this.dailyRate = dailyRate;
            this.weeklyRate = weeklyRate;
            this.monthlyRate = monthlyRate;
            this.yearlyRate = yearlyRate;
            this.isActive = isActive;
        }
    
        public String getFeeRateID() {
            return feeRateID;
        }
    
        public String getRoomType() {
            return roomType;
        }

        public double getDailyRate() {
            return dailyRate;
        }

        public double getWeeklyRate() {
            return weeklyRate;
        }
    
        public double getMonthlyRate() {
            return monthlyRate;
        }

        public double getYearlyRate() {
            return yearlyRate;
        }

        public boolean getIsActive() {
            return isActive;
        }
    
        public boolean isActive() {
            return isActive;
        }
    
        public void setActive(boolean isActive) {
            this.isActive = isActive;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }
    
        public void setDailyRate(double dailyRate) {
            this.dailyRate = dailyRate;
        }
    
        public void setWeeklyRate(double weeklyRate) {
            this.weeklyRate = weeklyRate;
        }
    
        public void setMonthlyRate(double monthlyRate) {
            this.monthlyRate = monthlyRate;
        }
    
        public void setYearlyRate(double yearlyRate) {
            this.yearlyRate = yearlyRate;
        }
    
        @Override
        public String toString() {
            return feeRateID + "," + roomType + "," + dailyRate + "," + weeklyRate + "," + monthlyRate + "," + yearlyRate + "," + isActive;
        }
    
        public void saveToFile(String filename) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(toString());
                writer.newLine();
            }
        }
    
        public double calculateCost(int days) {
            int months = days / 30;
            days %= 30;
            int weeks = days / 7;
            days %= 7;
    
            double cost = (months * monthlyRate) + (weeks * weeklyRate) + (days * dailyRate);
            return cost;
        }
    
        // Read fee rates from file
        public static List<FeeRate> readFromFile(String filename) throws IOException {
            List<FeeRate> feeRates = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        FeeRate feeRate = new FeeRate(parts[0], parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Boolean.parseBoolean(parts[6]));
                        feeRates.add(feeRate);
                    }
                }
            }
            return feeRates;
        }
    }

    //Room class
    public static class Room {
        private String roomID;
        private String feeRateID;
        private String roomType;
        private int roomNumber;
        private String roomStatus;
        private int roomCapacity;
        private boolean isActive;
    
        // Constructor
        public Room(String roomID, String feeRateID, String roomType, int roomNumber, String roomStatus, int roomCapacity, boolean isActive) {
            this.roomID = roomID;
            this.feeRateID = feeRateID;
            this.roomType = roomType;
            this.roomNumber = roomNumber;
            this.roomStatus = roomStatus;
            this.roomCapacity = roomCapacity;
            this.isActive = isActive;
        }
    
        public String getRoomID() {
            return roomID;
        }
    
        public void setRoomID(String roomID) {
            this.roomID = roomID;
        }
    
        public String getFeeRateID() {
            return feeRateID;
        }
    
        public void setFeeRateID(String feeRateID) {
            this.feeRateID = feeRateID;
        }
    
        public String getRoomType() {
            return roomType;
        }
    
        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }
    
        public int getRoomNumber() {
            return roomNumber;
        }
    
        public void setRoomNumber(int roomNumber) {
            this.roomNumber = roomNumber;
        }
    
        public String getRoomStatus() {
            return roomStatus;
        }
    
        public void setRoomStatus(String roomStatus) {
            this.roomStatus = roomStatus;
        }
    
        public int getRoomCapacity() {
            return roomCapacity;
        }
    
        public void setRoomCapacity(int roomCapacity) {
            this.roomCapacity = roomCapacity;
        }
    
        public boolean isActive() {
            return isActive;
        }
    
        public void setActive(boolean active) {
            isActive = active;
        }
    
        @Override
        public String toString() {
            return roomID + "," + feeRateID + "," + roomType + "," + roomNumber + "," + roomStatus + "," + roomCapacity + "," + isActive;
        }
    }

    /**
     * Validates uniqueness of IC/Passport number, username, and contact number
     */
    public static boolean isUnique(String icPassportNumber, String username, String contactNumber) throws IOException {
        List<User> users = new ArrayList<>();
        users.addAll(User.readFromFile("users.txt"));
        users.addAll(User.readFromFile("unapproved_residents.txt"));
        users.addAll(User.readFromFile("approved_residents.txt"));
        users.addAll(User.readFromFile("unapproved_staffs.txt"));
        users.addAll(User.readFromFile("approved_staffs.txt"));
        users.addAll(User.readFromFile("approved_managers.txt"));
        users.addAll(User.readFromFile("unapproved_managers.txt"));

        for (User user : users) {
            if ((icPassportNumber != null && !icPassportNumber.isEmpty() && user.getIcPassportNumber().equals(icPassportNumber)) ||
                (username != null && !username.isEmpty() && user.getUsername().equals(username)) ||
                (contactNumber != null && !contactNumber.isEmpty() && user.getContactNumber().equals(contactNumber))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates IC/Passport number format and uniqueness
     */
    public static void validateICPassport(String icPassport) throws Exception {
        
        if (icPassport.length() == 14) {
            if (icPassport.charAt(6) != '-' || icPassport.charAt(9) != '-') {
                throw new Exception("Invalid IC format: Must contain hyphens at positions 7 and 10");
            }
            if (!icPassport.replace("-", "").matches("\\d+")) {
                throw new Exception("Invalid IC format: Must contain only numbers between hyphens");
            }
        }
        
        else if (icPassport.length() == 9) {
            if (!Character.isLetter(icPassport.charAt(0))) {
                throw new Exception("Invalid Passport format: Must start with a letter");
            }
            if (!icPassport.substring(1).matches("\\d+")) {
                throw new Exception("Invalid Passport format: Must be followed by 8 numbers");
            }
        } else {
            throw new Exception("Invalid format. IC format: xxxxxx-xx-xxxx, Passport format: letter followed by 8 numbers");
        }
    
        
        try {
            if (!isUnique(icPassport, "", "")) {
                String type = icPassport.length() == 14 ? "IC" : "Passport";
                throw new Exception(type + " number already exists");
            }
        } catch (IOException e) {
            throw new Exception("Error checking " + (icPassport.length() == 14 ? "IC" : "Passport") + " number uniqueness");
        }
    }
    
    /**
     * Validates username format and uniqueness
     */
    public static void validateUsername(String username) throws Exception {
        if (username.length() < 3 || username.length() > 12) {
            throw new Exception("Username must be between 3 and 12 characters long");
        }
        
        if (!username.matches("^[a-zA-Z0-9_]*$")) {
            throw new Exception("Username can only contain letters, numbers, and underscores (_)");
        }
        
        if (!username.matches(".*[a-zA-Z]+.*")) {
            throw new Exception("Username must contain at least one letter");
        }
        
        try {
            if (!isUnique("", username, "")) {
                throw new Exception("Username already exists");
            }
        } catch (IOException e) {
            throw new Exception("Error checking username uniqueness");
        }
    }
    
    /**
     * Validates password format and uniqueness
     */
    public static void validatePassword(String password, String username) throws Exception {
        if (password.length() < 8 || password.length() > 12) {
            throw new Exception("Password must be between 8 and 12 characters long");
        }
        
        if (password.contains(username)) {
            throw new Exception("Password cannot contain username");
        }
        
        if (!password.matches(".*\\d.*")) {
            throw new Exception("Password must contain at least one number");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            throw new Exception("Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new Exception("Password must contain at least one special character (!@#$%^&*())");
        }
        
        if (password.matches(".*[^a-zA-Z0-9!@#$%^&*()].*")) {
            throw new Exception("Password contains invalid characters");
        }
    }
    
    public static void validateContactNumber(String contactNumber) throws Exception {
        
        String rawNumber = contactNumber.replace("-", "");
        if (!rawNumber.matches("^01\\d{8}$")) {
            throw new Exception("Contact number must be in format: 01X-XXX-XXXX (with hyphens)");
        }
        
        if (contactNumber.length() != 12) {
            throw new Exception("Contact number must be in format: 01X-XXX-XXXX (Example: 012-345-6789)");
        }
        
        if (contactNumber.charAt(3) != '-' || contactNumber.charAt(7) != '-') {
            throw new Exception("Contact number must contain hyphens in correct positions (Example: 012-345-6789)");
        }
        
        try {
            if (!isUnique("", "", contactNumber)) {
                throw new Exception("Contact number already exists");
            }
        } catch (IOException e) {
            throw new Exception("Error checking contact number uniqueness");
        }
    }

    // Validate the authorization code
    public static boolean isValidAuthCode(String authCode, String role) {
        
        Map<String, List<String>> validAuthCodes = new HashMap<>();
        validAuthCodes.put("manager", Arrays.asList("KhongCL", "kcl", "AUTH789"));
        validAuthCodes.put("staff", Arrays.asList("kcls", "kynax", "AUTH456"));
    
        return validAuthCodes.getOrDefault(role, Collections.emptyList()).contains(authCode);
    }

    // Register a new manager
    public static void registerManager(String icPassportNumber, String username, String password, String contactNumber) throws Exception {
        
        if (icPassportNumber.isEmpty() || username.isEmpty() || password.isEmpty() || contactNumber.isEmpty()) {
            throw new Exception("Please fill in all fields.");
        }
    
        
        validateICPassport(icPassportNumber);
        validateUsername(username);
        validatePassword(password, username);
        validateContactNumber(contactNumber);
    
        
        String dateOfRegistration = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String userID = generateUserID("U");
        String managerID = generateUserID("M");
        Manager manager = new Manager(managerID, userID, icPassportNumber, username, password, 
            contactNumber, dateOfRegistration, "manager", true, null);
        manager.saveToManagerFile(null, null, "unapproved_managers.txt");
    }
    
    // Register a new staff
    public static void registerStaff(String icPassportNumber, String username, String password, String contactNumber) throws Exception {
        
        if (icPassportNumber.isEmpty() || username.isEmpty() || password.isEmpty() || contactNumber.isEmpty()) {
            throw new Exception("Please fill in all fields.");
        }
    
        
        validateICPassport(icPassportNumber);
        validateUsername(username);
        validatePassword(password, username);
        validateContactNumber(contactNumber);
    
        
        String dateOfRegistration = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String userID = generateUserID("U");
        String staffID = generateUserID("S");
        Staff staff = new Staff(staffID, userID, icPassportNumber, username, password, 
            contactNumber, dateOfRegistration, "staff", true, null);
        staff.saveToStaffFile(null, null, "unapproved_staffs.txt");
    }
    
    // Register a new resident
    public static void registerResident(String icPassportNumber, String username, String password, String contactNumber) throws Exception {
        
        if (icPassportNumber.isEmpty() || username.isEmpty() || password.isEmpty() || contactNumber.isEmpty()) {
            throw new Exception("Please fill in all fields.");
        }
    
        
        validateICPassport(icPassportNumber);
        validateUsername(username);
        validatePassword(password, username);
        validateContactNumber(contactNumber);
    
        
        String dateOfRegistration = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String userID = generateUserID("U");
        String residentID = generateUserID("R");
        Resident resident = new Resident(residentID, userID, icPassportNumber, username, password, 
            contactNumber, dateOfRegistration, "resident", true, null);
        resident.saveToResidentFile(null, null, "unapproved_residents.txt");
    }

    // Login as a manager
    public static User loginManager(String username, String password) {
        try {
            User user = User.findUser(username, password, "approved_managers.txt");
            if (user != null && user.getRole().equals("manager")) {
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Login as a staff
    public static User loginStaff(String username, String password) {
        try {
            User user = User.findUser(username, password, "approved_staffs.txt");
            if (user != null && user.getRole().equals("staff")) {
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Login as a resident
    public static User loginResident(String username, String password) {
        try {
            User user = User.findUser(username, password, "approved_residents.txt");
            if (user != null && user.getRole().equals("resident")) {
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Generate unique user ID based on role
    static String generateUserID(String prefix) {
        int id = 1;
        String filename = null;
        switch (prefix) {
            case "U" -> filename = "users.txt";
            case "M" -> filename = "approved_managers.txt";
            case "S" -> filename = "approved_staffs.txt";
            case "R" -> filename = "approved_residents.txt";
            default -> {
            }
        }
        
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
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
        }
        return prefix + String.format("%02d", id);
    }

    // Validate user IC/Passport number for updating user details
    public static String validateUpdateICPassport(String icPassport, String currentICPassport) {
        try {
            validateICPassport(icPassport);
            return null; 
        } catch (Exception e) {
            return e.getMessage();
        }
    }
  
    // Validate user username for updating user details
    public static String validateUpdateUsername(String username, String currentUsername) {
        try {
            validateUsername(username);
            return null; 
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    // Validate user password for updating user details
    public static String validateUpdatePassword(String password, String username) {
        try {
            validatePassword(password, username);
            return null; 
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    // Validate user contact number for updating user details
    public static String validateUpdateContactNumber(String contactNumber, String currentContactNumber) {
        try {
            validateContactNumber(contactNumber);
            return null; 
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}





