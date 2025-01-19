package apu.hostel.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerMainPageGUI {
    private JFrame frame;

    public ManagerMainPageGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Manager Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLayout(new BorderLayout(10, 10)); // Add spacing between components

        JLabel managerLabel = new JLabel("Manager Menu", JLabel.CENTER);
        managerLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size
        frame.add(managerLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton approveUserButton = new JButton("Approve User Registration");
        approveUserButton.setPreferredSize(new Dimension(300, 50)); // Set button size
        approveUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManagerApproveUserRegistrationGUI();
                frame.dispose(); // Close the current window
            }
        });
        buttonPanel.add(approveUserButton, gbc);

        gbc.gridy++;
        JButton searchUserButton = new JButton("Search, Update, Delete or Restore User");
        searchUserButton.setPreferredSize(new Dimension(300, 50)); // Set button size
        searchUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchUsers();
            }
        });
        buttonPanel.add(searchUserButton, gbc);

        gbc.gridy++;
        JButton fixRateButton = new JButton("Fix, Update, Delete or Restore Rate");
        fixRateButton.setPreferredSize(new Dimension(300, 50)); // Set button size
        fixRateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fixOrUpdateRate();
            }
        });
        buttonPanel.add(fixRateButton, gbc);

        gbc.gridy++;
        JButton manageRoomsButton = new JButton("Manage Rooms");
        manageRoomsButton.setPreferredSize(new Dimension(300, 50)); // Set button size
        manageRoomsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageRooms();
            }
        });
        buttonPanel.add(manageRoomsButton, gbc);

        gbc.gridy++;
        JButton updatePersonalInfoButton = new JButton("Update Personal Information");
        updatePersonalInfoButton.setPreferredSize(new Dimension(300, 50)); // Set button size
        updatePersonalInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePersonalInformation();
            }
        });
        buttonPanel.add(updatePersonalInfoButton, gbc);

        gbc.gridy++;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(300, 50)); // Set button size
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        buttonPanel.add(logoutButton, gbc);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void searchUsers() {
        // Implement the logic for searching, updating, deleting, or restoring users
        JOptionPane.showMessageDialog(frame, "Search, Update, Delete or Restore User functionality to be implemented.");
    }

    private void fixOrUpdateRate() {
        // Implement the logic for fixing, updating, deleting, or restoring rates
        JOptionPane.showMessageDialog(frame, "Fix, Update, Delete or Restore Rate functionality to be implemented.");
    }

    private void manageRooms() {
        // Implement the logic for managing rooms
        JOptionPane.showMessageDialog(frame, "Manage Rooms functionality to be implemented.");
    }

    private void updatePersonalInformation() {
        // Implement the logic for updating personal information
        JOptionPane.showMessageDialog(frame, "Update Personal Information functionality to be implemented.");
    }

    private void logout() {
        // Implement the logic for logging out
        JOptionPane.showMessageDialog(frame, "Logging out...");
        frame.dispose();
        WelcomePageGUI welcomePage = new WelcomePageGUI(); // Create an instance of WelcomePageGUI
        welcomePage.setVisible(true); // Set the WelcomePageGUI frame to visible
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManagerMainPageGUI window = new ManagerMainPageGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}