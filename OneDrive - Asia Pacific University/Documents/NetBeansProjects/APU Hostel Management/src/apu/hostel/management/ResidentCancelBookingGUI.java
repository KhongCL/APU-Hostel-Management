package apu.hostel.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList; // Import ArrayList
import java.io.*;

public class ResidentCancelBookingGUI {
    private JFrame frame;
    private JPanel cancelBookingPanel;
    private DefaultTableModel tableModel; // Store the table model
    private Map<Integer, String[]> paymentDetailsMap; // Map to store payment details
    private APUHostelManagement.Resident resident; // Add resident field
    private JTable table;

    // Add new constructor
    public ResidentCancelBookingGUI(APUHostelManagement.Resident resident) {
        this.resident = resident;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Cancel Booking");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLayout(new BorderLayout(10, 10)); // Add spacing between components

        // Top panel for the back button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ResidentManageBookingsGUI(resident);
                frame.dispose();
            }
        });
        topPanel.add(backButton, BorderLayout.WEST);
        frame.add(topPanel, BorderLayout.NORTH);

        cancelBookingPanel = new JPanel(new BorderLayout(10, 10));
        frame.add(cancelBookingPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Cancel Booking", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size
        cancelBookingPanel.add(titleLabel, BorderLayout.NORTH);

        // Create table model and table
        tableModel = new DefaultTableModel(new Object[]{"Payment ID", "Room Number", "Stay Duration", "Payment Amount", "Booking Date Time"}, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        table.setRowHeight(30); // Increase row height
        JScrollPane scrollPane = new JScrollPane(table);
        cancelBookingPanel.add(scrollPane, BorderLayout.CENTER);

        // Load cancellable bookings and populate table
        List<String[]> cancellableBookings = APUHostelManagement.Resident.getCancellableBookingsForResident(resident.getResidentID());
        Map<String, String> roomMap = APUHostelManagement.Resident.getRoomMap();
        paymentDetailsMap = new HashMap<>(); // Initialize the map
        int rowIndex = 0;
        for (String[] details : cancellableBookings) {
            paymentDetailsMap.put(rowIndex, details); // Store payment details in the map
            String roomNumber = roomMap.getOrDefault(details[5], "Unknown Room");
            long stayDuration = ChronoUnit.DAYS.between(LocalDate.parse(details[3]), LocalDate.parse(details[4]));
            tableModel.addRow(new Object[]{details[0], roomNumber, stayDuration + " days", "RM" + details[6], details[8]});
            rowIndex++;
        }

        // Add Cancel button
        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.setPreferredSize(new Dimension(200, 40)); // Adjusted button size
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    showCancelBookingPopup(paymentDetailsMap.get(selectedRow), selectedRow);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a booking to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Create a panel for the bottom button
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(cancelButton);
        cancelBookingPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void showCancelBookingPopup(String[] details, int rowIndex) {
        String roomNumber = APUHostelManagement.Resident.getRoomMap().getOrDefault(details[5], "Unknown Room");
        LocalDate startDate = LocalDate.parse(details[3]);
        LocalDate endDate = LocalDate.parse(details[4]);
        long stayDuration = ChronoUnit.DAYS.between(startDate, endDate);

        // Create payment details table
        String[][] data = {
            {"Payment ID", details[0]},
            {"Resident ID", details[1]},
            {"Start Date", startDate.toString()},
            {"End Date", endDate.toString()},
            {"Stay Duration", stayDuration + " days"},
            {"Room Number", roomNumber},
            {"Payment Amount", "RM" + details[6]},
            {"Payment Status", details[7]},
            {"Booking Date Time", details[8]},
            {"Booking Status", details[10]}
        };
        String[] columnNames = {"Category", "Details"};
        JTable detailsTable = new JTable(data, columnNames);
        detailsTable.setEnabled(false);

        // Create confirmation dialog
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(detailsTable), BorderLayout.CENTER);
        JLabel confirmLabel = new JLabel("Are you sure you want to cancel this booking?");
        confirmLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(confirmLabel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Booking Details",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            cancelBooking(details[0], rowIndex, details[5]);
        }
    }

    private void cancelBooking(String paymentID, int rowIndex, String roomID) {
        if (APUHostelManagement.Resident.cancelBooking(paymentID)) {
            JOptionPane.showMessageDialog(frame, "Booking cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            tableModel.removeRow(rowIndex); // Remove the cancelled row from the table
            updateRoomStatusToAvailable(roomID); // Update room status to available
        } else {
            JOptionPane.showMessageDialog(frame, "An error occurred while cancelling the booking.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoomStatusToAvailable(String roomID) {
        List<String[]> rooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("rooms.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(roomID)) {
                    parts[4] = "available"; // Assuming parts[4] is RoomStatus
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
}