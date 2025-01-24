package apu.hostel.management;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffGenerateReceiptGUI {
    private JFrame frame;
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private Map<Integer, String[]> paymentDetailsMap;
    private APUHostelManagement.Staff staff;

    public StaffGenerateReceiptGUI(APUHostelManagement.Staff staff) {
        this.staff = staff;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Generate Receipt");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLayout(new BorderLayout(10, 10));

        // Back button panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> {
            new StaffMainPageGUI(staff);
            frame.dispose();
        });
        topPanel.add(backButton, BorderLayout.WEST);

        // View Receipts button
        JButton viewReceiptsButton = new JButton("View Receipts");
        viewReceiptsButton.addActionListener(e -> {
            new StaffViewReceiptGUI(staff);
            frame.dispose();
        });
        topPanel.add(viewReceiptsButton, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        // Payment table
        tableModel = new DefaultTableModel(
            new Object[]{"Payment ID", "Resident ID", "Staff ID", "Start Date", "End Date", 
                        "Room ID", "Amount", "Payment Method", "Booking Date"}, 0
        );
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Generate Receipt button
        JButton generateReceiptButton = new JButton("Generate Receipt");
        generateReceiptButton.addActionListener(e -> generateReceipt());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(generateReceiptButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        loadEligiblePayments();
        frame.setVisible(true);
    }

    private void loadEligiblePayments() {
        paymentDetailsMap = new HashMap<>();
        tableModel.setRowCount(0);
        
        try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] payment = line.split(",");
                // Check if payment is eligible (has staff ID, payment method, paid status, and active status)
                if (payment[2] != null && !payment[2].isEmpty() && 
                    payment[9] != null && !payment[9].isEmpty() &&
                    payment[7].equalsIgnoreCase("paid") &&
                    payment[10].equalsIgnoreCase("active")) {
                    
                    paymentDetailsMap.put(row, payment);
                    tableModel.addRow(new Object[]{
                        payment[0], payment[1], payment[2], payment[3], payment[4],
                        payment[5], payment[6], payment[9], payment[8]
                    });
                    row++;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading payments", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateReceipt() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a payment to generate receipt for.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] selectedPayment = paymentDetailsMap.get(selectedRow);
        
        // Create payment details panel for confirmation
        JPanel detailsPanel = new JPanel(new BorderLayout());
        String[][] data = {
            {"Payment ID", selectedPayment[0]},
            {"Resident ID", selectedPayment[1]},
            {"Staff ID", selectedPayment[2]},
            {"Start Date", selectedPayment[3]},
            {"End Date", selectedPayment[4]},
            {"Room ID", selectedPayment[5]},
            {"Amount", selectedPayment[6]},
            {"Payment Method", selectedPayment[9]},
            {"Booking Date", selectedPayment[8]}
        };
        JTable detailsTable = new JTable(data, new String[]{"Field", "Value"});
        detailsPanel.add(new JScrollPane(detailsTable), BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(frame, detailsPanel, 
            "Confirm Receipt Generation", JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            try {
                // Generate receipt ID
                List<String[]> receipts = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("receipts.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        receipts.add(line.split(","));
                    }
                } catch (IOException e) {
                    // Handle if receipts.txt doesn't exist yet
                }
                
                String receiptID = "RC" + String.format("%02d", receipts.size() + 1);
                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                // Add new receipt
                String[] newReceipt = {receiptID, selectedPayment[0], staff.getStaffID(), currentDateTime};
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("receipts.txt", true))) {
                    writer.write(String.join(",", newReceipt));
                    writer.newLine();
                }
                
                // Update payment status
                selectedPayment[10] = "completed";
                List<String[]> allPayments = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("payments.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] payment = line.split(",");
                        if (payment[0].equals(selectedPayment[0])) {
                            payment[10] = "completed";
                        }
                        allPayments.add(payment);
                    }
                }
                
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("payments.txt"))) {
                    for (String[] payment : allPayments) {
                        writer.write(String.join(",", payment));
                        writer.newLine();
                    }
                }
                
                JOptionPane.showMessageDialog(frame, "Receipt generated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadEligiblePayments(); // Refresh table
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error generating receipt", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
